package cz.zcu.fav.kiv.ups.core;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

/**
 * Created by vrenclouff on 06.12.16.
 */
public class Parameters {


    @Parameter(names = { "--verbose", "-l" }, description = "Level of verbosity\n" +
            "\t\t0 - DEBUG\n" +
            "\t\t1 - INFO\n" +
            "\t\t2 - WARN\n" +
            "\t\t3 - ERROR")
    private Integer verbose = 1;

    @Parameter(names={"--address", "-a"}, required = true, description = "Adresa serveru")
    private String address;

    @Parameter(names={"--port", "-p"}, required = true, description = "Port serveru")
    private Integer port;

    @Parameter(names = {"--user", "-u"}, description = "Uživatelské jméno pro přihlášení")
    private String username;

    @Parameter(names = {"--help", "-h"}, help = true, description = "Nápověda")
    private boolean help;

    @Parameter(names = {"--console", "-c"}, description = "Povolení výpisu logu do konzole")
    private boolean log_console = false;

    @Parameter(names = {"--file", "-f"}, description = "Povolení výpisu logu do souboru")
    private boolean log_file = false;

    public Parameters(String [] args) {

        JCommander commander = new JCommander(this);
        commander.setProgramName("NimClient -a <server_address> -p <server_port>");

        try {
            commander.parse(args);
        }catch (ParameterException e){
            commander.usage();
            System.exit(0);
        }

        if (isHelp()) {
            commander.usage();
            System.exit(0);
        }
    }

    public Integer getVerbose() {
        return verbose;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
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

}
