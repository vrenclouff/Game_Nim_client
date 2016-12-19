package cz.zcu.fav.kiv.ups.view.components;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Created by Lukas Cerny on 19.12.16.
 */
public class ViewUtils {

    private static String [] matches = new String[]{
            "1",
            "1,3",
            "1,3,5",
            "1,3,5,7",
            "1,3,5,7,9",
            "1,3,5,7,9,11"
    };

    public static String matchesInLayers(String layers) {
        String indexStr = layers.trim();
        if (NumberUtils.isNumber(indexStr)) {
            int index = Integer.valueOf(indexStr) - 1;
            return matches[index];
        }
        return "0";
    }
}
