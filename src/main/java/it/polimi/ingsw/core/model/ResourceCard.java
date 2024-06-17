package it.polimi.ingsw.core.model;

import java.io.Serializable;

/**
 * This class represents a ResourceCard in the game.
 * It extends the Card class and implements Serializable interface.
 * It maintains the point and the ANSI_COLOR of the resource card.
 */
public class ResourceCard extends Card implements Serializable {
    private int point;
    private String ANSI_COLOR;

    /**
     * Returns the point of the resource card.
     * @return The point of the resource card.
     */
    public int getPoint() {
        return point;
    }

    /**
     * Sets the point of the resource card.
     * @param point The point to set.
     */
    public void setPoint(int point) {
        this.point = point;
    }

    /**
     * Returns the ANSI_COLOR of the resource card.
     * @return The ANSI_COLOR of the resource card.
     */
    public String getANSI_COLOR() {
        return ANSI_COLOR;
    }

    /**
     * Sets the ANSI_COLOR of the resource card.
     * @param ANSI_COLOR The ANSI_COLOR to set.
     */
    public void setANSI_COLOR(String ANSI_COLOR) {
        this.ANSI_COLOR = ANSI_COLOR;
    }
}