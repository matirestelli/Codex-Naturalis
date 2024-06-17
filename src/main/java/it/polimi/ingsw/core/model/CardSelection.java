package it.polimi.ingsw.core.model;

/**
 * This class represents a card selection in the game.
 * It implements Serializable interface.
 * It maintains the id and side of the selected card.
 */
public class CardSelection implements java.io.Serializable {
    private int id;
    private Boolean side;

    /**
     * Constructs a CardSelection with the given id and side.
     * @param id The id of the card.
     * @param side The side of the card.
     */
    public CardSelection(int id, Boolean side) {
        this.id = id;
        this.side = side;
    }

    /**
     * Returns the id of the selected card.
     * @return The id of the selected card.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the side of the selected card.
     * @return The side of the selected card.
     */
    public Boolean getSide() {
        return side;
    }
}