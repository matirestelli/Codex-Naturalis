package it.polimi.ingsw.core.model;

/**
 * This class represents a StarterSide in the game.
 * It extends the CardGame class and implements Serializable interface.
 * It maintains the id and the side of the starter side.
 */
public class StarterSide extends CardGame implements java.io.Serializable {
    private int id;
    private Boolean side;

    /**
     * Constructor for the StarterSide class.
     * @param id The id of the starter side.
     * @param side The side of the starter side.
     */
    public StarterSide(int id, Boolean side) {
        this.id = id;
        this.side = side;
    }

    /**
     * Returns the id of the starter side.
     * @return The id of the starter side.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the side of the starter side.
     * @return The side of the starter side.
     */
    public Boolean getSide() {
        return side;
    }
}