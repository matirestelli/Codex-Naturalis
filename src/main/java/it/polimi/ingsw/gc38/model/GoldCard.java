package it.polimi.ingsw.gc38.model;

import java.util.List;

public class GoldCard extends Card implements ColoredCard {
    private Color color;
    private Point point; // classe Point
    private List<Requirement> requirements;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

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
