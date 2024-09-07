package it.simoneamighini.scala40.model;

import java.io.Serializable;
import java.util.List;

class JollyReplacer implements Serializable {
    private final Match match;

    JollyReplacer(Match match) {
        this.match = match;
    }

    JollyCard replace(Card card, int groupNumber, int jollyIndex) {
        try {
            // check if the specified card is actually a jolly card
            JollyCard jollyCard;
            Group group = match.getGroups().get(groupNumber);
            List<Card> groupCards = group.getCards();
            Card cardAtJollyIndex = groupCards.get(jollyIndex);

            if (cardAtJollyIndex instanceof JollyCard) {
                jollyCard = (JollyCard) cardAtJollyIndex;
            } else {
                return null;
            }

            // if the jolly exists then try to replace it with the passed card
            groupCards.set(jollyIndex, card);

            try {
                Group combination = new Combination(groupCards, false);
                match.updateGroup(group, combination);
                return jollyCard;
            } catch (InvalidGroupException ignored) {
                // maybe the group is valid sequence: continue
            }

            try {
                Group sequence = new Sequence(groupCards, false);
                match.updateGroup(group, sequence);
                return jollyCard;
            } catch (InvalidGroupException ignored) {
                // the group is not a valid combination nor sequence
            }

            // otherwise
            return null;

        } catch (Exception exception) {
            return null;
        }
    }
}
