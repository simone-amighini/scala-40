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

    public static Server getInstance() {
        return Server.getInstance(DEFAULT_PORT);
    }

    public static Server getInstance(int port) {
        if (instance == null) {
            try {
                instance = new Server(port);
            } catch (IOException exception) {
                System.err.println("Could not instantiate server on port " + port + ".");
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

    public boolean start() throws IllegalStateException {
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
            } catch (IOException exception) {
                signalGeneralServerProblem("cannot close connection with " + searchedConnection.getRemoteAddress());
            }
        }
    }

    synchronized void signalGeneralServerProblem(String message) {
        getLogger().severe("Server general problem: " + message);
        getLogger().severe("Emergency server stop activated");
        networkStopNotify();
        stop();
    }

    @Override
    synchronized void signalConnectionProblem(Connection connection, String message) {
        String messageSuffix = "";
        if (message != null) {
            messageSuffix = ": " + message;
        }
        getLogger().info("Disconnection of " + connection.getRemoteAddress() + messageSuffix);
        try {
            closeConnection(connection);
        } catch (IOException ignored) {}
        connectionClosingNotify(connection);
    }

    public void stop() {
        if (!isRunning()) {
            return;
        }

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
            getCurrentEventReaderThread().interrupt();
            getIncomingEventsQueue().reset();
            setRunning(false);
        }
    }
}
