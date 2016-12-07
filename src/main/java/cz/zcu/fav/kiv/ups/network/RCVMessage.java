package cz.zcu.fav.kiv.ups.network;

import java.util.List;

/**
 * Created by vrenclouff on 07.12.16.
 */
public class RCVMessage {

    private NetworkState state;

    private int socket;

    private List<String> parameters;

    public RCVMessage(final int socket, final NetworkState state, final List<String> parameters) {
        this.socket = socket;
        this.state = state;
        this.parameters = parameters;
    }

    public NetworkState getState() {
        return state;
    }

    public int getSocket() {
        return socket;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
