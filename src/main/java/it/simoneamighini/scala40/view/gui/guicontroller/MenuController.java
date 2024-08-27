package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class MenuController implements SceneController {
    @FXML
    public void onPlayButtonClick() {
        SceneLoader.changeScene("startGame.fxml");
    }
    
    @FXML
    public void onSettingsButtonClick() {
        SceneLoader.changeScene("settings.fxml");
    }

    @FXML
    public void onExitButtonClick() {
        Platform.exit();
        System.exit(0);
    }
}
