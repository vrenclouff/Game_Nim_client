package cz.zcu.fav.kiv.ups.view.components;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vrenclouff on 09.12.16.
 */
public class NodeUtils {

    public static <T extends Pane> List<Node> paneNodesByClass(T parent, Class[] classes) {
        return paneNodesByClass(parent, Arrays.asList(classes),  new ArrayList<Node>());
    }

    public static <T extends Pane> List<Node> paneNodes(T parent) {
        return paneNodes(parent, new ArrayList<Node>());
    }

    private static <T extends Pane> List<Node> paneNodes(T parent, List<Node> nodes) {
        for (Node node : parent.getChildren()) {
            if (node instanceof Pane) {
                paneNodes((Pane) node, nodes);
            } else {
                nodes.add(node);
            }
        }
        return nodes;
    }

    private static <T extends Pane> List<Node> paneNodesByClass(T parent, List<Class> classes, List<Node> nodes) {
        for (Node node : parent.getChildren()) {
            if (classes.contains(node.getClass())) {
                nodes.add(node);
            }else if (node instanceof Pane) {
                paneNodesByClass((Pane) node,classes, nodes);
            }
        }
        return nodes;
    }
}
