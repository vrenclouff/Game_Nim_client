package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import cz.zcu.fav.kiv.ups.core.InternalMsg;
import cz.zcu.fav.kiv.ups.network.Network;
import cz.zcu.fav.kiv.ups.network.NetworkState;
import cz.zcu.fav.kiv.ups.network.SNDMessage;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang.StringUtils;

import javax.swing.text.View;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vrenclouff on 07.12.16.
 */
public class WindowManager {

    private static final WindowManager INSTANCE = new WindowManager();

    private Stage stage;

    private Network network;

    public BaseController controller;

    public static void init(Stage stage, Network network) {
        INSTANCE.stage = stage;
        INSTANCE.network = network;

        INSTANCE.stage.initStyle(StageStyle.UNDECORATED);
    }

    public static WindowManager getInstance() {
        return INSTANCE;
    }

    /**
     * Nastavi aktualni obrazovku
     * @param controller - controller obshlujujici obrazovku
     * @param scene - obsah obrazovky
     */
    public void setView(BaseController controller, Scene scene) {
        setSize(controller.content.getPrefWidth(), controller.content.getPrefHeight());
        EffectUtilities.makeDraggable(stage, controller.moveBtn);
        controller.setNetwork(network);
        controller.setLoadingWheelToCenter();

        this.controller = controller;
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void processView(ViewDTO data) {
        Platform.runLater(() -> controller.setScene(data));
    }

    public void showAlert(InternalMsg state, String... content) {
        new Timer().schedule(new TimerTask() {public void run() {Platform.runLater(() -> {
            controller.stopLoadingWheel();
                switch (state) {
                    case INFO: {
                        PrettyAlert alert = new PrettyAlert(state.toString(), content[0]);
                        ButtonType buttonTypeYes = new ButtonType("Ok");
                        alert.addButtons(buttonTypeYes);
                        alert.showAndWait();
                    }
                    break;
                    case SERVER_AVAILABLE: {
                        PrettyAlert alert = new PrettyAlert(state.toString(), content[0]);
                        ButtonType buttonTypeYes = new ButtonType("Ok");
                        alert.addButtons(buttonTypeYes);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTypeYes) {
                            Application.getInstance().setUsername("");
                            showLoginScreen();
                        }
                    }
                    break;
                    default: {
                        controller.showAlert(state, content);
                    }
                    break;
                }
        });}}, 500);
    }

    public void logout() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> showLoginScreen());
            }
        }, 500);
    }

    public void login() { Platform.runLater(() -> showLoginScreen()); }

    public void closeWindow() {
        this.stage.setOnCloseRequest(e -> Platform.exit());
        Platform.exit();
        System.exit(0);
    }

    public void minimizeWindow() {
        this.stage.setIconified(true);
    }

    public void setSize(double width, double height) {
        this.stage.setWidth(width);
        this.stage.setHeight(height);
    }

    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLTemplates.LOGIN);
            Parent parent = loader.load();
            Scene loginScene = new Scene(parent);
            LoginController loginController = loader.getController();
            loginController.setNetwork(network);
            setView(loginController, loginScene);
            String username = Application.getInstance().getUsername();
            if (StringUtils.isNotEmpty(username)) {
                loginController.setUsername(username);
                loginController.login();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class EffectUtilities {
    /** makes a stage draggable using a given node */
    public static void makeDraggable(final Stage stage, final Node byNode) {
        final Delta dragDelta = new Delta();
        byNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = stage.getX() - mouseEvent.getScreenX();
                dragDelta.y = stage.getY() - mouseEvent.getScreenY();
                byNode.setCursor(Cursor.MOVE);
            }
        });
        byNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                byNode.setCursor(Cursor.DEFAULT);
            }
        });
        byNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() + dragDelta.x);
                stage.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
        byNode.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    byNode.setCursor(Cursor.DEFAULT);
                }
            }
        });
        byNode.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    byNode.setCursor(Cursor.DEFAULT);
                }
            }
        });
    }

    /** records relative x and y co-ordinates. */
    private static class Delta {
        double x, y;
    }
}
