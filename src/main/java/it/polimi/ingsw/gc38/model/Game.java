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

    public void setResourceDeck(Deck resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }

}

