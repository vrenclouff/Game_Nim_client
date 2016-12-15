package cz.zcu.fav.kiv.ups.network;

/**
 * Created by vrenclouff on 07.12.16.
 */
public class SNDMessage {

    private NetworkState state;

    private String parameters;

    public SNDMessage(final NetworkState state, final String parameters) {
        this.state = state;
        this.parameters = parameters;
    }

    public NetworkState getState() {
        return state;
    }

    public String getParameters() {
        return parameters;
    }
}
