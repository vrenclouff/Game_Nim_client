package cz.zcu.fav.kiv.ups.network;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Lukas Cerny on 12.12.16.
 */
public class SenderService implements Runnable {

    private final Logger logger = LogManager.getLogger(SenderService.class);

    private NetworkService network;

    private OutputStream stream;

    private Thread thread;

    public SenderService(NetworkService networkService) {
        this.network = networkService;
    }

    public void setStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        try {
            while(true) {
                try {
                    logger.debug("Waiting for message for send.");
                    SNDMessage message = network.getSenderQueue().take();
                    String msg = createValidatedMessage(message);
                    if (message.getState() == NetworkState.PONG) {
                        network.incrementPong();
                    }else {
                        logger.info("Sending message: "+message.getState()+" "+message.getParameters());
                    }
                    stream.write(msg.getBytes());
                    stream.flush();
                } catch (InterruptedException e) {break;}
            }
        } catch (IOException e) {
            network.disconnect();
        }
        logger.debug("Thread SenderService ends.");
    }

    private String createValidatedMessage(SNDMessage message) {

        if (message == null) {
            return StringUtils.EMPTY;
        }

        String data = (message.getState() + " " + message.getParameters()).trim();
        char [] res = new char[data.length()+3];
        int i;
        char temp;

        for(i=1;i<data.length()+1;i++) {
            temp = (char) data.getBytes()[i-1];
            res[i] = temp;
        }
        res[0] = NetworkService.STX;
        res[i] = NetworkService.ETX;
        res[i+1] = '\0';

        return new String(res);
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }}