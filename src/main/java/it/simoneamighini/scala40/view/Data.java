package it.simoneamighini.scala40.view;

import it.simoneamighini.scala40.events.PlannedDisconnectionEvent;

import java.util.List;
import java.util.Map;

public class Data {
    private static Data instance;
    public enum GameStartType {
        NEW_GAME,
        RESUMED_GAME,
    }

    private String username;
    private boolean existsSavedGame;
    private GameStartType gameStartType;
    private PlannedDisconnectionEvent.Cause disconnectionCause;
    private int matchNumber;
    private int turnNumber;
    private boolean playerAlreadyPickedACard;
    private List<String> usernames;
    private Map<String, Integer> usernamePointsMap;
    private Map<String, Boolean> usernameOpeningCompletedMap;
    private Map<String, Integer> usernameRemainingCardsMap;
    private String deckTopCardID;
    private String visibleDiscardedCardID;
    private List<List<String>> groups;
    private List<String> hand;
    private List<String> finalRanking;
    private boolean isGameEnded;

    private Data() {}

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void reset() {
        instance = null;
    }

    public boolean existsSavedGame() {
        return existsSavedGame;
    }

    public void setExistsSavedGame(boolean existsSavedGame) {
        this.existsSavedGame = existsSavedGame;
    }

    public GameStartType getGameStartType() {
        return gameStartType;
    }

    public void setGameStartType(GameStartType gameStartType) {
        this.gameStartType = gameStartType;
    }

    public PlannedDisconnectionEvent.Cause getDisconnectionCause() {
        return disconnectionCause;
    }

    public void setDisconnectionCause(PlannedDisconnectionEvent.Cause disconnectionCause) {
        this.disconnectionCause = disconnectionCause;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public void setPlayerAlreadyPickedACard(boolean playerAlreadyPickedACard) {
        this.playerAlreadyPickedACard = playerAlreadyPickedACard;
    }

    public boolean hasPlayerAlreadyPickedACard() {
        return playerAlreadyPickedACard;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public Map<String, Integer> getUsernamePointsMap() {
        return usernamePointsMap;
    }

    public void setUsernamePointsMap(Map<String, Integer> usernamePointsMap) {
        this.usernamePointsMap = usernamePointsMap;
    }

    public Map<String, Boolean> getUsernameOpeningCompletedMap() {
        return usernameOpeningCompletedMap;
    }

    public void setUsernameOpeningCompletedMap(Map<String, Boolean> usernameOpeningCompletedMap) {
        this.usernameOpeningCompletedMap = usernameOpeningCompletedMap;
    }

    public Map<String, Integer> getUsernameRemainingCardsMap() {
        return usernameRemainingCardsMap;
    }

    public void setUsernameRemainingCardsMap(Map<String, Integer> usernameRemainingCardsMap) {
        this.usernameRemainingCardsMap = usernameRemainingCardsMap;
    }

    public String getDeckTopCardID() {
        return deckTopCardID;
    }

    public void setDeckTopCardID(String deckTopCardID) {
        this.deckTopCardID = deckTopCardID;
    }

    public String getVisibleDiscardedCardID() {
        return visibleDiscardedCardID;
    }

    public void setVisibleDiscardedCardID(String visibleDiscardedCardID) {
        this.visibleDiscardedCardID = visibleDiscardedCardID;
    }

    public List<List<String>> getGroups() {
        return groups;
    }
    
    public void setGroups(List<List<String>> groups) {
        this.groups = groups;
    }

    public List<String> getHand() {
        return hand;
    }

    public void setHand(List<String> hand) {
        this.hand = hand;
    }

    public boolean hasPlayerOpened() {
        return usernameOpeningCompletedMap.get(username);
    }

    public void setPlayerHasOpened(boolean playerHasOpened) {
        usernameOpeningCompletedMap.put(username, playerHasOpened);
    }

    public List<String> getFinalRanking() {
        return finalRanking;
    }

    public void setFinalRanking(List<String> finalRanking) {
        this.finalRanking = finalRanking;
    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }
}
