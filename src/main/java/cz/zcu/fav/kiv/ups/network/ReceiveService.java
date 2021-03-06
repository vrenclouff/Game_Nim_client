package cz.zcu.fav.kiv.ups.network;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lukas Cerny on 12.12.16.
 */
public class ReceiveService implements Runnable {

    private final Logger logger = LogManager.getLogger(ReceiveService.class);

    private NetworkService network;

    private DataInputStream stream;

    private Thread thread;

    public ReceiveService(NetworkService networkService) {
        this.network = networkService;
    }

    public void setStream(InputStream stream) {
        this.stream = new DataInputStream(stream);
    }

    @Override
    public void run() {
        try {
            while(true) {
                byte[] buffer = new byte[1024];
                int count = stream.read(buffer);
                if (count > 0) {
                    logger.debug("Row message: " + new String(buffer, 0, count));
                    List<RCVMessage> messageList = new ArrayList<>();
                    createValidatedMessage(messageList, buffer);
                    for(RCVMessage message : messageList) {
                        if (message.getState() == NetworkState.PONG) {
                            network.resetPong();
                        } else {
                            try {
                                logger.info("Validated message " + message.getState()+" "+Arrays.toString(message.getParameters()));
                                network.getReceiveQueue().put(message);
                            } catch (InterruptedException e) {e.printStackTrace();}
                        }
                    }
                } else {
                        network.disconnect(); break;
                }
            }
        } catch (IOException e) {}

        logger.debug("Thread ReceiveService ends.");
    }

    private void createValidatedMessage(List<RCVMessage> messageList, byte [] message) {

        logger.debug("Validation receive message: " + message);

        for(int i = 0; i < message.length; i++ ) {
            byte c = message[i];
            if (c == NetworkService.STX) {
                message = Arrays.copyOfRange(message, i, message.length); break;
            }
        }

        /* Kontrola minimalni delky zpravy */
        if (message.length < 2) {
            logger.debug("The message does not have correct length.");return;
        }

        /* Kontrola pocatecniho znacky zpravy */
        if (message[0] != NetworkService.STX) {
            logger.debug("The message does not have STX mark.");return;
        }

        char temp[] = new char[message.length];
        char c;
        int i;
        boolean start_end_mark = false;

        /* Kontrola konecne znacky zpravy a checksumy */
        for(i = 0; i < message.length-1; i++) {
            if ((c = (char)message[i+1]) == NetworkService.ETX) { start_end_mark = true; break; }
            temp[i] = c;
        }
        temp[i] = '\0';
        String validatedMessage = new String(temp).trim();

        if (!start_end_mark) {
            logger.debug("The message does not have ETX mark.");return;
        }

        String[] separatedPacket = validatedMessage.split(" ");
        String stateString = separatedPacket[0];
        NetworkState state = null;

        /* Kontrola, zda zprava obsahuje validni prikaz */
        for(NetworkState item : NetworkState.values()) {
            if (stateString.equalsIgnoreCase(item.toString())) {
                state = item; break;
            }
        }

        if (state == null) {
            logger.debug("The message does not have STATE."); return;
        }

        Object [] params = Arrays.asList(separatedPacket).subList(1, separatedPacket.length).toArray();
        logger.debug("The message is successful validated.");
        messageList.add(new RCVMessage(state, params));

        byte [] nextMsg = Arrays.copyOfRange(message, validatedMessage.length() + 1 + 1 + 1, message.length);
        createValidatedMessage(messageList, nextMsg);
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }
}
