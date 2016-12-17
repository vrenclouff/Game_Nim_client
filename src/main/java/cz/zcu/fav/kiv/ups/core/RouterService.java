package cz.zcu.fav.kiv.ups.core;

import cz.zcu.fav.kiv.ups.network.RCVMessage;
import cz.zcu.fav.kiv.ups.network.SNDMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Lukas Cerny on 09.12.16.
 */
public class RouterService implements Runnable {

    private final Logger logger = LogManager.getLogger(RouterService.class);

    private BlockingQueue<RCVMessage> rcvQueue;

    private UserManager userManager;

    private GameManager gameManager;

    public RouterService(BlockingQueue<RCVMessage> rcvQueue, BlockingQueue<SNDMessage> sndQueue) {
        this.rcvQueue = rcvQueue;
        this.userManager = new UserManager(sndQueue);
        this.gameManager = new GameManager();
    }

    @Override
    public void run() {
        while(true) {
            try {
                logger.debug("Waiting for receive message.");
                RCVMessage message = rcvQueue.take();
                logger.debug("Receive message with state: " + message.getState());
                Object [] params = message.getParameters();

                switch (message.getState()) {
                    case LOGIN:             {   userManager.login(params);      }   break;
                    case ALL_USERS:         {   userManager.all_users(params);  }   break;
                    case LOGOUT:            {   userManager.logout(params);     }   break;
                    case GAME_INVITE:       {   gameManager.invite(params);     }   break;
                    case GAME_JOIN:         {   gameManager.join(params);       }   break;
                    case GAME_SETTINGS:     {   gameManager.settings(params);   }   break;
                    case GAME_TAKE:         {   gameManager.take(params);       }   break;
                    case GAME_SWITCH_USER:  {   gameManager.switch_user(params);}   break;
                    case GAME_DISCONNECT:   {   gameManager.disconnect(params); }   break;
                    case GAME_BACK:         {   gameManager.back(params);       }   break;
                    case GAME_END:          {   gameManager.end(params);        }   break;
                    case GAME_FINISH:       {   gameManager.finish(params);     }   break;
                    case GAME_CONTINUE:     { gameManager.game_continue(params);}   break;
                    case GAME_STATE:        {   gameManager.state(params);      }   break;
                    case IN_APP_ALERT:      {   gameManager.alert(params);      }   break;
                }
            } catch (InterruptedException e) {
                logger.error("RouterService::run()", e);
            }
        }
    }

    public void start() {
        (new Thread(this)).start();
    }
}
