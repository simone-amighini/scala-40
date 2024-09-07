package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.*;
import it.simoneamighini.scala40.model.Game;
import it.simoneamighini.scala40.model.PersistenceUtility;
import it.simoneamighini.scala40.model.Player;
import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.networking.NetworkObserver;
import it.simoneamighini.scala40.networking.Server;
import it.simoneamighini.scala40.servercontroller.GameController;

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

    public void removeUsernameFromUsernameConnectionMap(String username) {
        usernameConnectionMap.remove(username);
        usernamesInOrder.remove(username);
    }

    void removeRemoteAddressFromUsernameConnectionMap(String remoteAddress) {
        removeUsernameFromUsernameConnectionMap(getAssociatedUsername(remoteAddress));
    }

    public String getAssociatedRemoteAddress(String username) {
        return usernameConnectionMap.get(username);
    }

    public String getAssociatedUsername(String remoteAddress) {
        return usernameConnectionMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(remoteAddress))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
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

    public synchronized void handle(DrawFromDeckEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public synchronized void handle(PickFromDiscardedCardsEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public synchronized void handle(DiscardCardEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public synchronized void handle(OpeningEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public synchronized void handle(PlaceGroupEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public synchronized void handle(AttachGroupEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public synchronized void handle(AttachCardEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public synchronized void handle(ReplaceJollyEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public synchronized void handle(CancelTurnEvent event) {
        if (state instanceof BlockedConnectionsState) {
            GameController.getInstance().handle(event);
        } else {
            sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
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
        ConnectionsManager.getInstance().reset();
    }

    public synchronized void reset() {
        usernameConnectionMap.clear();
        usernamesInOrder.clear();
        changeState(new NoClientConnectedState(this));
    }

    void startNewGame() {
        List<Player> players = new ArrayList<>();
        for (String username : getUsernamesInOrder()) {
            players.add(new Player(username));
        }

        sendEventBroadcast(new NewGameEvent());

        Game game = new Game(players);
        GameController.getInstance().setGame(game);
        GameController.getInstance().start(false);
    }

    void resumeGame() {
        GameController.getInstance().setGame(PersistenceUtility.loadGameFromDisk());
        sendEventBroadcast(new GameResumeEvent());
        GameController.getInstance().start(true);
    }
}
