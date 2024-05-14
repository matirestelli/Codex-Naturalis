package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

public class Requirement implements java.io.Serializable {
    private int qta;
    private Resource resource;

    public int getQta() {
        return qta;
    }

    public void setQta(int qta) {
        this.qta = qta;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Requirement(Resource resource, int qta) {
        this.resource = resource;
        this.qta = qta;
    }

    public void incrementQta() {
        this.qta++;
    }

}
