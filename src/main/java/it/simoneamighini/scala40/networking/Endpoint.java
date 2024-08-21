package it.simoneamighini.scala40.networking;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public abstract class Endpoint {
    private final Logger logger;
    private boolean running;
    private final Map<Connection, EventListener> connectionsMap;
    private final IncomingEventsQueue incomingEventsQueue;
    private final List<NetworkObserver> observers;

    Endpoint(String classname) {
        this.logger = Logger.getLogger(classname);
        this.running = false;
        this.connectionsMap = new HashMap<>();
        this.incomingEventsQueue = new IncomingEventsQueue();
        this.observers = new ArrayList<>();
    }

    Logger getLogger() {
        return logger;
    }

    boolean isRunning() {
        return running;
    }

    void setRunning(boolean running) {
        this.running = running;
    }

    Map<Connection, EventListener> getConnectionsMap() {
        return connectionsMap;
    }

    public IncomingEventsQueue getIncomingEventsQueue() {
        return incomingEventsQueue;
    }

    public synchronized void addObserver(NetworkObserver observer) {
        observers.add(observer);
    }

    public synchronized void removeObserver(NetworkObserver observer) {
        observers.remove(observer);
    }

    synchronized void clearObservers() {
        observers.clear();
    }

    void startEchoSender(Connection connection) {
        Timer timer = new Timer();
        TimerTask echoSender = new EchoSender(this, connection);
        connection.setEchoSender(echoSender);
        timer.schedule(echoSender, 0, EchoSender.ECHO_PERIOD);
    }

    void networkStopNotify() {
        for (NetworkObserver observer : observers) {
            observer.networkStopUpdate();
        }
    }

    synchronized void addConnection(Connection connection, EventListener eventListener) {
        connectionsMap.put(connection, eventListener);
    }

    Connection getCorrespondentConnection(String remoteAddress) {
        Connection searchedConnection = null;
        for (Connection connection : getConnectionsMap().keySet()) {
            if (remoteAddress.equals(connection.getRemoteAddress())) {
                searchedConnection = connection;
            }
        }
        return searchedConnection;
    }

    public void send(Event event, String remoteAddress) {
        Connection connection = getCorrespondentConnection(remoteAddress);
        if (connection != null) {
            try {
                connection.send(event);
            } catch (IOException exception) {
                signalConnectionProblem(connection, exception.getMessage());
            }
        }
    }

    synchronized void closeConnection(Connection connection) throws IOException {
        // stop event listener
        connectionsMap.get(connection).stop();

        // remove connection from map
        connectionsMap.remove(connection);

        // close opened streams
        try {
            connection.close();
            connection.getEchoSender().cancel();
            logger.info("Closed connection with " + connection.getRemoteAddress());
        } catch (IOException exception) {
            logger.severe("Could not close connection with" + connection.getRemoteAddress());
            throw exception;
        }
    }

    public synchronized void closeAllConnections() throws IOException {
        Map<Connection, EventListener> connectionsMapCopy = new HashMap<>(getConnectionsMap());
        for (Connection connection : connectionsMapCopy.keySet()) {
            closeConnection(connection);
        }
    }

    void connectionClosingNotify(Connection connection) {
        for (NetworkObserver observer : observers) {
            observer.connectionClosingUpdate(connection.getRemoteAddress());
        }
    }

    abstract void signalConnectionProblem(Connection connection, String message);
}
