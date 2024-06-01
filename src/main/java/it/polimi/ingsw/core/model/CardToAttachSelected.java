package it.polimi.ingsw.core.model;

import java.io.Serializable;
import java.util.List;

public class CardToAttachSelected implements Serializable {
    private List<Card> codex;
    private String card;

    public CardToAttachSelected(String card, List<Card> codex) {
        this.card = card;
        this.codex = codex;
    }

    public String getString() {
        return card;
    }

    public List<Card> getCodex() {
        return codex;
    }
}
