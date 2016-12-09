package cz.zcu.fav.kiv.ups.core;

import cz.zcu.fav.kiv.ups.network.RCVMessage;
import cz.zcu.fav.kiv.ups.network.SNDMessage;
import cz.zcu.fav.kiv.ups.view.ExplorerController;
import cz.zcu.fav.kiv.ups.view.FXMLTemplates;
import cz.zcu.fav.kiv.ups.view.LoginController;
import cz.zcu.fav.kiv.ups.view.WindowManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by vrenclouff on 06.12.16.
 */
public class Application {

    private final Logger logger = LogManager.getLogger(Application.class);

    private static final Application INSTANCE = new Application();

    private String username;

    private Application(){}

    public static Application getInstance() {
        return INSTANCE;
    }

    public void start(Parameters params, Stage primaryStage) {

        WindowManager.init(primaryStage);
        initLogger(params.getVerbose(), params.isLog_console(), params.isLog_file());
        username = params.getUsername();

        logger.info("Application start.");

        ConcurrentLinkedQueue<RCVMessage> rcvQueue = new ConcurrentLinkedQueue<RCVMessage>();

        ConcurrentLinkedQueue<SNDMessage> sndQueue = new ConcurrentLinkedQueue<SNDMessage>();

        showLoginScreen(params.getUsername());

    }

    public void showLoginScreen(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMLTemplates.LOGIN);
            Parent parent = loader.load();

            Scene loginScene = new Scene(parent);
            LoginController loginController = loader.getController();
            loginController.setUsername(username);

            WindowManager.getInstance().setView(loginController, loginScene);

            if (StringUtils.isNotEmpty(username)) {
                loginController.login();
            }

        }catch (Exception e) {
            logger.error(e);
        }
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void initLogger(int loglevel, boolean console, boolean file) {
        try {
            String filePath = "logfile.log";
            PatternLayout layout = new PatternLayout("%d [%-5p] %m%n");

            if (console) {
                ConsoleAppender appender = new ConsoleAppender(layout);
                appender.activateOptions();
                Logger.getRootLogger().addAppender(appender);
            }

            if (file) {
                RollingFileAppender appender = new RollingFileAppender(layout, filePath);
                appender.setMaxFileSize("1MB");
                appender.activateOptions();
                Logger.getRootLogger().addAppender(appender);
            }

            LogManager.getRootLogger().setLevel(getLogLevel(loglevel));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Level getLogLevel(int level) {
        switch (level) {
            case 0:
                return Level.DEBUG;
            case 1:
                return Level.INFO;
            case 2:
                return Level.WARN;
            case 3:
                return Level.ERROR;
            default:
                return Level.INFO;
        }
    }
}