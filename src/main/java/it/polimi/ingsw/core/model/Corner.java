package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

import java.io.Serializable;

/**
 * This class represents a Corner in the game.
 * It implements Serializable interface.
 * It maintains the empty status, resource, and hidden status of the corner.
 */
public class Corner implements Serializable {
    private boolean empty;
    private Resource resource;
    private boolean hidden;

    /**
     * Checks if the corner is empty.
     * @return True if the corner is empty, false otherwise.
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Sets the empty status of the corner.
     * @param empty The empty status to set.
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Returns the resource of the corner.
     * @return The resource of the corner.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the resource of the corner.
     * @param resource The resource to set.
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * Checks if the corner is hidden.
     * @return True if the corner is hidden, false otherwise.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets the hidden status of the corner.
     * @param hidden The hidden status to set.
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}