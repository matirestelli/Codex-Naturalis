package it.polimi.ingsw.core.model;

import java.io.Serializable;

public class CardToAttachSelected implements Serializable {

    private String card;

    public CardToAttachSelected(String card) {
        this.card = card;
    }

    public String getString() {
        return card;
    }
}
