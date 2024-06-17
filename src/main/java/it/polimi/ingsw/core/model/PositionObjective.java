package it.polimi.ingsw.core.model;

/**
 * This abstract class represents a PositionObjective in the game.
 * It extends the Objective class.
 * It maintains the methods related to the position of the objective.
 */
public abstract class PositionObjective extends Objective {

    /**
     * This method is used to get a card from a player's codex by its id.
     * @param p The player state.
     * @param id The id of the card.
     * @return The card with the given id, or null if no such card exists.
     */
    public Card getCard(PlayerState p, int id) {
        Card cardSearched = p.getCodex().stream().filter(card -> card.getId() == id).findFirst().orElse(null);
        return cardSearched;
    }
}