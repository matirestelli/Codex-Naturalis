package it.polimi.ingsw.gc38.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;

public class Deck {
    private List<CardGame> cards;
    private String configFile;
    private boolean empty;

    public Deck(String configFile) {
        this.configFile = "src/main/resources/it/polimi/ingsw/gc38/" + configFile + ".json";
        this.cards = new ArrayList<>();
    }

    public void setCards(List<CardGame> cards) {
        this.cards = cards;
    }

    public List<CardGame> getCards() {
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
        Collections.shuffle(this.cards);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
    }

    public void loadCardsFromJSON(Type typeOfCard, Gson gson) {
        List<CardGame> cards = new ArrayList<>();
        try {
            cards = gson.fromJson(new FileReader(this.configFile),
                    typeOfCard);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.cards = cards;
    }

    public CardGame extractCard() {
        CardGame card = this.cards.getFirst();
        this.cards.removeFirst();
        return card;
    }
}
