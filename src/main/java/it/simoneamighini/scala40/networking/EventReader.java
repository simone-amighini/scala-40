package it.simoneamighini.scala40.networking;

public class EventReader implements Runnable {
    private final Endpoint endpoint;

    public EventReader(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void run() {
        while (true) {
            Event incomingEvent = endpoint.getIncomingEventsQueue().poll();
            try {
                incomingEvent.callHandler();
            } catch (NullPointerException exception) {
                // it happens only if the thread is interrupted
                return;
            }
        }
    }
}
