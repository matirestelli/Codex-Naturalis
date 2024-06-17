package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

/**
 * This class represents a Requirement in the game.
 * It implements the Serializable interface.
 * It maintains the quantity and the resource of the requirement.
 */
public class Requirement implements java.io.Serializable {
    private int qta;
    private Resource resource;

    /**
     * Constructor for the Requirement class.
     * @param resource The resource of the requirement.
     * @param qta The quantity of the requirement.
     */
    public Requirement(Resource resource, int qta) {
        this.resource = resource;
        this.qta = qta;
    }

    /**
     * Returns the quantity of the requirement.
     * @return The quantity of the requirement.
     */
    public int getQta() {
        return qta;
    }

    /**
     * Sets the quantity of the requirement.
     * @param qta The quantity to set.
     */
    public void setQta(int qta) {
        this.qta = qta;
    }

    /**
     * Returns the resource of the requirement.
     * @return The resource of the requirement.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the resource of the requirement.
     * @param resource The resource to set.
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * Increments the quantity of the requirement by one.
     */
    public void incrementQta() {
        this.qta++;
    }
}