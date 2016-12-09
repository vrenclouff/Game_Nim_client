package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.view.components.NodeUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;


public class GameController extends BaseController {

    private final Logger logger = LogManager.getLogger(GameController.class);

    private final static int MIN_HEIGHT = 75;

    private final static int LAYER_HEIGHT = 65;

    private final static int LAYER_SPACE = 15;

    @FXML
    private Label username;

    @FXML
    private Label counter;

    private List<Node> elements;

    @FXML
    private void initialize() {
        elements = NodeUtils.paneNodesByClass(content, new Class[]{HBox.class});
        elements.forEach(e -> {e.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectedLayer(e));});
        WindowManager.getInstance().setSize(content.getWidth(), getHeighForLayers());
    }

    private int getHeighForLayers() {
        int layers = 6;
        return MIN_HEIGHT+(layers*(LAYER_HEIGHT+LAYER_SPACE));
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
    }

    @FXML
    private void endTurn() {
        logger.info("Ukoncuji tah....");
        waitForTurnStart();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {public void run() {
                    Platform.runLater(() ->
                            WindowManager.getInstance().processView(
                                    new ViewDTO(GameController.class, null)));
                }}, 2000);
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
        assert (data != null && ExplorerController.class == data.getaClass());
        stopLoadingWheel();

    }

    @Override
    protected void showAlert(ViewDTO data) {
        assert (data != null && this.getClass() == data.getaClass());

    }

    @Override
    protected void processData(Object[] data) {
        waitForTurnStop();
    }

    @Override
    protected void didStopLoadingWheel() {
        stopLoadingWheel();
    }
}
