package it.simoneamighini.scala40.model;

import java.util.List;

class AttachmentChecker {
    private final Match match;

    AttachmentChecker(Match match) {
        this.match = match;
    }

    boolean isAttachable(Card card) {
        for (Group group : match.getGroups()) {
            List<Card> currentGroupCards = group.getCards();
            currentGroupCards.add(card);

            try {
                Group combination = new Combination(currentGroupCards);
                return true;
            } catch (InvalidGroupException ignored) {
                // maybe the group is valid sequence: continue
            }

            try {
                Group sequence = new Sequence(currentGroupCards);
                return true;
            } catch (InvalidGroupException ignored) {
                // the group is not a valid combination nor sequence
            }
        }

        // if the card cannot be attached to any combination or sequence
        return false;
    }
}
