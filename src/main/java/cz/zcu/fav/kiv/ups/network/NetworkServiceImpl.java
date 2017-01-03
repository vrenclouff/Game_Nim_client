package cz.zcu.fav.kiv.ups.network;

import cz.zcu.fav.kiv.ups.core.InternalMsg;
import cz.zcu.fav.kiv.ups.core.RouterService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by vrenclouff on 09.12.16.
 */
public class NetworkServiceImpl implements Network, NetworkService {

    private final Logger logger = LogManager.getLogger(NetworkServiceImpl.class);

    private final static int MAX_UNSEND_PACKETS = 5;

    private LinkedBlockingQueue<RCVMessage> rcvQueue;

    private LinkedBlockingQueue<SNDMessage> sndQueue;

    private String address;

    private Integer port;

    private ReceiveService rcvService;

    private SenderService sndService;

    private RouterService routerService;

    private Socket socket;

    private Timer timerWatch;

    private Timer timerPong;

    private boolean networkAvailable;

    private int unsendPackets;

    public NetworkServiceImpl(String address, Integer port) {
        this.address = address;
        this.port = port;
        this.rcvQueue = new LinkedBlockingQueue<>();
        this.sndQueue = new LinkedBlockingQueue<>();
        this.rcvService = new ReceiveService(this);
        this.sndService = new SenderService(this);
        this.routerService = new RouterService(rcvQueue, sndQueue);
        this.routerService.start();
    }

    private void run() {

        try {
            logger.debug("Creating network socket.");
            this.socket = new Socket(address, port);

            logger.debug("Initialize default properties.");
            this.networkAvailable = true;
            this.unsendPackets = 0;

            logger.debug("Settings socket streams.");
            this.rcvService.setStream(socket.getInputStream());
            this.sndService.setStream(socket.getOutputStream());
/*
            logger.debug("Start timer for watch network.");
            this.timerWatch = new Timer();
            this.timerWatch.schedule(new TimerTask() {public void run() {watchNetwork();}}, 1000, 2000);

            logger.debug("Start timer for pong.");
            this.timerPong = new Timer();
            this.timerPong.schedule(new TimerTask() {
                public void run() {
                    try {getSenderQueue().put(new SNDMessage(NetworkState.PONG, ""));}
                    catch (InterruptedException e) {e.printStackTrace();}
                }
            }, 1000, 3000);
*/
            logger.debug("Start receive thread.");
            this.rcvService.start();

            logger.debug("Start sender thread.");
            this.sndService.start();

        } catch (IOException e) {
            logger.warn("Server is not available.");
            try {
                getReceiveQueue().put(new RCVMessage(NetworkState.IN_APP_ALERT,
                        InternalMsg.SERVER_AVAILABLE, "Server je nedostupný."));
            } catch (InterruptedException e1) {
                logger.error("Cannot add message to rcvQueue.");
            }
        }
    }

    private void watchNetwork() {
        if (unsendPackets >= MAX_UNSEND_PACKETS) {
            logger.debug("Server is not available.");
            disconnect();
        }
    }

    @Override
    public void send(SNDMessage message) {
        try {
            logger.debug("Send message.");
            this.sndQueue.put(message);
            if (!networkAvailable) {run();}
        } catch (InterruptedException e) {
            logger.error("Cannot add message to rcvQueue.");
        }
    }

    @Override
    public void connect() {
        run();
    }

    @Override
    public void resetPong() {
        this.unsendPackets = 0;
    }

    @Override
    public void incrementPong() {
        this.unsendPackets++;
    }

    @Override
    public void disconnect() {
        try {
            this.networkAvailable = false;
            this.unsendPackets = 0;
            getReceiveQueue().put(new RCVMessage(NetworkState.IN_APP_ALERT,
                    InternalMsg.SERVER_AVAILABLE, "Server je nedostupný."));
            this.socket.close();
            this.rcvService.stop();
            this.sndService.stop();
            if (timerWatch != null) this.timerWatch.cancel();
            if (timerPong != null) this.timerPong.cancel();
        } catch (IOException | InterruptedException e) {
       //     logger.error("", e);
        }
    }

    @Override
    public LinkedBlockingQueue<RCVMessage> getReceiveQueue() {
        return this.rcvQueue;
    }

    @Override
    public LinkedBlockingQueue<SNDMessage> getSenderQueue() {
        return this.sndQueue;
    }
}


