package cz.zcu.fav.kiv.ups.core;

/**
 * Created by Lukas Cerny on 15.12.16.
 */
public class GameSettings {

    private int layers;

    private int taking;

    public GameSettings(int layers, int taking) {
        this.layers = layers;
        this.taking = taking;
    }

    public int getLayers() {
        return layers;
    }

    public int getTaking() {
        return taking;
    }
}
