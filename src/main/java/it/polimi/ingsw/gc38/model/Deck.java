package it.polimi.ingsw.gc38.model;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<CardGame> cards;
    private final String configFile;
    private boolean empty;

    public Deck(String configFile) {
        this.configFile = "src/main/resources/it/polimi/ingsw/gc38/" + configFile + ".json";
        this.cards = new ArrayList<>();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
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
