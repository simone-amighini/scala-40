package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

public class FirstPlayerChoicesEvent extends Event {
    private final boolean resumeGame;
    private final int numberOfPlayers;

    public FirstPlayerChoicesEvent(boolean resumeGame, int numberOfPlayers) {
        super("FIRST_PLAYER_CHOICES");
        this.resumeGame = resumeGame;
        this.numberOfPlayers = numberOfPlayers;
    }

    public boolean requestsGameResume() {
        return resumeGame;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    @Override
    public void callHandler() {
        ConnectionsManager.getInstance().handle(this);
    }
}
