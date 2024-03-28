package it.polimi.ingsw.gc38.model;

import java.util.List;

public class GoldCard extends Card {
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
