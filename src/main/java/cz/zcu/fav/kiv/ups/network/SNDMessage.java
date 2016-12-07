package cz.zcu.fav.kiv.ups.network;

/**
 * Created by vrenclouff on 07.12.16.
 */
public class SNDMessage {

    private int socket;

    private NetworkState state;

    private String parameters;

    public SNDMessage(final int socket, final NetworkState state, final String parameters) {
        this.socket = socket;
        this.state = state;
        this.parameters = parameters;
    }

    public int getSocket() {
        return socket;
    }

    public NetworkState getState() {
        return state;
    }

    public String getParameters() {
        return parameters;
    }
}
