package it.polimi.ingsw.gc38.model;

import it.polimi.ingsw.gc38.view.CliView;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ResourceObjective extends Objective {
    List<Requirement> requirements;

    public int calculatePoints(Player p) {
        Map<Resource, Integer> playerResources = p.calculateResources();
        List<Integer> cardinality = new ArrayList<>();

        for (Requirement req : requirements) {
            // if player has the required resource, add its cardinality to the list
            // otherwise, return 0 points
            if (playerResources.get(req.getResource()) == 0)
                return 0;
            cardinality.add(playerResources.get(req.getResource()) / req.getQta());
        }

        // to calculate point, use minimum cardinality of resources
        return cardinality.stream().min(Integer::compare).get() * getPoints();
    }

    public void visualizeCard(CliView cli) {
        cli.displayMessage("Resource Objective");
    }
}

