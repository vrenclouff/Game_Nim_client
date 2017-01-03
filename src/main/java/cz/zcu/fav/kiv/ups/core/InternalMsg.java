package cz.zcu.fav.kiv.ups.core;

/**
 * Created by vrenclouff on 10.12.16.
 */
public enum InternalMsg {
    SERVER_AVAILABLE("Server není dostupný"),
    INFO("Informace"),
    INVITE("Pozvání do hry"),
    GAME_DISCONNECT("Odpojen ze hry"),
    BACK("Zpět do hry"),
    GAME_END("Konec hry"),
    FINISH("Konec hry"),

    SWITCH_USER("SWITCH_USER"),
    START("START"),
    TAKE("TAKE"),
    STATE("STATE"),

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
