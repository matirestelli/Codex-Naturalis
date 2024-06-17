package it.polimi.ingsw.core.model;

/**
 * This class represents a DrawCard in the game.
 * It implements Serializable interface.
 * It maintains the id of the card.
 */
public class DrawCard implements java.io.Serializable {
    private String id;

    /**
     * Constructs a DrawCard with the given id.
     * @param id The id of the card.
     */
    public DrawCard(String id) {
        this.id = id;
    }

    /**
     * Returns the id of the card.
     * @return The id of the card.
     */
    public String getId() {
        return id;
    }
}