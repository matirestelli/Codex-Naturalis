package it.polimi.ingsw.clientmodel;
import  it.polimi.ingsw.core.model.Card;

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