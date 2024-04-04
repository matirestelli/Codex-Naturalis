package it.polimi.ingsw.gc38.model;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ResourceObjective extends Objective {
    private Map<Integer, Resource> requirements;

    public int calculatePoints(Player p) {
        Map<Resource, Integer> playerResources = p.calculateResources();
        List<Integer> cardinality = new ArrayList<>();

        for (Resource resource : requirements.values()) {
            // if player has the required resource, add its cardinality to the list
            // otherwise, return 0 points
            if (playerResources.get(resource) != 0)
                cardinality.add(playerResources.get(resource));
            else
                return 0;
        }

        // to calculate point, use minimum cardinality of resources
        return cardinality.stream().min(Integer::compare).get() * getPoints();
    }
}

