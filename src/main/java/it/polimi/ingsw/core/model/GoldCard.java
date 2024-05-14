package it.polimi.ingsw.core.model;

import java.io.Serializable;
import java.util.List;

public class GoldCard extends Card implements Serializable {
    private Point point;
    private List<Requirement> requirements;

    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

}