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

    public synchronized static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public synchronized boolean start(String address, int port) throws IllegalStateException {
        if (isRunning()) {
            throw new IllegalStateException("Client is already started");
        }

        setRunning(true);
        getLogger().info("Starting client");

        this.serverIpAddress = address;
        this.serverPort = port;

        Connection serverConnection;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), CONNECT_TIMEOUT);
            serverConnection = new Connection(socket);
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

    public void send(Event event) {
        send(event, getServerConnection().getRemoteAddress());
    }

    @Override
    synchronized void signalConnectionProblem(Connection connection, String message) {
        getLogger().info("Connection problem: " + message);
        stop();
    }

    public synchronized void stop() {
        try {
            closeConnection(getServerConnection());
            getLogger().info("Client stopped correctly");
        } catch (IOException exception) {
            getLogger().severe("Could not close connection with server");
        } finally {
            clearObservers();
            getIncomingEventsQueue().clear();
            setRunning(false);
            networkStopNotify();
        }
    }
}
