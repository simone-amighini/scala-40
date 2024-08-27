package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.FirstPlayerChoicesEvent;
import it.simoneamighini.scala40.events.GameEnterEvent;
import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.networking.NetworkObserver;
import it.simoneamighini.scala40.networking.Server;

import java.util.*;

public class ConnectionsManager implements NetworkObserver {
    private static ConnectionsManager instance;

    private ConnectionsManagerState state;
    private final Server server;
    private final HashMap<String, String> usernameConnectionMap;
    private final List<String> usernamesInOrder;

    public static ConnectionsManager getInstance() {
        if (instance == null) {
            instance = new ConnectionsManager();
        }
        return instance;
    }

    private ConnectionsManager() {
        this.state = new NoClientConnectedState(this);
        this.server = Server.getInstance();
        this.usernameConnectionMap = new HashMap<>();
        this.usernamesInOrder = new ArrayList<>();

        server.addObserver(this);
    }

    public boolean passesUsernameCheck(String newUsername) {
        return usernameConnectionMap.keySet().stream().noneMatch(username -> username.equals(newUsername)) &&
                newUsername != null && !newUsername.isBlank() &&
                !newUsername.contains(" ") && !(newUsername.length() > 20);
    }

    boolean isConnected(String remoteAddress) {
        return usernameConnectionMap.containsValue(remoteAddress);
    }

    void addToUsernameConnectionMap(String username, String remoteAddress) {
        usernameConnectionMap.put(username, remoteAddress);
        usernamesInOrder.add(username);
    }

    void removeUsernameFromUsernameConnectionMap(String username) {
        usernameConnectionMap.remove(username);
        usernamesInOrder.remove(username);
    }

    void removeRemoteAddressFromUsernameConnectionMap(String remoteAddress) throws NoSuchElementException {
        String relatedUsername = usernameConnectionMap.keySet().stream()
                .filter(username -> usernameConnectionMap.get(username).equals(remoteAddress))
                .findFirst()
                .get();

        removeUsernameFromUsernameConnectionMap(relatedUsername);
    }

    String getAssociatedRemoteAddress(String username) {
        return usernameConnectionMap.get(username);
    }

    boolean isFirstPlayerConnection(String remoteAddress) {
        return usernameConnectionMap.get(usernamesInOrder.getFirst()).equals(remoteAddress);
    }

    public List<String> getUsernamesInOrder() {
        return new ArrayList<>(usernamesInOrder);
    }

    int getNumberOfConnections() {
        return usernameConnectionMap.size();
    }

    public synchronized void sendEvent(Event event, String remoteAddress) {
        server.send(event, remoteAddress);
    }

    public synchronized void sendEventBroadcast(Event event) {
        for (String remoteAddress : usernameConnectionMap.values()) {
            server.send(event, remoteAddress);
        }
    }

    void closeConnection(String remoteAddress) {
        server.closeConnection(remoteAddress);
    }

    void changeState(ConnectionsManagerState state) {
        this.state = state;
    }

    public synchronized void handle(GameEnterEvent event) {
        state.handle(event);
    }

    public synchronized void handle(FirstPlayerChoicesEvent event) {
        state.handle(event);
    }

    @Override
    public synchronized void connectionClosingUpdate(String remoteAddress) {
        if (usernameConnectionMap.containsValue(remoteAddress)) {
            state.handleClientDisconnection(remoteAddress);
        }
    }

    @Override
    public synchronized void networkStopUpdate() {
        reset();
    }

    public synchronized void reset() {
        usernameConnectionMap.clear();
        usernamesInOrder.clear();
        changeState(new NoClientConnectedState(this));
    }
}
