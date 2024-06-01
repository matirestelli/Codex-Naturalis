package it.polimi.ingsw.core.model;

public class Cell {
    private char character;
    private Card card;
    private String color;

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card cardId) {
        this.card = cardId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}