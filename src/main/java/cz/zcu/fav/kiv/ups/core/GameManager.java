package cz.zcu.fav.kiv.ups.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        String userRecipient = params[0].toString();
        windowManager.showAlert(InternalMsg.INVITE,
                "Player "+userRecipient+" has you invited to game. Do you want accept this?",
                userRecipient);
    }

    public void join(Object [] params) {
        String [] data = Arrays.asList(params).toArray(new String[params.length]);
        windowManager.processView(new ViewDTO(GameController.class, data));
    }

    public void alert(Object [] params) {
        InternalMsg state = (InternalMsg) params[0];
        List<Object> tmp = Arrays.asList(params).subList(1, params.length);
        String [] content = tmp.toArray(new String[tmp.size()]);
        windowManager.showAlert(state, content);

    }

    public void settings(Object [] params) {

        String json = (String) params[0];

        Integer layers = 0;
        Integer taking = 0;
        try {
            Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
            layers = (Integer) map.get("layers");
            taking = (Integer) map.get("taking");
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameSettings settings = new GameSettings(layers, taking);
        Application.getInstance().setSettings(settings);
    }
}
