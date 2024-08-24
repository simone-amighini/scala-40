package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;

public class GameEnterResponseEvent extends Event {
    public enum Response {
        ACCEPTED,
        USERNAME_REFUSED,
        NOT_A_SAVED_GAME_PLAYER,
        WAIT,
        REFUSED
    }

    private final Response response;

    public GameEnterResponseEvent(Response response) {
        super("GAME_ENTER_RESPONSE");
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public void callHandler() {}
}
