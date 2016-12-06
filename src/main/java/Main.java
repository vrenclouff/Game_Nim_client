
import cz.zcu.fav.kiv.ups.network.TcpComm;
import cz.zcu.fav.kiv.ups.view.FXMLTemplates;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import cz.zcu.fav.kiv.ups.view.RootController;

import java.util.Arrays;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        cz.zcu.fa.kiv.ups.core.Parameters params = new cz.zcu.fa.kiv.ups.core.Parameters(Arrays.copyOf(
                getParameters().getRaw().toArray(), getParameters().getRaw().size(), String[].class));

        cz.zcu.fa.kiv.ups.core.Application.getInstance().start(params);

        TcpComm comm = new TcpComm();
        FXMLLoader loader = new FXMLLoader(FXMLTemplates.DEMO);
        Parent root = loader.load();

        RootController controller = loader.getController();
        controller.setM_comm(comm);

        primaryStage.setTitle("Nim");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        comm.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

