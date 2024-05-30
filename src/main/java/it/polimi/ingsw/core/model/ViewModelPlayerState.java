package it.polimi.ingsw.core.model;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewModelPlayerState implements Serializable {
    private int score;
    private List<Card> codex;
    private Map<Resource, Integer> personalResources;
    private Color color;

    public ViewModelPlayerState() {
        this.score = 0;
        this.codex = new ArrayList<>();
        this.personalResources = new HashMap<>();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Card> getCodex() {
        return codex;
    }

    public void setCodex(List<Card> codex) {
        this.codex = codex;
    }

    public Map<Resource, Integer> getPersonalResources() {
        return personalResources;
    }

    public void setPersonalResources(Map<Resource, Integer> personalResources) {
        this.personalResources = personalResources;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
