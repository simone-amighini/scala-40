package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class MenuController implements SceneController {
    @FXML
    public void onPlayButtonClick() {
        // TODO: missing scene
    }
    
    @FXML
    public void onSettingsButtonClick() {
        Platform.runLater(() -> SceneLoader.changeScene("fxml/settings.fxml"));
    }

    @FXML
    public void onExitButtonClick() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void handle(Event event) {}
}
