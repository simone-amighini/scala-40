package it.simoneamighini.scala40.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Server extends Endpoint {
    private static Server instance = null;
    public static final int DEFAULT_PORT = 20000;

    private final int port;
    private final ServerSocket welcomeSocket;
    private final NetworkListener networkListener;

    private Server(int port) throws IOException {
        super(Server.class.getSimpleName());

        this.port = port;
        this.networkListener = new NetworkListener(this);

        welcomeSocket = new ServerSocket(port);
    }

    public synchronized static Server getInstance() {
        return Server.getInstance(DEFAULT_PORT);
    }

    public synchronized static Server getInstance(int port) {
        if (instance == null) {
            try {
                instance = new Server(port);
            } catch (IOException exception) {
                instance.getLogger().severe("Could not instantiate server on port " + port);
                return null;
            }
        }

        if (instance.port != port) {
            instance.getLogger().warning("The server is already active on port " + instance.port +
                    " instead of " + port + " as requested");
        }

        return instance;
    }

    ServerSocket getWelcomeSocket() {
        return welcomeSocket;
    }

    public synchronized boolean start() throws IllegalStateException {
        if (isRunning()) {
            throw new IllegalStateException("Server is already started");
        }

        setRunning(true);

        Thread networkListenerThread = new Thread(networkListener, "NetworkListener (server)");
        networkListenerThread.start();

        try {
            getLogger().info("Server local IP address is " + InetAddress.getLocalHost().getHostAddress() +
                    ":" + port);
            return true;
        } catch (UnknownHostException exception) {
            getLogger().severe("Could not determine server address");
            networkListenerThread.interrupt();
            return false;
        }
    }

    public synchronized void closeConnection(String remoteAddress) {
        Connection searchedConnection = getCorrespondentConnection(remoteAddress);
        if (searchedConnection != null) {
            try {
                closeConnection(searchedConnection);
            } catch (IOException ignored) {}
        }
    }

    synchronized void signalGeneralServerProblem(String message) {
        getLogger().severe("Server general problem: " + message);
        getLogger().severe("Emergency server stop activated");
        stop();
    }

    @Override
    synchronized void signalConnectionProblem(Connection connection, String message) {
        getLogger().info("Unexpected disconnection from " + connection.getRemoteAddress() + ": " + message);
        try {
            closeConnection(connection);
        } catch (IOException ignored) {}
        connectionClosingNotify(connection);
    }

    public synchronized void stop() {
        try {
            closeAllConnections();
        } catch (IOException ignored) {}

        networkListener.stop();

        try {
            welcomeSocket.close();
            getLogger().info("Server stopped correctly");
        } catch (IOException exception) {
            getLogger().severe("Could not close welcome socket correctly");
        } finally {
            clearObservers();
            getIncomingEventsQueue().clear();
            setRunning(false);
            networkStopNotify();
        }
    }
}
