package cz.zcu.fav.kiv.ups.view;

import java.net.URL;

public class FXMLTemplates {

    /** Slozka se sablonami */
    private static final String FOLDER = "/templates/";

    /** Login sablona */
    public static final URL LOGIN = FXMLTemplates.class.getResource(FOLDER+"login.fxml");

    /** Sablona uzivatelu */
    public static final URL EXPLORER = FXMLTemplates.class.getResource(FOLDER+"explorer.fxml");

    /** DEMO sablona */
    public static final URL DEMO = FXMLTemplates.class.getResource(FOLDER+"demo.fxml");

}