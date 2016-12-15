package cz.zcu.fav.kiv.ups.view;

import delete.Comm;
import delete.UiCommObserver;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class RootController {

    @FXML
    private TextArea textArea;

    @FXML
    private Label label;

    private Comm m_comm;

    private UiCommObserver m_commObserver = new UiCommObserver();

    @FXML
    private void sendMessage() {
        send(textArea.getText());
    }

    @FXML
    private void LOGIN() {
        send("LOGIN");
    }

    @FXML
    private void ALL_USERS() {
        send("ALL_USERS");
    }

    @FXML
    private void GAME_JOIN() {
        send("GAME_JOIN");
    }

    @FXML
    private void GAME_TAKE() {
        send("GAME_TAKE");
    }

    @FXML
    private void UNDEFIND() {
        send("UNDEFIND");
    }

    @FXML
    private void LOGOUT() {
        send("LOGOUT");
    }
    @FXML
    private void GAME_END() {
        send("GAME_END");
    }
    @FXML
    private void GAME_SWITCH_USER() {
        send("GAME_SWITCH_USER");
    }
    @FXML
    private void GAME_TURN() {
        send("GAME_TURN");
    }
    @FXML
    private void GAME() {
        send("GAME");
    }

    private void send(String status) {
        String text = status + (textArea.getText().length()==0? "" : " "+textArea.getText());
        m_comm.send(text);
        textArea.setText("");
        label.setText("");
    }

    public void setM_comm(Comm m_comm) {
        this.m_comm = m_comm;
        this.m_comm.registerObserver(m_commObserver);
        m_commObserver.setM_label(label);
    }
}
