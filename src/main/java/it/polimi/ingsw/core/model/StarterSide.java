package it.polimi.ingsw.core.model;

public class StarterSide extends CardGame implements java.io.Serializable {
    private int id;
    private Boolean side;

    public StarterSide(int id, Boolean side) {
        this.id = id;
        this.side = side;
    }

    public int getId() {
        return id;
    }

    public Boolean getSide() {
        return side;
    }
}
