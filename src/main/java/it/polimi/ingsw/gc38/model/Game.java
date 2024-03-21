package it.polimi.ingsw.gc38.model;
import java.util.List;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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
        Type resourceCardListType = new TypeToken<List<ResourceCard>>() {}.getType();
        this.resourceDeck = new Deck("resourceCards");
        this.resourceDeck.loadCardsFromJSON(resourceCardListType);
    }

    public void initializeStarterDeck() {
        Type resourceCardListType = new TypeToken<List<ResourceCard>>() {}.getType();
        this.starterDeck = new Deck("starterCards");
        this.starterDeck.loadCardsFromJSON(resourceCardListType);
    }

    public void initializeGoldDeck() {
        Type goldCardListType = new TypeToken<List<GoldCard>>() {}.getType();
        this.goldDeck = new Deck("goldCards");
        this.goldDeck.loadCardsFromJSON(goldCardListType);
    }

    public void initializeDecks() {
        initializeResourceDeck();
        initializeStarterDeck();
        initializeGoldDeck();
    }

    public void shuffleDecks(){
        // shuffle starter deck
        this.starterDeck.shuffle();
        // shuffle resource deck
        this.resourceDeck.shuffle();
    }
}

