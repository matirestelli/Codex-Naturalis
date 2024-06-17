package it.polimi.ingsw.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a selected card to attach in the game.
 * It implements Serializable interface.
 * It maintains the card and codex of the selected card.
 */
public class CardToAttachSelected implements Serializable {
    private List<Card> codex;
    private String card;

    /**
     * Constructs a CardToAttachSelected with the given card and codex.
     * @param card The card to attach.
     * @param codex The codex of the card.
     */
    public CardToAttachSelected(String card, List<Card> codex) {
        this.card = card;
        this.codex = codex;
    }

    /**
     * Returns the card to attach.
     * @return The card to attach.
     */
    public String getString() {
        return card;
    }

    /**
     * Returns the codex of the card.
     * @return The codex of the card.
     */
    public List<Card> getCodex() {
        return codex;
    }
}