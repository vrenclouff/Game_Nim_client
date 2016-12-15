package delete;


import delete.CommObserver;
import javafx.application.Platform;
import javafx.scene.control.Label;


public class UiCommObserver implements CommObserver {

    private Label m_label;

    public void processData(final String data) {
        Platform.runLater(new Runnable() {
            public void run() {
                m_label.setText(data);
            }
        });

    }

    public void setM_label(Label textArea) {
        this.m_label = textArea;
    }
}
