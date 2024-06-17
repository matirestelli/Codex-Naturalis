package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a ViewModelPlayerState in the game.
 * It implements Serializable interface.
 * It maintains the score, codex and personal resources of the player state.
 */
public class ViewModelPlayerState implements Serializable {
    private int score;
    private List<Card> codex;
    private Map<Resource, Integer> personalResources;

    /**
     * Constructor for the ViewModelPlayerState class.
     */
    public ViewModelPlayerState() {
        this.score = 0;
        this.codex = new ArrayList<>();
        this.personalResources = new HashMap<>();
    }

    /**
     * Returns the score of the player state.
     * @return The score of the player state.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the player state.
     * @param score The score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the codex of the player state.
     * @return The codex of the player state.
     */
    public List<Card> getCodex() {
        return codex;
    }

    /**
     * Sets the codex of the player state.
     * @param codex The codex to set.
     */
    public void setCodex(List<Card> codex) {
        this.codex = codex;
    }

    /**
     * Returns the personal resources of the player state.
     * @return The personal resources of the player state.
     */
    public Map<Resource, Integer> getPersonalResources() {
        return personalResources;
    }

    /**
     * Sets the personal resources of the player state.
     * @param personalResources The personal resources to set.
     */
    public void setPersonalResources(Map<Resource, Integer> personalResources) {
        this.personalResources = personalResources;
    }
}