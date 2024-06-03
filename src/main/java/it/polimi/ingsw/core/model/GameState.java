package it.polimi.ingsw.core.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.utils.PlayableCardIds;

import java.lang.reflect.Type;
import java.util.*;


public class GameState implements java.io.Serializable {

    private Map<Player, PlayerState> playerStates; // map of players and their states
    private List<String> playerOrder; // list of players in the order they will play
    private Deck starterDeck; // deck of starter cards
    private Deck resourceDeck; // deck of resource cards
    private Deck goldDeck; // deck of gold cards
    private Deck objectiveDeck; // deck of objective cards
    private List<Color> colors = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE));

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
        this.objectiveDeck = new Deck("objective", new TypeToken<List<Objective>>() {}.getType());
        playerStates = new HashMap<>();
    }

    public void initializePawn(){
        for (Player player : playerStates.keySet()) {
            //extract a random color from the colors list and assign it to the player
            playerStates.get(player).setPawn(colors.remove(new Random().nextInt(colors.size())));
        }
    }

    public PlayerState getPlayerState(String username) {
        for (Player player : playerStates.keySet()) {
            if (player.getUsername().equals(username)) {
                return playerStates.get(player);
            }
        }
        return null;
    }

    public Map<Player, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public List<String> getPlayerOrder() {
        return playerOrder;
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

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
    }

    public void setResourceDeck(Deck deck) {
        this.resourceDeck = deck;
    }

    public void setGoldDeck(Deck deck) {
        this.goldDeck = deck;
    }

    public synchronized void addPlayer(Player player) {
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
        Gson gson = new GsonBuilder().registerTypeAdapter(Objective.class, new ObjectiveCardDeserializer()).create();
        this.objectiveDeck.loadCardsFromJSON(gson);
        this.objectiveDeckCopy.addAll(this.objectiveDeck.getCards());
        System.out.println("Objective deck loaded");
    }

    public void addCommonObjective(Objective objective) {
        this.commonObj.add(objective);
    }

    public void initializeMatrixPlayers(int matrixDimension) {
        for (Player player : playerStates.keySet()) {
            playerStates.get(player).initializeMatrix(matrixDimension);
        }
    }

    public void initializeChat() {
        for (Player player : playerStates.keySet()) {
            playerStates.get(player).initializeChat();
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

        // shuffle decks
        shuffleDecks();

        // load the gold cards visible on the table
        // TODO: implement for loop to draw n cards. Define n as class variable
        goldCardsVisible.add((Card) goldDeck.drawCard());
        goldCardsVisible.add((Card) goldDeck.drawCard());

        // load the resource cards visible on the table
        // TODO: implement for loop to draw n cards. Define n as class variable
        resourceCardsVisible.add((Card) resourceDeck.drawCard());
        resourceCardsVisible.add((Card) resourceDeck.drawCard());
    }

    public void shuffleDecks() {
        this.starterDeck.shuffle();
        this.resourceDeck.shuffle();
        this.goldDeck.shuffle();
        this.objectiveDeck.shuffle();
    }

    public PlayerState getPlayerState(int index) {
        ArrayList<PlayerState> playerStatesList = new ArrayList<>(playerStates.values());
        return playerStatesList.get(index);
    }

    public void assignStarterCardToPlayers() {
        for (Player player : playerStates.keySet()) {
            PlayerState ps = playerStates.get(player);
            if (!starterDeck.getCards().isEmpty()) {
                CardGame card = starterDeck.drawCard();
                ResourceCard rc = (ResourceCard) card;
                // TODO: change 10 to matrix dimension
                rc.setXYCord(81/2, 81/2);
                ps.setStarterCard(rc);
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
            // TODO: implement for loop to draw n cards. Define n as class variable
            for (int i = 0; i < 2; i++) {
                if (!resourceDeck.getCards().isEmpty()) {
                    Card card = (Card) resourceDeck.drawCard();
                    ps.addCardToHand(card);
                }
            }
            if (!goldDeck.getCards().isEmpty()) {
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

    public List<Objective> getCommonObjectives() {
        return this.commonObj;
    }

    public void setCommonObjective(List<Objective> commonObj) {
        this.commonObj.clear();
        try{
            this.commonObj.add(commonObj.get(0));
            this.commonObj.add(commonObj.get(1));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: common objectives not set");
        }
    }


    public Objective getCommonObjective(int index) {
        try {
            return commonObj.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Deck getObjectiveDeck() {
        return objectiveDeck;
    }

    public void setSecretObjective(String username, Objective cardSelected) {
        getPlayerState(username).setSecretObj(cardSelected);
    }

    public void assignStarterSide(String username, boolean cardSelected) {
        getPlayerState(username).setStarterSide(cardSelected);
    }

    public void placeStarter(int cardWidth, int cardHeight, int matrixDimension) {
        Coordinate leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5,matrixDimension / 2 * cardHeight - 5);
        for (Player player : playerStates.keySet()) {
           Card extractedStarterCard = playerStates.get(player).getStarterCard();
            ((Card) extractedStarterCard).setCentre(leftUpCorner);
            // set the x and y matrix coordinates of the card
            ((Card) extractedStarterCard).setXYCord(matrixDimension / 2, matrixDimension / 2);
            playerStates.get(player).getMatrix()[((Card) extractedStarterCard).getyMatrixCord()][((Card) extractedStarterCard).getxMatrixCord()] = extractedStarterCard.getId();
        }
    }

    public void placeStarterInMatrix(String username, int matrixDimension) {
        PlayerState playerState = getPlayerState(username);
        Card extractedStarterCard = playerState.getStarterCard();
        int x = matrixDimension / 2;
        int y = matrixDimension / 2;
        playerState.addCardToMatrix(x, y, extractedStarterCard.getId());
    }

    public void orderPlayers() {
        playerOrder = new ArrayList<>();
        for (Player player : playerStates.keySet()) {
            playerOrder.add(player.getUsername());
        }
        // shuffle player order
        Collections.shuffle(playerOrder);
    }

    public boolean isOver() {
        for (Player player : playerStates.keySet()) {
            if (playerStates.get(player).getScore() >= 20) {
                return true;
            }
        }
        return false;
    }

    public PlayableCardIds getPlayableCardIdsFromHand(String username) {
        List<Integer> playingHandIds = new ArrayList<>();
        List<Integer> playingHandIdsBack = new ArrayList<>();

        boolean playable;
        PlayerState playerState = getPlayerState(username);

        for (Card c : playerState.getHand()) {
            playable = true;
            if (c instanceof GoldCard x) {
                for (Requirement r : x.getRequirements()) {
                    if (playerState.getPersonalResources().get(r.getResource()) < r.getQta()) {
                        playable = false;
                    }
                }
            }

            if (playable) {
                playingHandIds.add(c.getId());
            } else
                playingHandIdsBack.add(c.getId());
        }

        return new PlayableCardIds(playingHandIds, playingHandIdsBack);
    }

    public Map<Resource, Integer> calculateResource(String us) {
        return getPlayerState(us).calculateResources();
    }

    public void removeResourceCardVisible(Card extractedCard) {
        this.resourceCardsVisible.remove(extractedCard);
    }

    public void removeGoldCardVisible(Card extractedCard) {
        this.goldCardsVisible.remove(extractedCard);
    }
}
