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
                    List<RCVMessage> messageList = new ArrayList<>();
                    createValidatedMessage(messageList, new String(buffer, 0, count));
                    for(RCVMessage message : messageList) {
                        if (message.getState() == NetworkState.PONG) {
                            network.resetPong();
                        } else {
                            try {
                                logger.info("Receive message " + message.getState()+" "+Arrays.toString(message.getParameters()));
                                network.getReceiveQueue().put(message);
                            } catch (InterruptedException e) {e.printStackTrace();}
                        }
                    }
                } else {
                        logger.error("ReceiveService empty message.");
                        network.disconnect();
                        break;
                }
            }
        } catch (IOException e) {}

        logger.debug("Thread ReceiveService ends.");
    }

    private void createValidatedMessage(List<RCVMessage> messageList, String message) {

        logger.debug("Validation receive message: " + message);

        /* Kontrola minimalni delky zpravy */
        if (message.length() < 3) {
            logger.debug("The message does not have correct length.");return;
        }

        /* Kontrola pocatecniho znacky zpravy */
        if (message.charAt(1) != NetworkService.STX) {
            logger.debug("The message does not have STX mark.");return;
        }

        char temp[] = new char[message.length()];
        char c;
        int i;
        int checksum_res = 0;
        boolean start_end_mark = false;
        int checksum_init = message.charAt(0);
        long checksum_temp = 0;

        /* Kontrola konecne znacky zpravy a checksumy */
        for(i = 0; i < message.length()-2; i++) {
            if ((c = message.charAt(i+2)) == NetworkService.ETX) { start_end_mark = true; break; }
            checksum_temp += c;
            temp[i] = c;
        }
        temp[i] = '\0';
        String validatedMessage = new String(temp).trim();

        if (checksum_init != (checksum_temp % NetworkService.CHECKSUM)) {
            logger.debug("The message does not valid by checksum.");return;
        }

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

        String nextMsg = message.substring(validatedMessage.length() + 1 + 1 + 1);
        if (nextMsg.equals(StringUtils.EMPTY)) return;
        else createValidatedMessage(messageList, nextMsg);
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }
}
