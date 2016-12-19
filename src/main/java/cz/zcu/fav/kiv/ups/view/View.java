package cz.zcu.fav.kiv.ups.view;

import javafx.scene.Scene;

/**
 * Created by Lukas Cerny on 19.12.16.
 */
public class View {

    private BaseController controller;

    private Scene scene;

    public View(BaseController controller, Scene scene) {
        this.controller = controller;
        this.scene = scene;
    }


    public BaseController getController() {
        return controller;
    }

    public Scene getScene() {
        return scene;
    }
}
