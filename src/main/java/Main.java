
import cz.zcu.fav.kiv.ups.core.Application;
import javafx.stage.Stage;
import org.apache.log4j.*;

import java.io.IOException;
import java.util.Arrays;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) {

        cz.zcu.fav.kiv.ups.core.Parameters params = new cz.zcu.fav.kiv.ups.core.Parameters(Arrays.copyOf(
                getParameters().getRaw().toArray(), getParameters().getRaw().size(), String[].class));

        initLogger(params.getVerbose(), params.isLog_console(), params.isLog_file());
        Application.getInstance().start(params, primaryStage);
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

    public static void main(String[] args) {
        launch(args);
    }
}

