package it.simoneamighini.scala40.networking;

import java.io.IOException;
import java.util.TimerTask;

class EchoSender extends TimerTask {
    static final int ECHO_PERIOD = 1000;

    private final Connection connection;
    private final Endpoint endpoint;

    EchoSender(Endpoint endpoint, Connection connection) {
        this.connection = connection;
        this.endpoint = endpoint;
    }

    @Override
    public void run() {
        try {
            EchoEvent echoEvent = new EchoEvent();
            connection.send(new EchoEvent());
        } catch (IOException exception) {
            endpoint.signalConnectionProblem(connection, exception.getMessage());
        }
    }
}
