package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SxDiagonalObjectiveTest {
    private SxDiagonalObjective sxDiagonalObjective;
    private PlayerState player;
    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    void setUp() {
        sxDiagonalObjective = new SxDiagonalObjective();
        List<Card> codex = new ArrayList<>();
        sxDiagonalObjective.setPoints(3); // Assuming the objective's points is set to 1
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        card1 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card2 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card3 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card4 = new ResourceCard();
        Card card5 = new ResourceCard();
        Card card6 = new ResourceCard();
        card1.setId(2);
        card2.setId(4);
        card3.setId(6);
        card4.setId(8);
        card5.setId(10);
        card6.setId(0);
        player.setCodex(codex);
        player.addCardToCodex(card1);
        player.addCardToCodex(card2);
        player.addCardToCodex(card3);
        player.addCardToCodex(card4);
        player.addCardToCodex(card5);
        player.addCardToCodex(card6);
        card1.setColor(Color.RED);
        card2.setColor(Color.RED);
        card3.setColor(Color.RED);
        card4.setColor(Color.RED);
        card5.setColor(Color.RED);
        card6.setColor(Color.RED);
        sxDiagonalObjective.setColor(Color.RED);
    }
    @Test
    void testGetType() {
        assertEquals("SxDiagonal", sxDiagonalObjective.getType());
    }

    @Test
    void testCalculatePoints() {
        player.setMatrix(new int[][] {
                {  0, -1, -1, -1, -1, 12},
                { -1,  2, -1, -1, 10, -1},
                { -1, -1,  4,  8, -1, -1},
                { -1, -1,  6,  6, -1,  9},
                { -1,  4, -1, -1,  8, -1},
                {  2, -1, -1,  5, -1, 10},
                { -1, -1, -1, -1, -1, -1}
        });
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(6, player.getScore()); // Assuming the objective's points is set to 1
    }

    @Test
    void testCalculatePoints2() {
        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                { 90, -1, -1, -1 },
                { -1,  2, -1, -1 },
                { -1, -1,  4, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore()); // Assuming the objective's points is set to 1

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                {  6, -1, -1, -1 },
                { -1, 90, -1, -1 },
                { -1, -1,  4, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                {  6, -1, -1, -1 },
                { -1,  2, -1, -1 },
                { -1, -1, 90, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                {  6, -1, -1, -1 },
                { -1,  2, -1, -1 },
                { -1, -1,  4, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });
        card1.setColor(Color.PURPLE);
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        card1.setColor(Color.RED);
        card2.setColor(Color.PURPLE);
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        card2.setColor(Color.RED);
        card3.setColor(Color.PURPLE);
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        card3.setColor(Color.RED);

        sxDiagonalObjective.addIDusato(2);
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        sxDiagonalObjective.getIDusati().clear();

        sxDiagonalObjective.addIDusato(4);
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        sxDiagonalObjective.getIDusati().clear();

        sxDiagonalObjective.addIDusato(6);
        sxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

    }
}