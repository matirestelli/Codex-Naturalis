package it.polimi.ingsw.core.model;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState implements java.io.Serializable {
    private Map<Player, PlayerState> playerStates;
    private Deck starterDeck;
    private Deck resourceDeck;
    private Deck goldDeck;

    private Objective[] commonObj;

    public GameState() {
        this.starterDeck = new Deck("starter", new TypeToken<List<ResourceCard>>() {}.getType());
        this.resourceDeck = new Deck("resource", new TypeToken<List<ResourceCard>>() {}.getType());
        this.goldDeck = new Deck("gold", new TypeToken<List<GoldCard>>() {}.getType());
        playerStates = new HashMap<>();
    }

    public int getPlayerId(String username) {
        // array list of playersState keys
        ArrayList<Player> playerStatesList = new ArrayList<>(playerStates.keySet());
        for (int i = 0; i < playerStatesList.size(); i++) {
            if (playerStatesList.get(i).getUsername().equals(username)) {
                return i;
            }
        }

        return -1;
    }

    public List<Card> getStarterDeck() {
        return this.starterDeck.getCards();
    }

    public void setStarterDeck(Deck deck) {
        this.starterDeck = deck;
    }

    public void setStarterCards(List<Card> cards) {
        this.starterDeck.setCards(cards);
    }

    public List<Card> getResourceDeck() {
        return this.resourceDeck.getCards();
    }

    public List<Card> getGoldDeck() {
        return this.goldDeck.getCards();
    }

    public void addPlayer(Player player) {
        playerStates.put(player, new PlayerState());
    }

    public void initializeStarterDeck() {
        this.starterDeck.loadCardsFromJSON();
        System.out.println("Starter deck loaded");
    }

    public void initializeResourceDeck() {
        this.resourceDeck.loadCardsFromJSON();
        System.out.println("Resource deck loaded");
    }

    public void initializeGoldDeck() {
        this.goldDeck.loadCardsFromJSON();
        System.out.println("Gold deck loaded");
    }

    public void loadDecks() {
        initializeStarterDeck();
        initializeResourceDeck();
        initializeGoldDeck();
    }

    public void shuffleDecks() {
        this.starterDeck.shuffle();
        this.resourceDeck.shuffle();
        this.goldDeck.shuffle();
    }

    public PlayerState getPlayerState(int index) {
        ArrayList<PlayerState> playerStatesList = new ArrayList<>(playerStates.values());
        return playerStatesList.get(index);
    }

    public void assignStarterCardToPlayers() {
        for (Player player : playerStates.keySet()) {
            PlayerState ps = playerStates.get(player);
            if (!starterDeck.isEmpty()) {
                Card card = starterDeck.drawCard();
                ps.setStarterCard((ResourceCard) card);
                ps.addCardToCodex(card);
            }
        }
    }

    public List<Card> getPlayerHand(String username) {
        for (Player player : playerStates.keySet()) {
            if (player.getUsername().equals(username)) {
                return playerStates.get(player).getHand();
            }
        }
        return null;
    }

    public void assignFirstHandToPlayers() {
        for (Player player : playerStates.keySet()) {
            PlayerState ps = playerStates.get(player);
            for (int i = 0; i < 2; i++) {
                if (!resourceDeck.isEmpty()) {
                    Card card = resourceDeck.drawCard();
                    ps.addCardToHand(card);
                }
            }
            if (!goldDeck.isEmpty()) {
                Card card = goldDeck.drawCard();
                ps.addCardToHand(card);
            }
        }
    }

    public void setPlayerHand(String username, List<Card> hand) {
        for (Player player : playerStates.keySet()) {
            if (player.getUsername().equals(username)) {
                playerStates.get(player).setHand(hand);
            }
        }
    }

    public void setCommonObjective(Objective[] commonObj) {
        this.commonObj = commonObj;
    }
    public Objective getCommonObjective(int index) {
        return commonObj[index];
    }
}
