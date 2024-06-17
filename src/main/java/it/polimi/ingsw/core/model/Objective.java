package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.ui.AnsiColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an Objective in the game.
 * It extends the Card class and implements Serializable interface.
 * It maintains the points, type, completion status, and other related data.
 */
public class Objective extends Card implements Serializable {
    private int points;
    private String type;
    private boolean isCompleted;
    private int completed; //number of times the objective has been completed
    private List<Integer> IDusati = new ArrayList<>();  //list of cards already used to complete an objective
    private String pattern;

    /**
     * Increments the number of times the objective has been completed.
     * @return The number of times the objective has been completed.
     */
    public int setCompleted() {
        completed++;
        return completed;
    }

    /**
     * Returns the type of the objective.
     * @return The type of the objective.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the objective.
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Resets the number of times the objective has been completed.
     */
    public void resetCompleted() {
        completed = 0;
    }

    /**
     * Returns the list of cards already used to complete an objective.
     * @return The list of cards already used to complete an objective.
     */
    protected List<Integer> getIDusati() {
        return IDusati;
    }

    /**
     * Adds a card to the list of cards already used to complete an objective.
     * @param id The id of the card to add.
     */
    protected void addIDusato(Integer id) {
        IDusati.add(id);
    }

    /**
     * Returns the number of times the objective has been completed.
     * @return The number of times the objective has been completed.
     */
    public int getCompleted() {
        return completed;
    }

    /**
     * Returns the completion status of the objective.
     * @return The completion status of the objective.
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Returns the points of the objective.
     * @return The points of the objective.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the points of the objective.
     * @param points The points to set.
     */
    public void setPoints(int points) {
        this.points = points;
    }
}