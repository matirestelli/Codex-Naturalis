package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.chat.*;
import it.polimi.ingsw.network.ClientAbstract;
import it.polimi.ingsw.ui.TextUserInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a PlayerState in the game.
 * It extends the Player class and implements Serializable interface.
 * It maintains the score, starterCard, hand, chat, pawn, board, cardWidth, cardHeight, matrixDimension, codex, matrix, secretObj, personalResources, connected, lastPingTime and other related data.
 */
public class PlayerState extends Player implements Serializable {
    private int score;
    private ResourceCard starterCard;
    private List<Card> hand;
    private Chat chat;
    private Color pawn;
    private Cell[][] board;
    private int cardWidth = 7;
    private int cardHeight = 3;
    private int matrixDimension = 81;

    private List<Card> codex;
    private int[][] matrix;
    private Objective secretObj;
    private Map<Resource, Integer> personalResources;
    private boolean connected;
    private long lastPingTime;

    public PlayerState() {
        this.score = 0;
        this.hand = new ArrayList<>();
        this.codex = new ArrayList<>();
        this.personalResources = new HashMap<>();
        this.chat = new Chat();
        this.board = new Cell[matrixDimension * cardHeight][matrixDimension * cardWidth];
        for (int i = 0; i < matrixDimension * cardHeight; i++)
            for (int j = 0; j < matrixDimension * cardWidth; j++) {
                board[i][j] = new Cell();
                this.board[i][j].setCharacter(' ');
            }
    }

    public void setPawn(Color pawn) {
        this.pawn = pawn;
    }

    public Color getPawn() {
        return pawn;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public Chat getChat(){
        return chat;
    }

    public String getUsername() {
        return super.getUsername();
    }

    public void setStarterCard(ResourceCard starterCard) {
        this.starterCard = starterCard;
    }

    public ResourceCard getStarterCard() {
        return starterCard;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    public void addCardToCodex(Card card) {
        codex.add(card);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Card getCardFromHand(int id) {
        for (Card card : hand) {
            if (card.getId() == id) {
                return card;
            }
        }
        return null;
    }

    public void initializeMatrix(int matrixDimension) {
        this.matrix = new int[matrixDimension][matrixDimension];

        for (int i = 0; i < this.matrix.length; i++)
            for (int j = 0; j < this.matrix.length; j++)
                matrix[i][j] = -1;
    }

    public void initializeChat(){
        chat=new Chat();
    }

    public void setPersonalResources(Map<Resource, Integer> personalResources) {
        this.personalResources = personalResources;
    }

    public Map<Resource, Integer> getPersonalResources() {
        return personalResources;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public List<Card> getCodex() {
        return codex;
    }

    public void setCodex(List<Card> codex) {
        this.codex = codex;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public Objective getSecretObj() {
        return secretObj;
    }

    public void setSecretObj(Objective secretObj) {
        this.secretObj = secretObj;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public Map<Resource, Integer> calculateResources(){
        personalResources = new HashMap<Resource, Integer>();

        for (Resource res : Resource.values()) {
            personalResources.put(res, 0);
        }

        for (Card c : this.codex) {
            if (c.isFrontSide()) {
                for (int i = 0; i < 4; i++) {
                    if (c.getFrontCorners().containsKey(i) && !c.getFrontCorners().get(i).isEmpty()) {
                        personalResources.put(c.getFrontCorners().get(i).getResource(), personalResources.get(c.getFrontCorners().get(i).getResource()) + 1);
                    }
                }
            } else {
                for (Resource res : c.getBackResources()) {
                    personalResources.put(res, personalResources.get(res) + 1);
                }
            }
        }

        return personalResources;
    }

    public void setStarterSide(boolean side) {
        starterCard.setSide(side);
    }

    public void addCardToMatrix(int x, int y, int id) {
        matrix[y][x] = id;
    }

}