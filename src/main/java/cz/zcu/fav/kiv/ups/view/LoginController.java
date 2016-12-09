package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LoginController extends BaseController {

    private static final Character[] UNSUPPORTED_CHARACTERS = {' '};

    private final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private TextField username;

    @FXML
    private Label validationMessage;

    @FXML
    private void initialize() {
        this.validationMessage.setText(StringUtils.EMPTY);
        Platform.runLater(() -> username.requestFocus());
        username.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {login();}
        });
    }

    @FXML
    public void login() {

        String text = getUsername();

        if (StringUtils.isEmpty(text)) {
            validationMessage.setText("Username is required."); return;
        }

        text = text.trim();
        List<Character> first = text.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        List<Character> second = Arrays.asList(UNSUPPORTED_CHARACTERS);
        boolean isCommon = Collections.disjoint(second, first);
        if (!isCommon) {
            validationMessage.setText("Username contains unsupported characters."); return;
        }

        validationMessage.setText(StringUtils.EMPTY);
        startLoadingWheel();
        Application.getInstance().setUsername(text);


        String[] names = new String[]{
                "Lukas", "Tomas", "Jiri", "Josef", "Vaclav",
                "Lukas", "Tomas", "Jiri", "Josef", "Vaclav",
                "Lukas", "Tomas", "Jiri", "Josef", "Vaclav",
        };

        new java.util.Timer().schedule(
                new java.util.TimerTask() {public void run() {
                    Platform.runLater(() ->
                            WindowManager.getInstance()
                                    .processView(new ViewDTO(ExplorerController.class, names)));
                }}, 500);
    }

    @FXML
    public void onEnter(ActionEvent ae){
        login();
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public String getUsername() {
        return this.username.getText();
    }

    @Override
    protected void nextScene(ViewDTO data) {
        assert (data != null && ExplorerController.class == data.getaClass());
        stopLoadingWheel();

        try {
            FXMLLoader loader = new FXMLLoader(FXMLTemplates.EXPLORER);
            Scene scene = new Scene(loader.load());
            ExplorerController controller = loader.getController();
            controller.processData(data.getObjects());
            WindowManager.getInstance().setView(controller, scene);
        } catch (IOException e) {
            logger.error("LoginController::nextScene()", e);
        }
    }

    @Override
    protected void showAlert(ViewDTO data) {
        assert (data != null && this.getClass() == data.getaClass());

    }

    @Override
    protected void processData(Object[] data) {}

    @Override
    protected void didStopLoadingWheel() {
        stopLoadingWheel();
    }
}
