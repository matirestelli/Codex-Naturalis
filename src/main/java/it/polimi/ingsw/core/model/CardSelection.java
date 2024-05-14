package it.polimi.ingsw.core.model;

public class CardSelection implements java.io.Serializable {
    private int id;
    private Boolean side;

    public CardSelection(int id, Boolean side) {
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
