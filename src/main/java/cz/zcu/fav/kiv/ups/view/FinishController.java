package cz.zcu.fav.kiv.ups.view;


import cz.zcu.fav.kiv.ups.network.Network;
import cz.zcu.fav.kiv.ups.network.NetworkState;
import cz.zcu.fav.kiv.ups.network.SNDMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

public class FinishController {


    private Network network;

    @FXML
    private Button closeButton;

    @FXML
    private void ok() {
        network.send(new SNDMessage(NetworkState.ALL_USERS, StringUtils.EMPTY));
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
