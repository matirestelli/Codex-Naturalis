package it.polimi.ingsw.core.model;

/**
 * This class represents a cell in the game.
 * It implements Serializable interface.
 * It maintains the character, card, and color of the cell.
 */
public class Cell implements java.io.Serializable{
    private char character;
    private Card card;
    private String color;

    /**
     * Returns the character of the cell.
     * @return The character of the cell.
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Sets the character of the cell.
     * @param character The character to set.
     */
    public void setCharacter(char character) {
        this.character = character;
    }

    /**
     * Returns the card of the cell.
     * @return The card of the cell.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the card of the cell.
     * @param card The card to set.
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Returns the color of the cell.
     * @return The color of the cell.
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the cell.
     * @param color The color to set.
     */
    public void setColor(String color) {
        this.color = color;
    }
}