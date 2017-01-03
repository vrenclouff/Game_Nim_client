package cz.zcu.fav.kiv.ups.network;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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
                    RCVMessage message = createValidatedMessage(new String(buffer, 0, count));
                    if (message != null) {
                        if (message.getState() == NetworkState.PONG) {
                            network.resetPong();
                        } else {
                            try {
                                logger.info("Receive message " + buffer);
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

    private RCVMessage createValidatedMessage(String message) {

        logger.debug("Validation receive message: " + message);

        /* Kontrola minimalni delky zpravy */
        if (message.length() < 3)
        {
            logger.debug("The message does not have correct length.");
            return null;
        }

        /* Kontrola pocatecniho znacky zpravy */
        if (message.charAt(1) != NetworkService.STX)
        {
            logger.debug("The message does not have STX mark.");
            return null;
        }

        char temp[] = new char[message.length()];
        char c;
        int checksum_res = 0;
        boolean start_end_mark = false;
        int checksum_init = message.charAt(0);
        long checksum_temp = 0;

        /* Kontrola konecne znacky zpravy a checksumy */
        for(int i = 0; i < message.length()-2; i++)
        {
            if ((c = message.charAt(i+2)) == NetworkService.ETX) { start_end_mark = true; break; }
            checksum_temp += c;
            temp[i] = c;
        }

        if (checksum_init != (checksum_temp % NetworkService.CHECKSUM))
        {
            logger.debug("The message does not valid by checksum.");
            return null;
        }

        if (!start_end_mark)
        {
            logger.debug("The message does not have ETX mark.");
            return null;
        }

        String[] separatedPacket = new String(temp).split(" ");
        String stateString = separatedPacket[0];
        NetworkState state = null;

        /* Kontrola, zda zprava obsahuje validni prikaz */
        for(int i = 0; i < NetworkState.values().length; i++) {
            if (stateString.equalsIgnoreCase(NetworkState.values()[i].toString())) {
                state = NetworkState.values()[i]; break;
            }
        }

        if (state == null)
        {
            logger.debug("The message does not have STATE.");
            return null;
        }

        Object [] params = Arrays.asList(separatedPacket).subList(1, separatedPacket.length).toArray();
        logger.debug("The message is successful validated.");
        return new RCVMessage(state, params);
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }
}
