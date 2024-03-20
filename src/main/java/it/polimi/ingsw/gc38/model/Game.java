package it.polimi.ingsw.gc38.model;
import java.util.List;
import java.util.Map;
public class Game {
    private String id;
    private int MaxPlayers;
    private Map<Player, Map<Integer, Integer>> players;
    private int numPlayers;
    private List<Board> boards;
    //array di 2 obiettivi comuni
    private Objective[] commonObjectives;
    private Deck goldDeck;
    private Deck resourceDeck;
    private Deck starterDeck;
    private Player winner;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMaxPlayers(int MaxPlayers) {
        this.MaxPlayers = MaxPlayers;
    }

    public int getMaxPlayers() {
        return MaxPlayers;
    }

    public void setPlayers(Map<Player, Map<Integer, Integer>> players) {
        this.players = players;
    }

    public Map<Player, Map<Integer, Integer>> getPlayers() {
        return players;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setCommonObjectives(Objective[] commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    public Objective[] getCommonObjectives() {
        return commonObjectives;
    }

    public void setGoldDeck(Deck goldDeck) {
        this.goldDeck = goldDeck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public Deck getStarterDeck() {
        return starterDeck;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }

    public void initializeResourceDeck() {
        this.resourceDeck = new Deck("resourceCards");
        this.resourceDeck.loadCardsFromJSON();
    }

    public void initializeStarterDeck() {
        this.starterDeck = new Deck("starterCards");
        this.starterDeck.loadCardsFromJSON();
    }

    public void initializeDecks(String configFile) {
        initializeResourceDeck();
        initializeStarterDeck();
    }

    public void initializeDecks() {
        // goldDeck = new Deck();
        resourceDeck = new Deck("resourceCards");
        resourceDeck.loadCardsFromJSON();
        // goldDeck.loadGameCards();
        // resourceDeck = Deck.loadGameCards("src\\main\\resources\\it\\polimi\\ingsw\\gc38\\resourceCards.json");
        // starterDeck.loadGameCards("src\\main\\resources\\it\\polimi\\ingsw\\gc38\\starterCards.json");
    }

}

