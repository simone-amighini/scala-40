package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Client;
import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;

import java.util.List;
import java.util.Map;

public class EndGameEvent extends Event {
    private final List<String> usernames;
    private final Map<String, Integer> usernamePointsMap;
    private final boolean gameEnded;

    public EndGameEvent(List<String> usernames, Map<String, Integer> usernamePointsMap, boolean gameEnded) {
        super("END GAME");
        this.usernames = usernames;
        this.usernamePointsMap = usernamePointsMap;
        this.gameEnded = gameEnded;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public Map<String, Integer> getUsernamePointsMap() {
        return usernamePointsMap;
    }

    @Override
    public void callHandler() {
        Client.getInstance().stop();
        Data.getInstance().setFinalRanking(usernames);
        Data.getInstance().setUsernamePointsMap(usernamePointsMap);
        Data.getInstance().setGameEnded(gameEnded);
        SceneLoader.getCurrentController().handle(this);
    }
}
