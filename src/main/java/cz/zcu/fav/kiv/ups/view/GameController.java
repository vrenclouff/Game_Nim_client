package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import cz.zcu.fav.kiv.ups.core.GameSettings;
import cz.zcu.fav.kiv.ups.core.InternalMsg;
import cz.zcu.fav.kiv.ups.network.NetworkState;
import cz.zcu.fav.kiv.ups.network.SNDMessage;
import cz.zcu.fav.kiv.ups.view.components.NodeUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class GameController extends BaseController {

    private final Logger logger = LogManager.getLogger(GameController.class);

    private GameSettings settings;

    private final static int MIN_HEIGHT = 75;

    private final static int LAYER_HEIGHT = 65;

    private final static int LAYER_SPACE = 15;

    @FXML
    private Label username;

    @FXML
    private Label counter;

    @FXML
    private Button endTurnButton;

    private List<Node> elements;


    @FXML
    private void initialize() {
        settings = Application.getInstance().getSettings();
        elements = NodeUtils.paneNodesByClass(content, new Class[]{HBox.class});
        elements.forEach(e -> {
            e.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectedLayer(e));
        });
        resetCounter();
    }

    private void resetCounter() {
        counter.setText(String.valueOf(settings.getTaking()));
        endTurnButton.setDisable(true);
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public String getUsername() {
        return this.username.getText();
    }

    @FXML
    private void endGame() {
        logger.info("Exit game.");
        PrettyAlert alert = new PrettyAlert("Konec hry", "Chcete ukončit hru?");
        ButtonType buttonTypeYes = new ButtonType("Ano");
        ButtonType buttonTypeNo = new ButtonType("Ne");
        alert.addButtons(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes){
            network.send(new SNDMessage(NetworkState.GAME_END, StringUtils.EMPTY));
        }
    }

    @FXML
    private void endTurn() {
        network.send(new SNDMessage(NetworkState.GAME_SWITCH_USER, StringUtils.EMPTY));
    }

    private void waitForTurnStart() {
        this.content.setOpacity(0.8);
        counter.setVisible(false);
        endTurnButton.setVisible(false);
        elements.forEach(e -> {
            if (e.getStyleClass().contains("game_layer")) {
                e.getStyleClass().remove("game_layer");
            }
        });
    }

    private void waitForTurnStop() {
        this.content.setOpacity(1);
        counter.setVisible(true);
        endTurnButton.setVisible(true);
        elements.forEach(e -> {
            if (!e.getStyleClass().contains("game_layer")) {
                e.getStyleClass().add("game_layer");
            }
        });
    }

    private void selectedLayer(Node layer) {
        Integer cnt = Integer.valueOf(counter.getText());
        if (cnt > 0 && ((HBox)layer).getChildren().size() > 0) {
            logger.info("Selected layer: "+layer.getId());
            network.send(new SNDMessage(NetworkState.GAME_TAKE, layer.getId()));
        }
    }

    @Override
    protected void showAlert(InternalMsg state, String... content) {
        assert (state != null && content.length == 0);

        stopLoadingWheel();

        switch (state) {
            case GAME_DISCONNECT:
            {
                PrettyAlert alert = new PrettyAlert(state.toString(), content[0]);
                ButtonType buttonTypeYes = new ButtonType("Ano");
                ButtonType buttonTypeNo = new ButtonType("Ne");
                alert.addButtons(buttonTypeYes, buttonTypeNo);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeNo) {
                    network.send(new SNDMessage(NetworkState.GAME_END, StringUtils.EMPTY));
                }else if (result.get() == buttonTypeYes) {
                    startLoadingWheel();
                }
            }break;
            case GAME_END:
            {
                PrettyAlert alert = new PrettyAlert(state.toString(), content[0]);
                ButtonType buttonTypeOk = new ButtonType("Ok");
                alert.addButtons(buttonTypeOk);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOk) {
                    network.send(new SNDMessage(NetworkState.ALL_USERS, StringUtils.EMPTY));
                }
            }break;
            case FINISH:
            {
                String result = content[0];
                if (result.equalsIgnoreCase("WIN")){
                    WindowManager.getInstance().showFinishWindow(FXMLTemplates.WIN);
                }else if (result.equalsIgnoreCase("LOSE")) {
                    WindowManager.getInstance().showFinishWindow(FXMLTemplates.LOSE);
                }
            }break;
        }
    }

    @Override
    protected void processData(Object[] data) {

         stopLoadingWheel();
        username.setText(Application.getInstance().getUsername());

        if (!data[0].getClass().isEnum()) { return; }

        InternalMsg state = (InternalMsg) data[0];

        switch (state) {
            case START:
            {
                if (data.length < 1) { return; }
                String onTurn = ((String) data[1]).trim();
                if (onTurn.equals("START")) {
                    waitForTurnStop();
                }else if (onTurn.equals("STOP")){
                    waitForTurnStart();
                }
            }break;
            case TAKE:
            {
                if (data.length < 2) { return; }
                String onTurn = ((String) data[1]).trim();
                if (onTurn.equals("START")) {
                    waitForTurnStop();
                }else if (onTurn.equals("STOP")){
                    waitForTurnStart();
                }

                String layer = ((String) data[2]).trim();
                for(Node el : elements){
                    HBox item = (HBox)el;

                    if (item.getId().equals(layer) && item.getChildren().size() > 0) {
                        item.getChildren().remove(0);
                        Integer cnt = Integer.valueOf(counter.getText());
                        if (cnt > 0) counter.setText((--cnt).toString());
                        endTurnButton.setDisable(false);
                        break;
                    }
                }

            }break;
            case SWITCH_USER:
            {
                if (data.length < 1) { return; }
                String onTurn = ((String) data[1]).trim();
                if (onTurn.equals("START")) {
                    resetCounter();
                    waitForTurnStop();
                }else if (onTurn.equals("STOP")){
                    waitForTurnStart();
                }
            }break;
            case STATE:
            {
                if (data.length < 3) { return; }
                String [] numbers = ((String)data[2]).split(",");
                for(int i = 0; i < numbers.length; i++) {
                    HBox item = elementByID(String.valueOf(i));
                    if (item == null) continue;

                    int childrenSize = item.getChildren().size();
                    int size = Integer.valueOf(numbers[i]);

                    int delete = childrenSize - size;
                    int add = size - childrenSize;

                    if (delete > 0){
                        for(int j = 0; j < delete; j++) {
                            item.getChildren().remove(0);
                        }
                    }else if (add > 0) {
                        for(int j = 0; j < add; j++) {
                            ImageView image =  new ImageView("/images/match.png");
                            image.setFitHeight(70);
                            image.setFitWidth(20);
                            item.getChildren().add(0, image);
                        }
                    }
                }

                String onTurn = ((String) data[1]).trim();
                if (onTurn.equals("START")) {
                    waitForTurnStop();
                }else if (onTurn.equals("STOP")){
                    waitForTurnStart();
                }

                Integer cnt = Integer.valueOf(((String)data[3]).trim());
                if (cnt == 0) endTurnButton.setDisable(false);
                counter.setText(cnt.toString());

                content.setPrefHeight(MIN_HEIGHT+(numbers.length*(LAYER_HEIGHT+LAYER_SPACE)));

            }break;
        }

    }

    private HBox elementByID(String id) {
         for (Node item : elements) {
             if (item.getId().equalsIgnoreCase(id)) return (HBox)item;
         }
         return null;
    }

    @Override
    protected void didStopLoadingWheel() {
        stopLoadingWheel();
        network.send(new SNDMessage(NetworkState.GAME_END, StringUtils.EMPTY));
    }
}
