package cz.zcu.fa.kiv.ups.core;

import org.apache.log4j.*;

import java.io.IOException;

/**
 * Created by vrenclouff on 06.12.16.
 */
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    private static final Application INSTANCE = new Application();

    private Application(){}

    public static Application getInstance() {
        return INSTANCE;
    }

    public void start(Parameters params) {
        initLogger(0, true, false);

        logger.info("Application start.");
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
}
