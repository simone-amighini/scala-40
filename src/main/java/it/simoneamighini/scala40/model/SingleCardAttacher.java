package it.simoneamighini.scala40.model;

import it.simoneamighini.scala40.utils.Position;

import java.util.List;

class SingleCardAttacher {
    private final Match match;
    private boolean locked;
    private Card tempCard;
    private Position tempPosition;
    private boolean tempResult;

    SingleCardAttacher(Match match) {
        this.match = match;
        this.locked = false;
        this.tempCard = null;
        this.tempPosition = null;
        this.tempResult = false;
    }

    private void saveTempData(Card card, Position position) {
        tempCard = card;
        tempPosition = position;
    }

    boolean attach(Card card, int groupNumber, Position position) {
        // in use
        locked = true;

        // save the data
        saveTempData(card, position);

        // retrieve the requested group
        Group group;
        try {
            group = match.getGroups().get(groupNumber);
        } catch (IndexOutOfBoundsException exception) {
            throw new RuntimeException("Cannot attach " + card.getName() + " to group number " +
                    groupNumber + " because there is no such group");
        }

        // use the Visitor pattern to execute the placement
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
            case START -> cards.addFirst(tempCard);
            case END -> cards.addLast(tempCard);
        }

        Group newCombination;
        try {
            newCombination = new Combination(cards);
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
            case START -> cards.addFirst(tempCard);
            case END -> cards.addLast(tempCard);
        }

        Group newSequence;
        try {
            newSequence = new Sequence(cards);
            match.updateGroup(sequence, newSequence);
            tempResult = true;
        } catch (InvalidGroupException exception) {
            tempResult = false;
        }
    }
}
