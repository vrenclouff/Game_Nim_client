package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import cz.zcu.fav.kiv.ups.core.InternalMsg;
import cz.zcu.fav.kiv.ups.network.NetworkState;
import cz.zcu.fav.kiv.ups.network.SNDMessage;
import cz.zcu.fav.kiv.ups.view.components.CellButton;
import cz.zcu.fav.kiv.ups.view.components.UserCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.*;

public class ExplorerController extends BaseController {

    private final Logger logger = LogManager.getLogger(ExplorerController.class);

    @FXML
    private ListView<CellButton> listOfUser;

    @FXML
    private Label username;

    private CellButton selectedCell;

    @FXML
    private void initialize() {
        username.setText(Application.getInstance().getUsername());
        listOfUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> listViewDidSelectRow(newValue));
        listOfUser.setCellFactory(param -> new UserCell(this));
    }

    public void listViewDidPlayRow() {
        startLoadingWheel();
        logger.info("Selected player with name: " + selectedCell.text);
        network.send(new SNDMessage(NetworkState.GAME_CHALLENGER, selectedCell.text));
    }

    private void listViewDidSelectRow(CellButton cell) {
        cell.visibleButton = true;
        if (selectedCell != null) selectedCell.visibleButton = false;
        selectedCell = cell;
        listOfUser.refresh();
    }

    @Override
    protected void nextScene(ViewDTO data) {
        if (data != null && GameController.class != data.getaClass()) {return;}
        stopLoadingWheel();

        try {
            FXMLLoader loader = new FXMLLoader(FXMLTemplates.GAME);
            Scene scene = new Scene(loader.load());
            GameController controller = loader.getController();
            controller.processData(data.getObjects());
            WindowManager.getInstance().setView(controller, scene);
        } catch (IOException e) {
            logger.error("GameController::nextScene()", e);
        }
    }

    @Override
    protected void showAlert(InternalMsg state, String... content) {
        assert (state != null);

        switch (state) {
            case INVITE:
            {
                PrettyAlert alert = new PrettyAlert(state.toString(), content[0]);
                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeNo = new ButtonType("No");
                alert.addButtons(buttonTypeYes, buttonTypeNo);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeYes) {
                    network.send(new SNDMessage(NetworkState.GAME_INVITE, ("ACCEPT" + " " + content[1])));
                }else if (result.get() == buttonTypeNo) {
                    network.send(new SNDMessage(NetworkState.GAME_INVITE, ("IGNORE" + " " + content[1])));
                }
            }break;
        }
    }

    @Override
    protected void processData(Object[] data) {
        stopLoadingWheel();
        ObservableList<CellButton> list = FXCollections.observableArrayList();
        Arrays.asList(data).stream().forEach((e -> list.add(new CellButton((String)e))));
        listOfUser.getItems().clear();
        listOfUser.getSelectionModel().clearSelection();
        listOfUser.setItems(list);
    }

    @Override
    protected void didStopLoadingWheel() {
        stopLoadingWheel();
    }
}
