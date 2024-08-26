package it.simoneamighini.scala40.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Endpoint {
    private static Client instance = null;
    private static final int CONNECT_TIMEOUT = 2000;

    private String serverIpAddress;
    private int serverPort;

    private Client() {
        super(Client.class.getSimpleName());
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public boolean start(String address, int port) throws IllegalStateException {
        if (isRunning()) {
            throw new IllegalStateException("Client is already started");
        }

        setRunning(true);
        getLogger().info("Starting client");

        Connection serverConnection;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), CONNECT_TIMEOUT);
            serverConnection = new Connection(socket);
            this.serverIpAddress = socket.getInetAddress().getHostAddress();
            this.serverPort = port;
            getLogger().info("Connected to server " + serverConnection.getRemoteAddress());
        } catch (IOException exception) {
            getLogger().severe("Could not connect to server: " + exception.getMessage());
            setRunning(false);
            return false;
        }


        EventListener eventListener = new EventListener(this, serverConnection, getIncomingEventsQueue());
        addConnection(serverConnection, eventListener);
        new Thread(eventListener, "EventListener on connection with " + serverConnection).start();
        startEchoSender(serverConnection);
        return true;
    }

    Connection getServerConnection() {
        return getCorrespondentConnection(serverIpAddress + ":" + serverPort);
    }

    public synchronized void send(Event event) {
        send(event, getServerConnection().getRemoteAddress());
    }

    @Override
    synchronized void signalConnectionProblem(Connection connection, String message) {
        String messageSuffix = "";
        if (message != null) {
            messageSuffix = ": " + message;
        }
        getLogger().info("Disconnection of " + connection.getRemoteAddress() + messageSuffix);

        networkStopNotify();
        stop();
    }

    public void stop() {
        if (!isRunning()) {
            return;
        }

        try {
            closeConnection(getServerConnection());
            getLogger().info("Client stopped correctly");
        } catch (IOException exception) {
            getLogger().severe("Could not close connection with server");
        } finally {
            clearObservers();
            getCurrentEventReaderThread().interrupt();
            getIncomingEventsQueue().reset();
            setRunning(false);
        }
    }
}
