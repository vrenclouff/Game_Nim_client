package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class LoginController extends BaseController {

    private final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private Pane content;

    @FXML
    private TextField username;

    @FXML
    private Label validationMessage;

    @FXML
    private ProgressIndicator loadingWheel;

    @FXML
    private void initialize() {
        this.validationMessage.setText(StringUtils.EMPTY);
    }

    @FXML
    public void login() {

        if (StringUtils.isEmpty(getUsername())) {
            validationMessage.setText("Username is required.");
            return;
        }

        validationMessage.setText(StringUtils.EMPTY);
        loadingWheel.setVisible(true);
        content.setOpacity(0.5);
        Application.getInstance().setUsername(getUsername());

        new java.util.Timer().schedule(
                new java.util.TimerTask() {public void run() {
                    Platform.runLater(() ->
                            WindowManager.getInstance()
                                    .processView(new ViewDTO(ExplorerController.class, null)));
                }}, 500);

    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public String getUsername() {
        return this.username.getText();
    }

    @Override
    protected void setScene(ViewDTO data) {
        assert (data != null);

        if (data.getaClass() == this.getClass()) {
            this.actualizeScene(data);
        }else {
            this.nextScene(data);
        }
    }

    @Override
    protected void actualizeScene(ViewDTO data) {
        assert (data != null && this.getClass() == data.getaClass());

    }

    @Override
    protected void nextScene(ViewDTO data) {
        assert (data != null && ExplorerController.class == data.getaClass());

        try {
            FXMLLoader loader = new FXMLLoader(FXMLTemplates.EXPLORER);
            Scene scene = new Scene(loader.load());
            ExplorerController controller = loader.getController();
            WindowManager.getInstance().setView(controller, scene);
        } catch (IOException e) {
            logger.error("LoginController::nextScene()", e);
        }
    }

    @Override
    protected void showAlert(ViewDTO data) {
        assert (data != null && this.getClass() == data.getaClass());

    }
}
