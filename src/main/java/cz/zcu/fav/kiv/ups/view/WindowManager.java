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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.INITIALIZE;

import javax.swing.text.View;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Created by vrenclouff on 07.12.16.
 */
public class WindowManager {

    private static final WindowManager INSTANCE = new WindowManager();

    private Stage stage;

    private Network network;

    public BaseController controller;

    public Map<Class<? extends BaseController>, cz.zcu.fav.kiv.ups.view.View> views;

    public void initScenes() {
        this.views = new HashMap<>();

        createView(FXMLTemplates.LOGIN);
        createView(FXMLTemplates.EXPLORER);
        createView(FXMLTemplates.GAME);

        if (!views.containsKey(LoginController.class) ||
                !views.containsKey(ExplorerController.class) ||
                !views.containsKey(GameController.class)) {
            System.exit(1);
        }
    }

    public static void init(Stage stage, Network network) {
        INSTANCE.stage = stage;
        INSTANCE.network = network;

        INSTANCE.initScenes();
        INSTANCE.stage.initStyle(StageStyle.UNDECORATED);
    }

    public static WindowManager getInstance() {
        return INSTANCE;
    }

    public void setView(BaseController controller, Scene scene) {
        setSize(controller.content.getPrefWidth(), controller.content.getPrefHeight());
        EffectUtilities.makeDraggable(stage, controller.moveBtn);
        controller.setNetwork(network);
        controller.setLoadingWheelToCenter();

        this.controller = controller;
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void showFinishWindow(URL template) {

        try {
            FXMLLoader loader = new FXMLLoader(template);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.initOwner(stage);
            dialogStage.setScene(new Scene(loader.load()));

            FinishController controller = loader.getController();
            controller.setNetwork(network);
            dialogStage.showAndWait();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processView(ViewDTO data) {
        Platform.runLater(() ->{
            cz.zcu.fav.kiv.ups.view.View view = views.get(data.getaClass());
            view.getController().processData(data.getObjects());
            setView(view.getController(), view.getScene());
        });
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
                            login();
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
                login();
            }
        }, 500);
    }

    public void login() {
        processView(new ViewDTO(LoginController.class, new Object[]{}));
    }

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

    private void createView(URL template){
        if (template == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(template);
            Scene scene = new Scene(loader.load());
            BaseController controller = loader.getController();
            views.put(controller.getClass(), new cz.zcu.fav.kiv.ups.view.View(controller, scene));
        } catch (IOException e) {
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
