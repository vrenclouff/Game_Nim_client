package cz.zcu.fav.kiv.ups.core;

import cz.zcu.fav.kiv.ups.network.Network;
import cz.zcu.fav.kiv.ups.network.NetworkServiceImpl;
import cz.zcu.fav.kiv.ups.view.WindowManager;
import javafx.stage.Stage;
import org.apache.log4j.*;

/**
 * Created by vrenclouff on 06.12.16.
 */
public class Application {

    private final Logger logger = LogManager.getLogger(Application.class);

    private static final Application INSTANCE = new Application();

    private String username;

    private GameSettings settings;

    private Network network;

    private Application(){}

    public static Application getInstance() {
        return INSTANCE;
    }

    public void start(Parameters params, Stage primaryStage) {

        logger.info("Application start.");

        this.username = params.getUsername();
        this.network = new NetworkServiceImpl(params.getAddress(), params.getPort());

        WindowManager.init(primaryStage, network);
        WindowManager.getInstance().login();
        network.connect();
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }
}
