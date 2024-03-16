package it.polimi.ingsw.gc38.model;

public class Cell {
    char character;
    Card card;
    String color;

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