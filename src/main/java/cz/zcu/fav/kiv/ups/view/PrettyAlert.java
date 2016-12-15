package cz.zcu.fav.kiv.ups.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Created by Lukas Cerny on 10.12.16.
 */
public class PrettyAlert {

    private static final String CSS = "/css/style.css";

    private static final String CLASS_NAME = "pretty_alert";

    private Alert alert;

    public PrettyAlert(String header, String content) {
        alert = new Alert(Alert.AlertType.NONE);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UNDECORATED);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource(CSS).toExternalForm());
        dialogPane.getStyleClass().add(CLASS_NAME);
    }


    public void addButtons(ButtonType... buttons) {
        alert.getButtonTypes().setAll(buttons);

        ButtonBar buttonBar = (ButtonBar)alert.getDialogPane().lookup(".button-bar");
        buttonBar.getButtons().forEach(b->b.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color: white;" +
                "-fx-text-fill: white"));

    }

    public Optional<ButtonType> showAndWait() {
        return alert.showAndWait();
    }

}
