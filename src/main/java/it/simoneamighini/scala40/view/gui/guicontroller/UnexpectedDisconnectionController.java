package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.fxml.FXML;

public class UnexpectedDisconnectionController implements SceneController {
    @FXML
    public void onExitButtonClick() {
        SceneLoader.changeScene("menu.fxml");
    }
}
