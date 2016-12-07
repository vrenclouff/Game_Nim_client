package cz.zcu.fav.kiv.ups.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


/**
 * Created by vrenclouff on 07.12.16.
 */
public abstract class BaseController {

    @FXML
    public Button moveBtn;

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

    protected abstract void setScene(ViewDTO data);

    protected abstract void nextScene(ViewDTO data);

    protected abstract void actualizeScene(ViewDTO data);

    protected abstract void showAlert(ViewDTO data);
}
