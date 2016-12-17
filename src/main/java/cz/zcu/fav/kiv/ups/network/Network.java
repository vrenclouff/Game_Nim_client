package cz.zcu.fav.kiv.ups.network;

/**
 * Created by vrenclouff on 10.12.16.
 */
public interface Network {

    String SUCCESS = "SUCCESS";

    String ERROR = "ERROR";

    String ACCEPT = "ACCEPT";

    String IGNORE = "IGNORE";

    void send(SNDMessage message);

    void connect();
}
