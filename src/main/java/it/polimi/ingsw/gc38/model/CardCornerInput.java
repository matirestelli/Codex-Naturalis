package it.polimi.ingsw.gc38.model;

public class CardCornerInput {
    private int id;
    private boolean frontSide;

    public CardCornerInput(int id, boolean frontSide) {
        this.id = id;
        this.frontSide = frontSide;
    }

    public int getId() {
        return id;
    }

    public boolean isFrontSide() {
        return frontSide;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrontSide(boolean frontSide) {
        this.frontSide = frontSide;
    }
}
