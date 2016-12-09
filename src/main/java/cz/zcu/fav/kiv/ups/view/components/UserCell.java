package cz.zcu.fav.kiv.ups.view.components;

import cz.zcu.fav.kiv.ups.view.ExplorerController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * Created by vrenclouff on 08.12.16.
 */
public class UserCell extends ListCell<CellButton> {
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button("<>");
    CellButton lastItem;

    public UserCell(ExplorerController delegate) {
        super();

        label.setTextFill(Color.WHITE);

        button.setOnAction(event -> delegate.listViewDidPlayRow());
        button.setMinSize(5, 5);
        button.setPrefSize(25, 25);

        String image = "/images/play.png";
        button.setStyle("-fx-background-image: url('" + image + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;" +
                "-fx-background-size: cover;" +
                "-fx-background-color: transparent"
        );

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(CellButton item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            button.setVisible(item != null ? item.visibleButton : false);
            lastItem = item;
            label.setText(item != null ? item.text : "<null>");
            setGraphic(hbox);
        }
    }
}