package cz.zcu.fav.kiv.ups.network;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

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
                    if (message.getState() == NetworkState.PONG) network.incrementPong();
                    String msg = createValidatedMessage(message);
                    logger.debug("Sending message: "+msg);
                    stream.write(msg.getBytes());
                    stream.flush();
                } catch (InterruptedException e) {break;}
            }
        } catch (IOException e) {
            logger.error("SenderService::IOException", e);
        }
        logger.debug("Thread SenderService ends.");
    }

    private String createValidatedMessage(SNDMessage message) {

        if (message == null) {
            return "";
        }

        String data = (message.getState() + " " + message.getParameters()).trim();
        byte [] res = new byte[data.length()+3];
        res[1] = NetworkService.STX;
        int i;
        byte checksum_res = 0;
        byte temp;
        int sum_temp;
        for(i=2;i<data.length()+2;i++) {
            temp = data.getBytes()[i-2];
            res[i] = temp;
            sum_temp = checksum_res + temp;
            checksum_res = (byte)(sum_temp % NetworkService.CHECKSUM);
        }
        res[0] = checksum_res;
        res[i] = NetworkService.ETX;

        return new String(res);
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }}