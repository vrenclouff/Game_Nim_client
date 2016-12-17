package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import cz.zcu.fav.kiv.ups.core.InternalMsg;
import cz.zcu.fav.kiv.ups.network.Network;
import cz.zcu.fav.kiv.ups.network.NetworkState;
import cz.zcu.fav.kiv.ups.network.SNDMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;

import java.util.Optional;


/**
 * Created by vrenclouff on 07.12.16.
 */
abstract class BaseController {


    @FXML
    protected Pane content;

    @FXML
    protected Button moveBtn;

    @FXML
    private Pane loadingPane;

    @FXML
    protected ProgressIndicator loadingWheel;

    @FXML
    protected Button loadingStopWheel;

    protected Network network;

    @FXML
    private void close() {
        WindowManager.getInstance().closeWindow();
    }

    @FXML
    private void minimize() {
        WindowManager.getInstance().minimizeWindow();
    }

    @FXML
    private void logout() {
        PrettyAlert alert = new PrettyAlert("Logout", "Do you want logout from application?");
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.addButtons(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            Application.getInstance().setUsername("");
            network.send(new SNDMessage(NetworkState.LOGOUT, ""));
        }
    }

    void startLoadingWheel() {
        this.loadingPane.setVisible(true);
        this.loadingStopWheel.setOnAction(e -> didStopLoadingWheel());
        this.content.setOpacity(0.5);
    }

    void stopLoadingWheel() {
        this.loadingPane.setVisible(false);
        this.content.setOpacity(1.0);
    }

    protected abstract void didStopLoadingWheel();

    protected void setScene(ViewDTO data) {
        assert (data != null);

        if (data.getaClass() == this.getClass()) {
            this.actualizeScene(data);
        }else {
            this.nextScene(data);
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    private void actualizeScene(ViewDTO data) {
        if (data != null && this.getClass() != data.getaClass()) { return; }
        this.processData(data.getObjects());
    }

    public void setLoadingWheelToCenter() {
        double x = content.getPrefWidth() / 2 - loadingPane.getMinWidth() / 2;
        double y = content.getPrefHeight() / 2 - loadingPane.getPrefHeight() / 2;
        this.loadingPane.setLayoutX(x);
        this.loadingPane.setLayoutY(y);
    }

    protected abstract void nextScene(ViewDTO data);

    protected abstract void showAlert(InternalMsg state, String... content);

    protected abstract void processData(Object[] data);
}
