package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

/**
 * This class represents a Point in the game.
 * It implements the Serializable interface.
 * It maintains the quantity, simplicity status, and the resource of the point.
 */
public class Point implements java.io.Serializable {
    private int qta;
    private boolean simple;
    private Resource resource;

    /**
     * Returns the quantity of the point.
     * @return The quantity of the point.
     */
    public int getQta() {
        return qta;
    }

    /**
     * Sets the quantity of the point.
     * @param qta The quantity to set.
     */
    public void setQta(int qta) {
        this.qta = qta;
    }

    /**
     * Returns the simplicity status of the point.
     * @return The simplicity status of the point.
     */
    public boolean isSimple() {
        return simple;
    }

    /**
     * Sets the simplicity status of the point.
     * @param simple The simplicity status to set.
     */
    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    /**
     * Returns the resource of the point.
     * @return The resource of the point.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the resource of the point.
     * @param resource The resource to set.
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}