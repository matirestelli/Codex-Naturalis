package it.polimi.ingsw.core.model;

public class DrawCard implements java.io.Serializable {
    private String id;

    public DrawCard(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
