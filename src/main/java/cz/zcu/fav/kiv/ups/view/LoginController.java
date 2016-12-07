package cz.zcu.fav.kiv.ups.view;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LoginController {

    private final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private Pane content;

    @FXML
    private ProgressIndicator loadingWheel;

    @FXML
    public void login() {
        loadingWheel.setVisible(true);
        content.setOpacity(0.5);
    }
}
