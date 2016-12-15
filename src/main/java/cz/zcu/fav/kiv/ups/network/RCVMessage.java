package cz.zcu.fav.kiv.ups.network;


/**
 * Created by vrenclouff on 07.12.16.
 */
public class RCVMessage {

    private NetworkState state;

    private Object[] parameters;

    public RCVMessage(final NetworkState state, Object... parameters) {
        this.state = state;
        this.parameters = parameters;
    }

    public NetworkState getState() {
        return state;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
