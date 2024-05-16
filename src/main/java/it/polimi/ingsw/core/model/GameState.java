package it.polimi.ingsw.core.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GameState implements java.io.Serializable {
    private Map<Player, PlayerState> playerStates;
    private Deck starterDeck;
    private Deck resourceDeck;
    private Deck goldDeck;
    private Deck objectiveDeck;

    private List<Objective> commonObj = new ArrayList<>();

    private List<Card> resourceCardsVisible= new ArrayList<>();
    private List<Card> goldCardsVisible = new ArrayList<>();
    private List<CardGame> objectiveDeckCopy= new ArrayList<>();

    public void addCardToResourceCardsVisible(Card card) {
        this.resourceCardsVisible.add(card);
    }

    public void addCardToGoldCardsVisible(Card card) {
        this.goldCardsVisible.add(card);
    }

    public List<Card> getResourceCardsVisible() {
        return resourceCardsVisible;
    }

    public List<Card> getGoldCardsVisible() {
        return goldCardsVisible;
    }

    public GameState() {
        this.starterDeck = new Deck("starter", new TypeToken<List<ResourceCard>>() {}.getType());
        this.resourceDeck = new Deck("resource", new TypeToken<List<ResourceCard>>() {}.getType());
        this.goldDeck = new Deck("gold", new TypeToken<List<GoldCard>>() {}.getType());
        //this.objectiveDeck = new Deck("objective", new TypeToken<List<Objective>>() {}.getType());
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

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
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


    public void initializeObjectiveDeck() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Objective.class, new ObjectiveCardDeserializer())
                .create();

        Type objectiveCardListType = new TypeToken<List<Objective>>() {}.getType();
        this.objectiveDeck = new Deck("objective",objectiveCardListType);
        this.objectiveDeck.loadCardsFromJSON();
        for(CardGame c: this.objectiveDeck.getCards()){
            this.objectiveDeckCopy.add(c);
        }
        System.out.println("Objective deck loaded");
    }

    public void addCommonObjective(Objective objective) {
        this.commonObj.add(objective);
    }

    public void initializeMatrix(int matrixDimension) {
        for (Player player : playerStates.keySet()) {
            playerStates.get(player).initializeMatrix(matrixDimension);
        }
    }

    public void initializeBoard(int matrixDimension, int cardWidth, int cardHeight) {
        for (Player player : playerStates.keySet()) {
            playerStates.get(player).initializeBoard( matrixDimension,cardWidth, cardHeight);
        }
    }

    public List<CardGame> getObjectiveDeckCopy() {
        return this.objectiveDeckCopy;
    }

    public void loadDecks() {
        initializeStarterDeck();
        initializeResourceDeck();
        initializeGoldDeck();
        initializeObjectiveDeck();
        goldCardsVisible.add((Card) goldDeck.drawCard());
        goldCardsVisible.add((Card) goldDeck.drawCard());
        resourceCardsVisible.add((Card) resourceDeck.drawCard());
        resourceCardsVisible.add((Card) resourceDeck.drawCard());
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
                CardGame card = starterDeck.drawCard();
                ps.setStarterCard((ResourceCard) card);
                ps.addCardToCodex((Card) card);
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
                    Card card = (Card) resourceDeck.drawCard();
                    ps.addCardToHand(card);
                }
            }
            if (!goldDeck.isEmpty()) {
                Card card = (Card) goldDeck.drawCard();
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

    public void setCommonObjective(Objective[] commonObj, int index) {
        this.commonObj.set(index, commonObj[index]);
    }
    public Objective getCommonObjective(int index) {
        return commonObj.get(index);
    }

    public Deck getObjectiveDeck() {
        return objectiveDeck;
    }

    public void setSecretObjective(Objective[] secretObj, int index) {
        for (Player player : playerStates.keySet()) {
            playerStates.get(player).setSecretObjective(secretObj[index]);
        }
    }

    public void placeStarter(Boolean isFront, int cardWidth, int cardHeight, int matrixDimension) {
        Coordinate leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5,matrixDimension / 2 * cardHeight - 5);
        for (Player player : playerStates.keySet()) {
           Card extractedStarterCard = playerStates.get(player).getStarterCard();
            ((Card) extractedStarterCard).setCentre(leftUpCorner);
            // set the x and y matrix coordinates of the card
            ((Card) extractedStarterCard).setXYCord(matrixDimension / 2, matrixDimension / 2);
            playerStates.get(player).getMatrix()[((Card) extractedStarterCard).getyMatrixCord()][((Card) extractedStarterCard).getxMatrixCord()] = extractedStarterCard.getId();
        }
    }
}
