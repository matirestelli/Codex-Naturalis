package it.polimi.ingsw.gc38.model;

import java.util.List;
import java.util.Map;

public class Player {
    private String nickname;
    private int score;
    private List<Card> playingHand;
    private List<Card> codex;
    private int[][] matrix;
    private Cell[][] board;
    private Card secretObjective;
    private Map<Resource, Integer> personalResources;
    private boolean firstPlayer;
    private boolean turn;
    private Color pawn;
    private boolean deadlock;

    public Player() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public List<Card> getPlayingHand() {
        return playingHand;
    }

    public void setPlayingHand(List<Card> playingHand) {
        this.playingHand = playingHand;
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

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public Card getSecretObjective() {
        return secretObjective;
    }

    public void setSecretObjective(Card secretObjective) {
        this.secretObjective = secretObjective;
    }

    public Map<Resource, Integer> getPersonalResources() {
        return personalResources;
    }

    public void setPersonalResources(Map<Resource, Integer> personalResources) {
        this.personalResources = personalResources;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public Color getPawn() {
        return pawn;
    }

    public void setPawn(Color pawn) {
        this.pawn = pawn;
    }

    public boolean isDeadlock() {
        return deadlock;
    }

    public void setDeadlock(boolean deadlock) {
        this.deadlock = deadlock;
    }

    public void initializeMatrix() {
        int matrixDimension = 20;
        this.matrix = new int[matrixDimension][matrixDimension];

        for (int i = 0; i < this.matrix.length; i++)
            for (int j = 0; j < this.matrix.length; j++)
                matrix[i][j] = -1;
    }

    public void initializeBoard() {
        int matrixDimension = 20;
        this.board = new Cell[matrixDimension * 3][matrixDimension * 7];
        for (int i = 0; i < matrixDimension*3; i++)
            for (int j = 0; j < matrixDimension*7; j++) {
                board[i][j] = new Cell();
                this.board[i][j].setCharacter(' ');
            }
    }
}