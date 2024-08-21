package it.simoneamighini.scala40.networking;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

class NetworkListener implements Runnable {
    private final Server server;
    private boolean stop;
    private final Logger logger;

    NetworkListener(Server server) {
        this.server = server;
        this.stop = false;
        this.logger = Logger.getLogger("NetworkListener (server)");
    }

    @Override
    public void run() {
        IncomingEventsQueue incomingEventsQueue = server.getIncomingEventsQueue();
        Socket clientSocket = null;
        Connection clientConnection;
        Thread clientDedicatedThread;

        while (true) {
            try {
                clientSocket = server.getWelcomeSocket().accept();
                logger.info("New client " + clientSocket.getInetAddress().getHostAddress() + ":" +
                        clientSocket.getPort() + " connected");
            } catch (IOException exception) {
                if (stop) {
                    return;
                } else {
                    server.signalGeneralServerProblem("Unexpected closing of welcome socket");
                }
            }

            try {
                clientConnection = new Connection(clientSocket);
            } catch (IOException exception) {
                logger.severe("Connection aborted: " + exception.getMessage());
                continue;
            }

            EventListener eventListener = new EventListener(server, clientConnection, incomingEventsQueue);
            new Thread(
                    eventListener,
                    "EventListener on connection with " + clientConnection.getRemoteAddress()
            ).start();

            server.startEchoSender(clientConnection);
            server.addConnection(clientConnection, eventListener);
        }
    }

    void stop() {
        stop = true;
    }
}
