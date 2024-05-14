package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;

import java.io.Serializable;

public abstract class CardGame implements Serializable {
    private int id;
    public boolean frontSide;
    private String frontCover;
    private String backCover;
    private Color color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFrontSide() {
        return frontSide;
    }

    public void setSide(boolean frontSide) {
        this.frontSide = frontSide;
    }

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

    public String getBackCover() {
        return backCover;
    }

    public void setBackCover(String backCover) {
        this.backCover = backCover;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
