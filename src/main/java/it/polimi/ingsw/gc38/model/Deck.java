package it.polimi.ingsw.gc38.model;

import java.util.Map;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private String configFile ;
    private boolean empty;

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void shuffle() {
    }

    public void drawCard() {
    }

    public void removeCard (int cardId) {

    }
}
