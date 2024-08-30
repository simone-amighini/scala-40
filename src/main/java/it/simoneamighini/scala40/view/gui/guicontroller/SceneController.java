package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.events.*;
import it.simoneamighini.scala40.networking.Client;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public interface SceneController {
    default void criticalErrorManagement() {
        SceneLoader.changeScene("criticalError.fxml");
        Client.getInstance().stop();
    }

    default void handle(FirstPlayerResponseEvent event) {
        criticalErrorManagement();
    }

    default void handle(GameEnterResponseEvent event) {
        criticalErrorManagement();
    }

    default void handle(PlannedDisconnectionEvent event) {
        Client.getInstance().stop();
        Data.getInstance().setDisconnectionCause(event.getCause());
        SceneLoader.changeScene("plannedDisconnection.fxml");
    }

    default void handle(WaitingRoomUpdateEvent event) {
        criticalErrorManagement();
    }

    default void handle(NewGameEvent event) {
        criticalErrorManagement();
    }

    default void handle(GameResumeEvent event) {
        criticalErrorManagement();
    }
}
