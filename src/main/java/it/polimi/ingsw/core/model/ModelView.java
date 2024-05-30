package it.polimi.ingsw.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;

public class ModelView {
    private List<String> players;
    private Map<String, Integer> playerOrder;
    private Map<String, ViewModelPlayerState> playerStates ;
    private Card deckGBack;
    private Card deckRBack;
    private List<Card> resourceCardsVisible;
    private List<Card> goldCardsVisible;
    private List<Objective> commonObj;
    private Objective secretObj;
    private ResourceCard myStarterCard;
    private List<Card> myHand;
    private List<Card> myCodex;
    private String myUsername;
    private int myUnreadedMessages;
    private boolean myTurn;
    private Color myColor;
    private int myScore;
    private int[][] myMatrix;
    private Map<Resource, Integer> myResources;
    private Chat chat;
    private Card playingCard;

    public ModelView() {
        this.myUnreadedMessages = 0;
        this.myTurn = false;
        this.playerStates = new HashMap<>();
        this.resourceCardsVisible = new ArrayList<>();
        this.goldCardsVisible = new ArrayList<>();
        this.commonObj = new ArrayList<>();
        this.myHand = new ArrayList<>();
        this.myCodex = new ArrayList<>();
        this.myResources = new HashMap<>();
        this.myMatrix = new int[81][81];
        this.players = new ArrayList<>();
        this.playerOrder = new HashMap<>();
        this.myScore = 0;
    }

    public void setMyPlayingCard(Card playingCard) {
        this.playingCard = playingCard;
    }

    public Card getMyPlayingCard() {
        return playingCard;
    }

    public Chat getChat() {
        return chat;
    }

    public int getMyUnreadedMessages() {
        return myUnreadedMessages;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyMatrix(int[][] myMatrix) {
        this.myMatrix = myMatrix;
    }

    public int[][] getMyMatrix() {
        return myMatrix;
    }
    public void setSecretObj(Objective privateObj) {
        this.secretObj = privateObj;
    }

    public Objective getSecretObj() {
        return secretObj;
    }

    public ResourceCard getMyStarterCard() {
        return myStarterCard;
    }

    public void setMyStarterCard(ResourceCard myStarterCard) {
        this.myStarterCard = myStarterCard;
    }

    public List<Card> getMyHand() {
        return myHand;
    }

    public void setMyHand(List<Card> myHand) {

        this.myHand = myHand;
        for(Card c : myHand){
            c.setSide(true);
        }
    }

    public List<Card> getMyCodex() {
        return myCodex;
    }

    public void setMyCodex(List<Card> myCodex) {
        this.myCodex = myCodex;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    public Color getMyColor() {
        return myColor;
    }

    public void setMyColor(Color myColor) {
        this.myColor = myColor;
    }

    public int getMyScore() {
        return myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    public Map<Resource, Integer> getMyResources() {
        return myResources;
    }

    public void setMyResources(Map<Resource, Integer> myResources) {
        this.myResources = myResources;
    }

    public void addCardToCodex(Card card){
        myCodex.add(card);
    }

    //lista che contiene la chat di gioco broadcast rappresentata come una lista di array con chiave lo username del giocatore e valore il messaggio
    private List<String[]> broadcastChat = new ArrayList<>();

    public Map<String, ViewModelPlayerState> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(Map<String, ViewModelPlayerState> playerStates) {
        this.playerStates = playerStates;
    }
    public void createPlayerStates(List<String> usernames){
        for(String username : usernames){
            playerStates.put(username, new ViewModelPlayerState());
        }
    }

    public void setStateOfPlayer(String username, ViewModelPlayerState state){
        //it over writes the previous state of the player
        playerStates.put(username, state);
    }

    public Card getDeckGBack() {
        return deckGBack;
    }

    public void setDeckGBack(Card deckGBack) {
        this.deckGBack = deckGBack;
    }

    public Card getDeckRBack() {
        return deckRBack;
    }

    public void setDeckRBack(Card deckRBack) {
        this.deckRBack = deckRBack;
    }

    public List<Card> getResourceCardsVisible() {
        return resourceCardsVisible;
    }

    public void setResourceCardsVisible(List<Card> resourceCardsVisible) {
        this.resourceCardsVisible = resourceCardsVisible;
    }

    public List<Card> getGoldCardsVisible() {
        return goldCardsVisible;
    }

    public void setGoldCardsVisible(List<Card> goldCardsVisible) {
        this.goldCardsVisible = goldCardsVisible;
    }

    public List<Objective> getCommonObj() {
        return commonObj;
    }

    public void setCommonObj(List<Objective> commonObj) {
        this.commonObj = commonObj;
    }

    public List<String[]> getBroadcastChat() {
        return broadcastChat;
    }

    public void setBroadcastChat(List<String[]> broadcastChat) {
        this.broadcastChat = broadcastChat;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {

        this.players = players;
        for(int i = 0; i < players.size(); i++){
            playerOrder.put(players.get(i), i);
        }
    }

    public Map<String, Integer> getPlayerOrder() {
        return playerOrder;
    }
}