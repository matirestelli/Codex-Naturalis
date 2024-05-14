package it.polimi.ingsw.core.model;

//import java.util.List;
//import java.util.Map;

public abstract class PositionObjective extends Objective {

    public Card getCard(PlayerState p, int id) {
            Card cardSearched = p.getCodex().stream().filter(card -> card.getId() == id).findFirst().orElse(null);
            return cardSearched;
    }
}
