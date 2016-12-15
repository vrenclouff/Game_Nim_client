package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import cz.zcu.fav.kiv.ups.core.GameSettings;
import cz.zcu.fav.kiv.ups.core.InternalMsg;
import cz.zcu.fav.kiv.ups.view.components.NodeUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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

    private List<Node> elements;

     public GameController() {
        this.settings = Application.getInstance().getSettings();
     }

    @FXML
    private void initialize() {
        username.setText(Application.getInstance().getUsername());
        elements = NodeUtils.paneNodesByClass(content, new Class[]{HBox.class});
        elements.forEach(e -> {e.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectedLayer(e));});
        content.setPrefHeight(MIN_HEIGHT+(settings.getLayers()*(LAYER_HEIGHT+LAYER_SPACE)));
        resetCounter();
    }

    private void resetCounter() {
        counter.setText(String.valueOf(settings.getTaking()));
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public String getUsername() {
        return this.username.getText();
    }

    @FXML
    private void endGame() {
        logger.info("Ukoncuji hru....");
        PrettyAlert alert = new PrettyAlert("End game", "Do you want exit game?");
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.addButtons(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes){
            /*
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
                   */
        }
    }

    @FXML
    private void endTurn() {
        logger.info("Ukoncuji tah....");
        waitForTurnStart();
/*
        new java.util.Timer().schedule(
                new java.util.TimerTask() {public void run() {
                    Platform.runLater(() ->
                            WindowManager.getInstance().processView(
                                    new ViewDTO(GameController.class, null)));
                }}, 2000);
          */
    }

    private void waitForTurnStart() {
        this.content.setOpacity(0.8);
        elements.forEach(e -> {
            if (e.getStyleClass().contains("game_layer")) {
                e.getStyleClass().remove("game_layer");
            }
        });
    }

    private void waitForTurnStop() {
        this.content.setOpacity(1);
        this.counter.setText("3");
        elements.forEach(e -> {
            if (!e.getStyleClass().contains("game_layer")) {
                e.getStyleClass().add("game_layer");
            }
        });
    }

    private void selectedLayer(Node layer) {
        logger.info("Selected layer: "+layer.getId());
        Integer cnt = Integer.valueOf(counter.getText());
        if (cnt == 0) return;
        if (((HBox)layer).getChildren().size() > 0) {
            ((HBox) layer).getChildren().remove(0);
            counter.setText((--cnt).toString());
        }
    }

    @Override
    protected void nextScene(ViewDTO data) {
        if (data != null && ExplorerController.class != data.getaClass()) { return; }
        stopLoadingWheel();

        try {
            FXMLLoader loader = new FXMLLoader(FXMLTemplates.EXPLORER);
            Scene scene = new Scene(loader.load());
            ExplorerController controller = loader.getController();
            controller.processData(data.getObjects());
            WindowManager.getInstance().setView(controller, scene);
        } catch (IOException e) {
            logger.error("GameController::nextScene()", e);
        }
    }

    @Override
    protected void showAlert(InternalMsg state, String... content) {
        assert (state != null);

    }

    @Override
    protected void processData(Object[] data) {

        String state = ((String)data[0]).trim();
        if (state.equals("START")) {
            waitForTurnStop();
        }else if (state.equals("STOP")) {
            waitForTurnStart();
        }

        if (data.length == 2) {
            Integer layer = Integer.valueOf((String) data[1]);

        }
    }

    @Override
    protected void didStopLoadingWheel() {
        stopLoadingWheel();
    }
}
