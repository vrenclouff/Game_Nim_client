package cz.zcu.fav.kiv.ups.core;

/**
 * Created by vrenclouff on 10.12.16.
 */
public enum InternalMsg {
    SERVER_AVAILABLE("Server is not available"),
    INFO("Info"),
    INVITE("Invite to game"),
    ;

    private final String name;

    InternalMsg(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
