package it.polimi.ingsw.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a GoldCard in the game.
 * It extends the Card class and maintains a list of requirements.
 */
public class GoldCard extends Card implements Serializable {
    private Point point;
    private List<Requirement> requirements;

    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
    /**
     * Returns the requirements of the card.
     * @return The requirements of the card.
     */
    public List<Requirement> getRequirements() {
        return requirements;
    }
    /**
     * Sets the requirements of the card.
     * @param requirements The requirements to set.
     */
    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

}