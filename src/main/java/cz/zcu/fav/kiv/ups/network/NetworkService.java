package cz.zcu.fav.kiv.ups.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Lukas Cerny on 12.12.16.
 */
public interface NetworkService {

    char STX = 02;

    char CHECKSUM = 138;

    char ETX = 03;

    void decrementPong();

    void incrementPong();

    void disconnect();

    boolean isNetworkAvailable();

    LinkedBlockingQueue<RCVMessage> getReceiveQueue();

    LinkedBlockingQueue<SNDMessage> getSenderQueue();
}
