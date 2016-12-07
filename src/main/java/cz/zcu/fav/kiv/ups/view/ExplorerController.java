package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

public class ExplorerController extends BaseController {

    private final Logger logger = LogManager.getLogger(ExplorerController.class);


    @FXML
    private Pane content;

    @FXML
    private ListView<String> listOfUser;

    @FXML
    private Label username;

    ObservableList<String> data = FXCollections.observableArrayList(
            "Lukas", "Tomas", "Jiri", "Josef", "Vaclav", "Vera", "Katerina",
            "Lukas", "Tomas", "Jiri", "Josef", "Vaclav", "Vera", "Katerina",
            "Lukas", "Tomas", "Jiri", "Josef", "Vaclav", "Vera", "Katerina");

    @FXML
    private void initialize() {
        username.setText(Application.getInstance().getUsername());
        listOfUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedUser(newValue));
        listOfUser.setItems(data);
    }

    private void selectedUser(String username) {
        logger.info("Send request to " + username);

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
    protected void setScene(ViewDTO data) {

    }

    @Override
    protected void actualizeScene(ViewDTO data) {

    }

    @Override
    protected void nextScene(ViewDTO data) {

    }

    @Override
    protected void showAlert(ViewDTO data) {

    }
}
