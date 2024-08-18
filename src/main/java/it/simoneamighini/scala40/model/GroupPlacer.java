package it.simoneamighini.scala40.model;

import java.util.List;

class GroupPlacer {
    private final Match match;

    GroupPlacer(Match match) {
        this.match = match;
    }

    boolean place(List<Card> group) {
        try {
            Group combination = new Combination(group);
            match.addGroup(combination);
            return true;
        } catch (InvalidGroupException ignored) {
            // maybe the group is valid sequence: continue
        }

        try {
            Group sequence = new Sequence(group);
            match.addGroup(sequence);
            return true;
        } catch (InvalidGroupException ignored) {
            // the group is not a valid combination nor sequence...
        }

        // ... so
        return false;
    }
}
