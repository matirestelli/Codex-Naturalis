package it.polimi.ingsw.core.model;

import it.polimi.ingsw.clientmodel.Cell;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.clientmodel.*;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerState implements Serializable {
    private int score;
    private ResourceCard starterCard;
    private List<Card> hand;

    private Cell[][] board;
    private List<Card> codex;
    private int[][] matrix;
    private Card secretObj;
    private Map<Resource, Integer> personalResources;

    public PlayerState() {
        this.score = 0;
        this.hand = new ArrayList<>();
        this.codex = new ArrayList<>();
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

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
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

    public void initializeBoard(int matrixDimension, int cardWidth, int cardHeight) {
        this.board = new Cell[matrixDimension * cardHeight][matrixDimension * cardWidth];
        for (int i = 0; i < matrixDimension * cardHeight; i++)
            for (int j = 0; j < matrixDimension * cardWidth; j++) {
                board[i][j] = new Cell();
                this.board[i][j].setCharacter(' ');
            }
    }

    public void initializeMatrix(int matrixDimension) {
        this.matrix = new int[matrixDimension][matrixDimension];

        for (int i = 0; i < this.matrix.length; i++)
            for (int j = 0; j < this.matrix.length; j++)
                matrix[i][j] = -1;
    }

    public void setSecretObjective(Objective secretObjective) {
        this.secretObj= secretObjective;
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

    public Card getSecretObj() {
        return secretObj;
    }

    public void setSecretObj(Card secretObj) {
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
    }

