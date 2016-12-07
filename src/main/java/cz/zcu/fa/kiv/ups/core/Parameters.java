package cz.zcu.fa.kiv.ups.core;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.lang.StringUtils;

/**
 * Created by vrenclouff on 06.12.16.
 */
public class Parameters {


    @Parameter(names = { "-log", "-verbose" }, description = "Level of verbosity\n" +
            "\t\t0 - DEBUG\n" +
            "\t\t1 - INFO\n" +
            "\t\t2 - WARN\n" +
            "\t\t3 - ERROR")
    private Integer verbose = 1;

    @Parameter(names={"--address", "-a"}, description = "Define IP address or hostname to server")
    private String address;

    @Parameter(names={"--port", "-p"}, description = "Define port to server")
    private String port;

    @Parameter(names = {"--user", "-u"}, description = "Username for login to server")
    private String loginname;

    @Parameter(names = {"--help", "-h"}, description = "Print help")
    private boolean help;

    @Parameter(names = {"--console", "-c"}, description = "Print log messages to console")
    private boolean log_console = true;

    @Parameter(names = {"--file", "-f"}, description = "Print log messages to file")
    private boolean log_file = false;

    public Parameters(String [] args) {
        JCommander jCommander = new JCommander(this, args);
        jCommander.setProgramName("NimClient");
        if (isHelp()) {
            jCommander.usage();
            System.exit(0);
        }
    }

    public Integer getVerbose() {
        return verbose;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }

    public String getLoginname() {
        return loginname;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isLog_console() {
        return log_console;
    }

    public boolean isLog_file() {
        return log_file;
    }

    public boolean isAutoLogin() { return StringUtils.isNotEmpty(address) && StringUtils.isNotEmpty(port) && StringUtils.isNotEmpty(loginname); }
}
