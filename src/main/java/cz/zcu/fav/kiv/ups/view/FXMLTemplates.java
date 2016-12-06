package cz.zcu.fav.kiv.ups.view;

import java.net.URL;

public class FXMLTemplates {

    /** Slozka se sablonami */
    private static final String FOLDER = "/templates/";

    /** Index sablona */
    public static final URL INDEX = FXMLTemplates.class.getResource(FOLDER+"root.fxml");

    /** DEMO sablona */
    public static final URL DEMO = FXMLTemplates.class.getResource(FOLDER+"demo.fxml");

}