package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

public class Point implements java.io.Serializable {
    private int qta;
    private boolean simple;
    private Resource resource;

    public int getQta() {
        return qta;
    }

    public void setQta(int qta) {
        this.qta = qta;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
