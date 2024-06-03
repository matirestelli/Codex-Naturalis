package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResourceObjective extends Objective {
    List<Requirement> requirements;

    public String getType() {
        return "Resource";
    }

    public ResourceObjective() {
        requirements = new ArrayList<>();
    }

    public void CalculatePoints(PlayerState p) {
        Map<Resource, Integer> playerResources = p.calculateResources();
        List<Integer> cardinality = new ArrayList<>();

        for (Requirement req : requirements) {
            // if player has the required resource, add its cardinality to the list
            // otherwise, return 0 points
            if (playerResources.get(req.getResource()) == 0)
                return;
            cardinality.add(playerResources.get(req.getResource()) / req.getQta());
        }

        // to calculate point, use minimum cardinality of resources
       p.addScore(cardinality.stream().min(Integer::compare).get() * getPoints());
    }
}

