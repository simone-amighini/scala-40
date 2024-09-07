package it.simoneamighini.scala40.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class OpeningPlacer implements Serializable {
    private final Match match;

    OpeningPlacer(Match match) {
        this.match = match;
    }

    boolean place(List<List<Card>> groups) {
        // check if all the groups are valid
        List<Group> groupList = new ArrayList<>();
        for (List<Card> group : groups) {
            try {
                Group combination = new Combination(group, false);
                groupList.add(combination);
                continue;
            } catch (InvalidGroupException ignored) {
                // maybe the group is valid sequence: continue
            }

            try {
                Group sequence = new Sequence(group, false);
                groupList.add(sequence);
                continue;
            } catch (InvalidGroupException ignored) {
                // the group is not a valid combination nor sequence...
            }

            // ... so
            return false;
        }

        // check if the total amount of points is >= 40
        int totalPoints = 0;

        for (Group group : groupList) {
            totalPoints += group.getPoints();
        }

        if (totalPoints >= 40) {
            for (Group group : groupList) {
                match.addGroup(group);
            }
            return true;
        } else {
            return false;
        }
    }
}
