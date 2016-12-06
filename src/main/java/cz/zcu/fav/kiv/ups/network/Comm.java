package cz.zcu.fav.kiv.ups.network;


public interface Comm {
    public void send(String data);
    public void registerObserver(CommObserver observer);
}
