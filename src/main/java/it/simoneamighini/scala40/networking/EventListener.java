package it.simoneamighini.scala40.networking;

import java.io.IOException;
import java.util.logging.Logger;

class EventListener implements Runnable {
    private final Endpoint endpoint;
    private final Connection listeningConnection;
    private volatile boolean stop;
    private final IncomingEventsQueue incomingEventsQueue;
    private final Logger logger;

    EventListener(Endpoint endpoint, Connection connection, IncomingEventsQueue incomingEventsQueue) {
        this.endpoint = endpoint;
        this.listeningConnection = connection;
        this.stop = false;
        this.incomingEventsQueue = incomingEventsQueue;
        this.logger = Logger.getLogger("EventListener on " + connection.getRemoteAddress());
    }

    @Override
    public void run() {
        Event receivedEvent;
        while (true) {
            try {
                receivedEvent = listeningConnection.receive();
                if (!(receivedEvent instanceof EchoEvent)) {
                    logger.info("Received " + receivedEvent + " from " + listeningConnection.getRemoteAddress());
                }
            } catch (IOException exception) {
                // connection error
                if (!stop) {
                    // unexpected disconnection
                    endpoint.signalConnectionProblem(listeningConnection, exception.getMessage());
                }
                return;
            } catch (ClassNotFoundException exception) {
                endpoint.signalConnectionProblem(listeningConnection, exception.getMessage());
                return;
            }

            receivedEvent.setRemoteAddress(listeningConnection.getRemoteAddress());
            if (!(receivedEvent instanceof EchoEvent)) {
                incomingEventsQueue.add(receivedEvent);
            }
        }
    }

    void stop() {
        stop = true;
    }
}
