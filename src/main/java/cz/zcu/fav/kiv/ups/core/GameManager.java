package cz.zcu.fav.kiv.ups.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.fav.kiv.ups.network.Network;
import cz.zcu.fav.kiv.ups.view.GameController;
import cz.zcu.fav.kiv.ups.view.ViewDTO;
import cz.zcu.fav.kiv.ups.view.WindowManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Lukas Cerny on 14.12.16.
 */
public class GameManager {

    private WindowManager windowManager;

    private ObjectMapper objectMapper;


    public GameManager() {
        this.objectMapper = new ObjectMapper();
        this.windowManager = WindowManager.getInstance();
    }


    public void invite(Object [] params) {
        String userRecipient = ((String)params[0]).trim();
        windowManager.showAlert(InternalMsg.INVITE,
                "Player "+userRecipient+" has you invited to game. Do you want accept this?",
                userRecipient);
    }

    public void join(Object [] params) {
        Object [] data = new Object[params.length + 1];
        data[0] = InternalMsg.START;
        for(int i=1;i<params.length+1;i++){data[i] = params[i-1];}
        windowManager.processView(new ViewDTO(GameController.class, data));
    }

    public void alert(Object [] params) {
        InternalMsg state = (InternalMsg) params[0];
        List<Object> tmp = Arrays.asList(params).subList(1, params.length);
        String [] content = tmp.toArray(new String[tmp.size()]);
        windowManager.showAlert(state, content);
    }

    public void settings(Object [] params) {
        String json = ((String) params[0]).trim();
        Integer layers = 0;
        Integer taking = 0;
        try {
            Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
            layers = (Integer) map.get("layers");
            taking = (Integer) map.get("taking");
        } catch (IOException e) {e.printStackTrace();}
        Application.getInstance().setSettings(new GameSettings(layers, taking));
    }

    public void take(Object [] params) {
        Object [] data = new Object[params.length + 1];
        data[0] = InternalMsg.TAKE;
        for(int i=1;i<params.length+1;i++){data[i] = params[i-1];}
        windowManager.processView(new ViewDTO(GameController.class, data));
    }

    public void switch_user(Object [] params) {
        Object [] data = new Object[params.length + 1];
        data[0] = InternalMsg.SWITCH_USER;
        for(int i=1;i<params.length+1;i++){data[i] = params[i-1];}
        windowManager.processView(new ViewDTO(GameController.class, data));
    }

    public void disconnect(Object [] params) {
        windowManager.showAlert(InternalMsg.GAME_DISCONNECT,
                "Second player was disconnected. Do you want exit the game?");
    }

    public void back(Object [] params) {
        windowManager.showAlert(InternalMsg.BACK,
                "Do you want back to game?");
    }

    public void end(Object [] params) throws InterruptedException {
        String result = ((String) params[0]).trim();
        if (result.equalsIgnoreCase(Network.SUCCESS)) {
            windowManager.showAlert(InternalMsg.GAME_END, "The game was exited.");
        }else if (result.equalsIgnoreCase(Network.ERROR)) {
            windowManager.showAlert(InternalMsg.INFO, "The game cannot be exited.");
        }
    }

    public void finish(Object [] params) {
        String result = ((String) params[0]).trim();
        if (result.equalsIgnoreCase("WON")){
            windowManager.showAlert(InternalMsg.FINISH, "You WON!!");
        }else if (result.equalsIgnoreCase("LOST")) {
            windowManager.showAlert(InternalMsg.FINISH, "You LOST!!");
        }
    }

    public void game_continue(Object [] params) {
        String result = ((String) params[0]).trim();
        if (result.equalsIgnoreCase("START")) {
            Object [] data = new Object[]{InternalMsg.START, params[1]};
            windowManager.processView(new ViewDTO(GameController.class, data));
        }
    }

    public void state(Object [] params) {
        Object [] data = new Object[4];
        data[0] = InternalMsg.STATE;
        data[1] = ((String) params[0]).trim();
        data[2] = ((String) params[1]).trim();
        data[3] = ((String) params[2]).trim();
        windowManager.processView(new ViewDTO(GameController.class, data));
    }
}