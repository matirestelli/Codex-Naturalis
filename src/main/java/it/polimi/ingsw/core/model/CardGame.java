package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;

import java.io.Serializable;

/**
 * This abstract class represents a game card.
 * It implements Serializable interface.
 * It maintains the id, frontSide status, frontCover, backCover, and color of the card.
 */
public abstract class CardGame implements Serializable {
    private int id;
    public boolean frontSide;
    private String frontCover;
    private String backCover;
    private Color color;

    /**
     * Returns the id of the card.
     * @return id of the card.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the card.
     * @param id id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the frontSide status of the card.
     * @return frontSide status of the card.
     */
    public boolean isFrontSide() {
        return frontSide;
    }

    /**
     * Sets the frontSide status of the card.
     * @param frontSide frontSide status to set.
     */
    public void setSide(boolean frontSide) {
        this.frontSide = frontSide;
    }

    /**
     * Returns the frontCover of the card.
     * @return frontCover of the card.
     */
    public String getFrontCover() {
        return frontCover;
    }

    /**
     * Sets the frontCover of the card.
     * @param frontCover frontCover to set.
     */
    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

    /**
     * Returns the backCover of the card.
     * @return backCover of the card.
     */
    public String getBackCover() {
        return backCover;
    }

    /**
     * Sets the backCover of the card.
     * @param backCover backCover to set.
     */
    public void setBackCover(String backCover) {
        this.backCover = backCover;
    }

    /**
     * Returns the color of the card.
     * @return color of the card.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the card.
     * @param color color to set.
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
