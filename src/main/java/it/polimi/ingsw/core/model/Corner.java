package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

import java.io.Serializable;

public class Corner implements Serializable {
    private boolean empty;
    private Resource resource;
    private boolean hidden;

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

}