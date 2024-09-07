package it.simoneamighini.scala40.model;

import java.io.Serializable;
import java.util.List;

class GroupAttacher implements Serializable {
    private final Match match;
    private boolean locked;
    private List<Card> tempCards;
    private Position tempPosition;
    private boolean tempResult;

    GroupAttacher(Match match) {
        this.match = match;
        this.locked = false;
        this.tempCards = null;
        this.tempPosition = null;
        this.tempResult = false;
    }

    private void saveTempData(List<Card> cards, Position position) {
        tempCards = cards;
        tempPosition = position;
    }

    boolean attach(List<Card> cards, int groupNumber, Position position) throws RuntimeException {
        // in use
        locked = true;

        // save the data
        saveTempData(cards, position);

        // retrieve the requested group
        Group group;
        try {
            group = match.getGroups().get(groupNumber);
        } catch (IndexOutOfBoundsException exception) {
            throw new RuntimeException("Cannot attach a group to group number " + groupNumber +
                    " because there is no such group");
        }

        // use the Visitor pattern to execute the attachment
        visit(group);

        // free to use
        locked = false;

        return tempResult;
    }

    private void visit(Group group) {
        group.accept(this);
    }

    void executeAttachmentOn(Combination combination) throws IllegalStateException {
        if (!locked) {
            throw new IllegalStateException("Cannot attach a combination using a locked attacher");
        }

        List<Card> cards = combination.getCards();
        switch (tempPosition) {
            case START -> cards.addAll(0, tempCards);
            case END -> cards.addAll(cards.size(), tempCards);
        }

        Group newCombination;
        try {
            newCombination = new Combination(cards, true);
            match.updateGroup(combination, newCombination);
            tempResult = true;
        } catch (InvalidGroupException exception) {
            tempResult = false;
        }
    }

    void executeAttachmentOn(Sequence sequence) {
        if (!locked) {
            throw new IllegalStateException("Cannot attach a sequence using a locked attacher");
        }

        List<Card> cards = sequence.getCards();
        switch (tempPosition) {
            case START -> cards.addAll(0, tempCards);
            case END -> cards.addAll(cards.size(), tempCards);
        }

        Group newSequence;
        try {
            newSequence = new Sequence(cards, true);
            match.updateGroup(sequence, newSequence);
            tempResult = true;
        } catch (InvalidGroupException exception) {
            tempResult = false;
        }
    }
}
