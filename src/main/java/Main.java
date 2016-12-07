
import cz.zcu.fav.kiv.ups.core.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Arrays;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) {

        cz.zcu.fav.kiv.ups.core.Parameters params = new cz.zcu.fav.kiv.ups.core.Parameters(Arrays.copyOf(
                getParameters().getRaw().toArray(), getParameters().getRaw().size(), String[].class));

        Application.getInstance().start(params, primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

