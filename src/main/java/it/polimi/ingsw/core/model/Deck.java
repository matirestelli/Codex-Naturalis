package it.polimi.ingsw.core.model;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck implements Serializable {
    private List<Card> cards;
    private String configFile;
    private Type typeOfCard;
    private boolean empty;

    public Deck(String configFile, Type typeOfCard) {
        this.cards = new ArrayList<>();
        this.configFile = new StringBuilder().append("src/main/resources/it/polimi/ingsw/").append(configFile).append(".json").toString();
        this.typeOfCard = typeOfCard;
        this.empty = true;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public void loadCardsFromJSON() {
        List<Card> cards = new ArrayList<>();
        try {
            Gson gson = new Gson();
            cards = gson.fromJson(new FileReader(this.configFile), this.typeOfCard);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.cards = cards;
        this.empty = cards.isEmpty();
    }

    public void loadCardsFromJSON(Gson gson) {
        List<Card> cards = new ArrayList<>();
        try {
            cards = gson.fromJson(new FileReader(this.configFile), this.typeOfCard);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.cards = cards;
        this.empty = cards.isEmpty();
    }

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public CardGame drawCard() {
        if (this.cards.isEmpty()) {
            return null;
        }

        CardGame card = this.cards.getFirst();
        this.cards.removeFirst();
        this.empty = this.cards.isEmpty();
        return card;
    }
}
