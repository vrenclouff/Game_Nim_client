package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import cz.zcu.fav.kiv.ups.view.components.CellButton;
import cz.zcu.fav.kiv.ups.view.components.UserCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

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
        logger.info("Send request to " + selectedCell.text);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {public void run() {
                    Platform.runLater(() ->
                            WindowManager.getInstance().processView(
                                    new ViewDTO(GameController.class, null)));
                }}, 500);
    }

    private void listViewDidSelectRow(CellButton cell) {
        cell.visibleButton = true;
        if (selectedCell != null) selectedCell.visibleButton = false;
        selectedCell = cell;
        listOfUser.refresh();





       /* Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setHeaderText("You should play with "+username);

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes){
            logger.info("I will play with "+username);
        } else if (result.get() == buttonTypeNo) {
            logger.info("I will not play with "+username);
        }
        */
    }

    @Override
    protected void nextScene(ViewDTO data) {
        assert (data != null && ExplorerController.class == data.getaClass());
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
    protected void showAlert(ViewDTO data) {
        assert (data != null && this.getClass() == data.getaClass());

    }

    @Override
    protected void processData(Object[] data) {
        stopLoadingWheel();
        ObservableList<CellButton> list = FXCollections.observableArrayList();
        Arrays.stream(data).forEach(e -> list.add(new CellButton((String)e)));
        listOfUser.setItems(list);
    }

    @Override
    protected void didStopLoadingWheel() {
        stopLoadingWheel();
    }
}
