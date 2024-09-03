package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.events.GameResumeEvent;
import it.simoneamighini.scala40.events.NewGameEvent;
import it.simoneamighini.scala40.events.WaitingRoomUpdateEvent;
import it.simoneamighini.scala40.networking.Client;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WaitingRoomController implements SceneController {
    @FXML
    private VBox displayNamesVBox;

    @FXML
    public void onExitButtonClick() {
        Client.getInstance().stop();
        SceneLoader.changeScene("menu.fxml");
    }

    @Override
    public void handle(WaitingRoomUpdateEvent event) {
        Platform.runLater(() -> displayNamesVBox.getChildren().clear());
        for (String username : event.getUsernames()) {
            Platform.runLater(
                    () -> {
                        Label label = new Label(username);
                        label.getStyleClass().add("normal-text");
                        displayNamesVBox.getChildren().add(label);
                    }
            );
        }
    }

    @Override
    public void handle(NewGameEvent event) {
        SceneLoader.changeScene("match.fxml");
    }

    @Override
    public void handle(GameResumeEvent event) {
        SceneLoader.changeScene("match.fxml");
    }
}
