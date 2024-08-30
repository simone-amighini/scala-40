package it.simoneamighini.scala40.servercontroller;

import it.simoneamighini.scala40.model.Game;

public class GameController {
    private static GameController instance;

    private Game game;

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    private GameController() {}

    public void setGame(Game game) {
        this.game = game;
    }
}
