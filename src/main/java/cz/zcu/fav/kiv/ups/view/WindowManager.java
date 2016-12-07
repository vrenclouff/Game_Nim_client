package cz.zcu.fav.kiv.ups.view;

import cz.zcu.fav.kiv.ups.core.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by vrenclouff on 07.12.16.
 */
public class WindowManager {

    private static final WindowManager INSTANCE = new WindowManager();

    private Stage stage;

    public BaseController controller;

    public static void init(Stage stage) {
        INSTANCE.stage = stage;

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
        EffectUtilities.makeDraggable(stage, controller.moveBtn);
        this.controller = controller;
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void processView(ViewDTO data) {
        this.controller.setScene(data);
    }

    public void showAlert(ViewDTO data) {
        this.controller.showAlert(data);
    }

    public void logout() {
        Application.getInstance().showLoginScreen("");
    }

    public void closeWindow() {
        // TODO logout
        this.stage.setOnCloseRequest(e -> Platform.exit());
        Platform.exit();
        System.exit(0);
    }

    public void minimizeWindow() {
        this.stage.setIconified(true);
    }

    class Delta { double x, y; }
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
