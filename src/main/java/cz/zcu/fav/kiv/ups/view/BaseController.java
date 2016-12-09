package cz.zcu.fav.kiv.ups.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;


/**
 * Created by vrenclouff on 07.12.16.
 */
public abstract class BaseController {


    @FXML
    protected Pane content;

    @FXML
    protected Button moveBtn;

    @FXML
    protected ProgressIndicator loadingWheel;

    @FXML
     protected Button loadingStopWheel;

    @FXML
    private void close() {
        WindowManager.getInstance().closeWindow();
    }

    @FXML
    private void minimize() {
        WindowManager.getInstance().minimizeWindow();
    }

    @FXML
    private void logout() { WindowManager.getInstance().logout();}

    protected void startLoadingWheel() {
        this.loadingWheel.setVisible(true);
        this.loadingStopWheel.setVisible(true);
        this.loadingStopWheel.setOnAction(e -> didStopLoadingWheel());
        this.content.setOpacity(0.5);
    }

    protected void stopLoadingWheel() {
        this.loadingWheel.setVisible(false);
        this.loadingStopWheel.setVisible(false);
        this.content.setOpacity(1.0);
    }

    protected abstract void didStopLoadingWheel();

    protected void setScene(ViewDTO data) {
        assert (data != null);

        if (data.getaClass() == this.getClass()) {
            this.actualizeScene(data);
        }else {
            this.nextScene(data);
        }
    }

    protected void actualizeScene(ViewDTO data) {
        assert (data != null && this.getClass() == data.getaClass());
        this.processData(data.getObjects());
    }

    protected abstract void nextScene(ViewDTO data);

    protected abstract void showAlert(ViewDTO data);

    protected abstract void processData(Object[] data);
}
