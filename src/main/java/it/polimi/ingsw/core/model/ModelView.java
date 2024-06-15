package it.polimi.ingsw.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;

public class ModelView {
    private List<String> players;
    private Map<String, Integer> playerOrder;
    private Map<String, ViewModelPlayerState> playerStates ;
    private Map<String, Color> playerPawns;
    private Map<String, Cell[][]> playerBoards;
    private Card deckGBack;
    private Map<String,String> boardToPrint;
    private Card deckRBack;
    private List<Card> resourceCardsVisible;
    private List<Card> goldCardsVisible;
    private List<Objective> commonObj;
    private Objective secretObj;
    private ResourceCard myStarterCard;
    private List<Card> myHand;
    private List<Card> myCodex;
    private String myUsername;
    private int cardWidth = 7;
    private int cardHeight = 3;
    private int matrixDimension = 81;
    private int myUnreadedMessages;
    private boolean myTurn;
    private int myScore;
    private int[][] myMatrix;
    private Map<Resource, Integer> myResources;
    private Chat chat;
    private Card playingCard;


    public ModelView() {
        this.myUnreadedMessages = 0;
        this.myTurn = false;
        this.boardToPrint = new HashMap<>();
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
        this.chat = new Chat();
        this.myScore = 0;
        this.playerBoards = new HashMap<>();
    }

    public void setMyPlayingCard(Card playingCard, Boolean side) {
        this.playingCard = playingCard;
        this.playingCard.setSide(side);
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

    public void setMyUnreadedMessages(int myUnreadedMessages) {
        this.myUnreadedMessages = myUnreadedMessages;
    }

    public void addUnreadedMessage() {
        this.myUnreadedMessages++;
    }

    public void setPlayerBoards(Map<String, Cell[][]> playerBoards) {
        this.playerBoards = playerBoards;
    }

    public void setBoardToPrint(Map<String, String> boardToPrint) {
        this.boardToPrint = boardToPrint;
    }

    public Map<String, String> getBoardToPrint() {
        return boardToPrint;
    }

    public void setBoardToPrintByUsername(String username, String board) {
        this.boardToPrint.put(username, board);
    }

    public void initializePlayerBoards(){
        for(String player : players){
            Cell[][] board = new Cell[matrixDimension * cardHeight][matrixDimension * cardWidth];
            for(int i = 0; i < matrixDimension * cardHeight; i++){
                for(int j = 0; j < matrixDimension * cardWidth; j++){
                    board[i][j] = new Cell();
                    board[i][j].setCharacter(' ');
                }
            }
            playerBoards.put(player, board);
        }
    }

    public Map<String, Cell[][]> getPlayerBoards() {
        if(playerBoards == null){
            playerBoards = new HashMap<>();
            for(String player : players){
                Cell[][] board = new Cell[matrixDimension * cardHeight][matrixDimension * cardWidth];
                for(int i = 0; i < matrixDimension * cardHeight; i++){
                    for(int j = 0; j < matrixDimension * cardWidth; j++){
                        board[i][j] = new Cell();
                        board[i][j].setCharacter(' ');
                    }
                }
                playerBoards.put(player, board);
            }
        }
        return playerBoards;
    }

    Cell getCell(int x, int y){
        return playerBoards.get(myUsername)[x][y];
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

    public void putInMyMatrix(int x, int y, int card){
        myMatrix[x][y] = card;
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
        this.createPlayerStates(players);
    }

    public Map<String, Integer> getPlayerOrder() {
        return playerOrder;
    }

    public Map<String, Color> getPlayerPawns() {
        return playerPawns;
    }

    public void setPlayerPawns(Map<String, Color> playerPawns) {
        this.playerPawns = playerPawns;
    }
}