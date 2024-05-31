package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PositionObjectiveTest {
    private PositionObjective positionObjective;
    private PlayerState player;
    private List<Card> codex;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        player = new PlayerState();
        codex = new ArrayList<>();
        card1 = new ResourceCard();
        card1.setId(1);
        card2 = new ResourceCard();
        card2.setId(2);
        codex.add(card1);
        codex.add(card2);
        player.setCodex(codex);
        positionObjective = new PositionObjective() {}; // Create an anonymous class because PositionObjective is abstract
    }

    @Test
    void testGetCard() {
        assertEquals(card1, positionObjective.getCard(player, 1));
        assertEquals(card2, positionObjective.getCard(player, 2));
        assertNull(positionObjective.getCard(player, 3));
    }
}