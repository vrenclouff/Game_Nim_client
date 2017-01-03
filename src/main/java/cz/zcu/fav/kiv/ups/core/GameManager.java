package cz.zcu.fav.kiv.ups.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.fav.kiv.ups.network.Network;
import cz.zcu.fav.kiv.ups.view.GameController;
import cz.zcu.fav.kiv.ups.view.ViewDTO;
import cz.zcu.fav.kiv.ups.view.WindowManager;
import cz.zcu.fav.kiv.ups.view.components.ViewUtils;

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
        String result = ((String)params[0]).trim();
        if (!result.equalsIgnoreCase(Network.ERROR)) {
            windowManager.showAlert(InternalMsg.INVITE,
                    "Chcete přijmout požadavek do hry od hráče " + result + "?",
                    result);
        }
    }

    public void join(Object [] params) {
        String result = ((String)params[0]).trim();
        if (!result.equalsIgnoreCase(Network.ERROR)) {
            Object[] data = new Object[4];
            data[0] = InternalMsg.STATE;
            data[1] = result;
            data[2] = ViewUtils.matchesInLayers(((String) params[1]).trim());
            data[3] = ((String) params[2]).trim();
            windowManager.processView(new ViewDTO(GameController.class, data));
        }
    }

    public void alert(Object [] params) {
        InternalMsg state = (InternalMsg) params[0];
        List<Object> tmp = Arrays.asList(params).subList(1, params.length);
        String [] content = tmp.toArray(new String[tmp.size()]);
        windowManager.showAlert(state, content);
    }

    public void settings(Object [] params) {
        String result = ((String) params[0]).trim();
        if (!result.equalsIgnoreCase(Network.ERROR)) {
            Integer layers = 0;
            Integer taking = 0;
            try {
                Map<String, Object> map = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
                });
                layers = (Integer) map.get("layers");
                taking = (Integer) map.get("taking");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Application.getInstance().setSettings(new GameSettings(layers, taking));
        }
    }

    public void take(Object [] params) {
        String result = ((String)params[0]).trim();
        if (!result.equalsIgnoreCase(Network.ERROR)) {
            Object [] data = new Object[params.length + 1];
            data[0] = InternalMsg.TAKE;
            for(int i=1;i<params.length+1;i++){data[i] = params[i-1];}
            windowManager.processView(new ViewDTO(GameController.class,data));
        }
    }

    public void switch_user(Object [] params) {
        String result = ((String)params[0]).trim();
        if (!result.equalsIgnoreCase(Network.ERROR)) {
            Object[] data = new Object[params.length + 1];
            data[0] = InternalMsg.SWITCH_USER;
            for (int i = 1; i < params.length + 1; i++) {data[i] = params[i - 1];}
            windowManager.processView(new ViewDTO(GameController.class, data));
        }
    }

    public void disconnect(Object [] params) {
        windowManager.showAlert(InternalMsg.GAME_DISCONNECT,
                "Druhý hráč se odpojil. Pokračovat ve hře?");
    }

    public void back(Object [] params) {
        windowManager.showAlert(InternalMsg.BACK,
                "Chcete se vrátit do hry?");
    }

    public void end(Object [] params) throws InterruptedException {
        String result = ((String) params[0]).trim();
        if (result.equalsIgnoreCase(Network.SUCCESS)) {
            windowManager.showAlert(InternalMsg.GAME_END, "Hra byla ukončena.");
        }else if (result.equalsIgnoreCase(Network.ERROR)) {
            windowManager.showAlert(InternalMsg.INFO, "Hra nemůže být ukončena.");
        }
    }

    public void finish(Object [] params) {
        String result = ((String) params[0]).trim();
        if (!result.equalsIgnoreCase(Network.ERROR)) {
            windowManager.showAlert(InternalMsg.FINISH, result);
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
        String result = ((String) params[0]).trim();
        if (!result.equalsIgnoreCase(Network.ERROR)) {
            Object[] data = new Object[4];
            data[0] = InternalMsg.STATE;
            data[1] = result;
            data[2] = ((String) params[1]).trim();
            data[3] = ((String) params[2]).trim();
            windowManager.processView(new ViewDTO(GameController.class, data));
        }
    }
}