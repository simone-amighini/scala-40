package it.simoneamighini.scala40.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class AttachmentChecker implements Serializable {
    private final Match match;

    AttachmentChecker(Match match) {
        this.match = match;
    }

    boolean isAttachable(Card card) {
        for (Group group : match.getGroups()) {
            List<Card> currentGroupCards = group.getCards();

            List<Card> attachAtStartGroup = new ArrayList<>(currentGroupCards);
            attachAtStartGroup.addFirst(card);

            List<Card> attachAtEndGroup = new ArrayList<>(currentGroupCards);
            attachAtEndGroup.addLast(card);

            try {
                new Combination(attachAtStartGroup, true);
                return true;
            } catch (InvalidGroupException ignored) {
                // continue
            }

            try {
                new Combination(attachAtEndGroup, true);
                return true;
            } catch (InvalidGroupException ignored) {
                // maybe the group is valid sequence: continue
            }

            try {
                new Sequence(attachAtStartGroup, true);
                return true;
            } catch (InvalidGroupException ignored) {
                // continue
            }

            try {
                new Sequence(attachAtEndGroup, true);
                return true;
            } catch (InvalidGroupException ignored) {
                // the group is not a valid combination nor sequence
            }
        }

        // if the card cannot be attached to any combination or sequence
        return false;
    }
}
