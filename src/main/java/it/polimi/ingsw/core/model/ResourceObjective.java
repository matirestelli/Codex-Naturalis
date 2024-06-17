package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a ResourceObjective in the game.
 * It extends the Objective class.
 * It maintains a list of requirements and methods related to the calculation of points.
 */
public class ResourceObjective extends Objective {
    List<Requirement> requirements;

    /**
     * Returns the type of the objective.
     * @return The type of the objective.
     */
    public String getType() {
        return "Resource";
    }

    /**
     * Constructor for the ResourceObjective class.
     */
    public ResourceObjective() {
        requirements = new ArrayList<>();
    }

    /**
     * Returns the list of requirements of the resource objective.
     * @return The list of requirements of the resource objective.
     */
    public List<Requirement> getRequirements() {
        return requirements;
    }

    /**
     * Calculates the points for the player state based on the requirements.
     * @param p The player state.
     */
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