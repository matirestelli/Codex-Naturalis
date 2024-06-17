package it.polimi.ingsw.core.model;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a deck in the game.
 * It implements Serializable interface.
 * It maintains the cards, configFile, typeOfCard, and empty status of the deck.
 */
public class Deck implements Serializable {
    private List<Card> cards;
    private String configFile;
    private Type typeOfCard;
    private boolean empty;

    /**
     * Constructs a Deck with the given configFile and typeOfCard.
     * @param configFile The configFile of the deck.
     * @param typeOfCard The typeOfCard of the deck.
     */
    public Deck(String configFile, Type typeOfCard) {
        this.cards = new ArrayList<>();
        this.configFile = new StringBuilder().append("src/main/resources/it/polimi/ingsw/").append(configFile).append(".json").toString();
        this.typeOfCard = typeOfCard;
        this.empty = true;
    }

    /**
     * Returns the cards of the deck.
     * @return The cards of the deck.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Sets the cards of the deck.
     * @param cards The cards to set.
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Shuffles the cards of the deck.
     */
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
     * Loads the cards from JSON.
     */
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

    /**
     * Loads the cards from JSON with the given Gson.
     * @param gson The Gson to use.
     */
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

    /**
     * Checks if the deck is empty.
     * @return True if the deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    /**
     * Draws a card from the deck.
     * @return The drawn card.
     */
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