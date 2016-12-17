package cz.zcu.fav.kiv.ups.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.fav.kiv.ups.network.Network;
import cz.zcu.fav.kiv.ups.network.NetworkState;
import cz.zcu.fav.kiv.ups.network.SNDMessage;
import cz.zcu.fav.kiv.ups.view.ExplorerController;
import cz.zcu.fav.kiv.ups.view.GameController;
import cz.zcu.fav.kiv.ups.view.ViewDTO;
import cz.zcu.fav.kiv.ups.view.WindowManager;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Lukas Cerny on 14.12.16.
 */
public class UserManager {

    private WindowManager windowManager;

    private BlockingQueue<SNDMessage> sndQueue;

    private ObjectMapper objectMapper;

    public UserManager(BlockingQueue<SNDMessage> sndQueue) {
        this.sndQueue = sndQueue;
        this.objectMapper = new ObjectMapper();
        this.windowManager = WindowManager.getInstance();
    }

    public void login(Object [] params) throws InterruptedException {
        String result = ((String) params[0]).trim();
        if (result.equalsIgnoreCase(Network.SUCCESS)) {
            windowManager.processView(new ViewDTO(ExplorerController.class, new Object[]{}));
            sndQueue.put(new SNDMessage(NetworkState.ALL_USERS, StringUtils.EMPTY));
            if (params.length > 1 && ((String)params[1]).trim().equalsIgnoreCase("GAME")) {
                windowManager.showAlert(InternalMsg.BACK,
                        "Do you want back to game?");
            }
        }else if (result.equalsIgnoreCase(Network.ERROR)) {
            windowManager.showAlert(InternalMsg.INFO, "User exists. Choose different username.");
        }
    }

    public void all_users(Object [] params) {
        String result = ((String) params[0]).trim();
        if (result.equalsIgnoreCase(Network.ERROR)) {return;}

        String [] users = {};
        try {
            Map<String, Object> map = objectMapper.readValue(result, new TypeReference<Map<String, Object>>(){});
            List<String> items = ((List<String>) map.get("users"));
            users = items.toArray(new String[items.size()]);
        } catch (IOException e) {e.printStackTrace();}
        windowManager.processView(new ViewDTO(ExplorerController.class, users));
    }

    public void logout(Object [] params) {
        String result = ((String) params[0]).trim();
        if (result.equalsIgnoreCase(Network.SUCCESS)) {
            windowManager.logout();
        }else if (result.equalsIgnoreCase(Network.ERROR)) {
            windowManager.showAlert(InternalMsg.INFO, "You can't logout now.");
        }
    }
}