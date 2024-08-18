package it.simoneamighini.scala40.model;

import java.util.List;

class JollyReplacer {
    private final Match match;

    JollyReplacer(Match match) {
        this.match = match;
    }

    JollyCard replace(int groupNumber, Card card) {
        // find the jolly card inside the group (if exists)
        Group group = match.getGroups().get(groupNumber);
        List<Card> groupCards = group.getCards();
        JollyCard jollyCard = null;
        int jollyCardIndex = 0;

        for (Card groupCard : groupCards) {
            if (groupCard instanceof JollyCard) {
                jollyCard = (JollyCard) groupCard;
                jollyCardIndex = groupCards.indexOf(jollyCard);
            }
        }

        // if the jolly exists then try to replace it with the passed card
        if (jollyCard != null) {
            groupCards.set(jollyCardIndex, card);

            try {
                Group combination = new Combination(groupCards);
                match.updateGroup(group, combination);
                return jollyCard;
            } catch (InvalidGroupException ignored) {
                // maybe the group is valid sequence: continue
            }

            try {
                Group sequence = new Sequence(groupCards);
                match.updateGroup(group, sequence);
                return jollyCard;
            } catch (InvalidGroupException ignored) {
                // the group is not a valid combination nor sequence
            }
        }

        // otherwise
        return null;
    }
}
