package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;

import java.util.List;
import java.util.Map;

public class MatchInfoUpdateEvent extends Event {
    private final String username;
    private final int matchNumber;
    private final int turnNumber;
    private final Map<String, Integer> usernamePointsMap;
    private final Map<String, Boolean> usernameOpeningCompletedMap;
    private final Map<String, Integer> usernameRemainingCardsMap;
    private final String deckTopCardID;
    private final String visibleDiscardedCardID;
    private final List<List<String>> groups;
    private final List<String> hand;

    public MatchInfoUpdateEvent(
            String username,
            int matchNumber,
            int turnNumber,
            Map<String, Integer> usernamePointsMap,
            Map<String, Boolean> usernameOpeningCompletedMap,
            Map<String, Integer> usernameRemainingCardsMap,
            String deckTopCardID,
            String visibleDiscardedCardID,
            List<List<String>> groups,
            List<String> hand
    ) {
        super("MATCH_INFO_UPDATE");
        this.username = username;
        this.matchNumber = matchNumber;
        this.turnNumber = turnNumber;
        this.usernamePointsMap = usernamePointsMap;
        this.usernameOpeningCompletedMap = usernameOpeningCompletedMap;
        this.usernameRemainingCardsMap = usernameRemainingCardsMap;
        this.deckTopCardID = deckTopCardID;
        this.visibleDiscardedCardID = visibleDiscardedCardID;
        this.groups = groups;
        this.hand = hand;
    }

    @Override
    public void callHandler() {
        Data.getInstance().setUsername(username);
        Data.getInstance().setMatchNumber(matchNumber);
        Data.getInstance().setTurnNumber(turnNumber);
        Data.getInstance().setUsernamePointsMap(usernamePointsMap);
        Data.getInstance().setUsernameOpeningCompletedMap(usernameOpeningCompletedMap);
        Data.getInstance().setUsernameRemainingCardsMap(usernameRemainingCardsMap);
        Data.getInstance().setDeckTopCardID(deckTopCardID);
        Data.getInstance().setVisibleDiscardedCardID(visibleDiscardedCardID);
        Data.getInstance().setGroups(groups);
        Data.getInstance().setHand(hand);
        SceneLoader.getCurrentController().handle(this);
    }
}
