package it.simoneamighini.scala40.view;

import it.simoneamighini.scala40.events.PlannedDisconnectionEvent;

public class Data {
    private static Data instance;
    public enum GameStartType {
        NEW_GAME,
        RESUMED_GAME,
    }

    private String username;
    private boolean existsSavedGame;
    private GameStartType gameStartType;
    private PlannedDisconnectionEvent.Cause disconnectionCause;

    private Data() {}

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void reset() {
        instance = null;
    }

    public boolean existsSavedGame() {
        return existsSavedGame;
    }

    public void setExistsSavedGame(boolean existsSavedGame) {
        this.existsSavedGame = existsSavedGame;
    }

    public GameStartType getGameStartType() {
        return gameStartType;
    }

    public void setGameStartType(GameStartType gameStartType) {
        this.gameStartType = gameStartType;
    }

    public PlannedDisconnectionEvent.Cause getDisconnectionCause() {
        return disconnectionCause;
    }

    public void setDisconnectionCause(PlannedDisconnectionEvent.Cause disconnectionCause) {
        this.disconnectionCause = disconnectionCause;
    }
}
