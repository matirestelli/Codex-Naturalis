package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DownReverseLObjectiveTest {
    private DownReverseLObjective downReverselObjective;
    private PlayerState player;
    private Card card1;
    private Card card2;
    private Card card4;

    @BeforeEach
    void setUp() {
        downReverselObjective = new DownReverseLObjective();
        List<Card> codex = new ArrayList<>();
        downReverselObjective.setPoints(3); // Assuming the objective's points is set to 1
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        card1 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card2 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card3 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card4 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card5 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card6 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card7 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card8 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card1.setId(2);
        card2.setId(3);
        card3.setId(4);
        card4.setId(5);
        card5.setId(6);
        card6.setId(7);
        card7.setId(8);
        card8.setId(9);
        player.setCodex(codex);
        player.addCardToCodex(card1);
        player.addCardToCodex(card2);
        player.addCardToCodex(card3);
        player.addCardToCodex(card4);
        player.addCardToCodex(card5);
        player.addCardToCodex(card6);
        player.addCardToCodex(card7);
        player.addCardToCodex(card8);
        card1.setColor(Color.GREEN);
        card2.setColor(Color.BLUE);
        card3.setColor(Color.GREEN);
        card4.setColor(Color.BLUE);
        card5.setColor(Color.GREEN);
        card6.setColor(Color.BLUE);
        card7.setColor(Color.BLUE);
        card8.setColor(Color.GREEN);
        downReverselObjective.setColor1(Color.BLUE);
        downReverselObjective.setColor2(Color.GREEN);
    }

    @Test
    void testgetType() {
        assertEquals("DownReverseL", downReverselObjective.getType());
    }

    @Test
    void testSetAndGetColor1() {
        downReverselObjective.setColor1(Color.RED);
        assertEquals(Color.RED, downReverselObjective.getColor1());
    }

    @Test
    void testSetAndGetColor2() {
        downReverselObjective.setColor2(Color.RED);
        assertEquals(Color.RED, downReverselObjective.getColor2());
    }

    @Test
    void testCalculatePoints() {
        player.setMatrix(new int[][] {
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  2, -1, -1, -1, -1, -1},
                { -1, -1,  3, -1, -1, -1, -1},
                { -1,  4, -1, -1, -1, -1, -1},
                { -1, -1,  5, -1, -1, -1, -1},
                { -1,  6, -1, -1, -1, -1, -1},
                { -1, -1,  7, -1, -1, -1, -1},
                { -1,  9, -1, -1, -1, -1, -1},
                { -1, -1,  8, -1, -1, -1, -1}
        });
        downReverselObjective.CalculatePoints(player);
        assertEquals(6, player.getScore()); // Assuming the objective's points is set to 1
    }

    @Test
    void testCalculatePoints2() {

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, 90, -1, -1, -1, -1, -1},
                { -1, -1,  3, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1,  5, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  2, -1, -1, -1, -1, -1},
                { -1, -1, 90, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1,  5, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  2, -1, -1, -1, -1, -1},
                { -1, -1,  3, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, 90, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  2, -1, -1, -1, -1, -1},
                { -1, -1,  3, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1,  5, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });

        card1.setColor(Color.RED);
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        card1.setColor(Color.GREEN);
        card2.setColor(Color.RED);
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        card2.setColor(Color.BLUE);
        card4.setColor(Color.RED);
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        card4.setColor(Color.BLUE);

        downReverselObjective.addIDusato(2);
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        downReverselObjective.getIDusati().clear();

        downReverselObjective.addIDusato(3);
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        downReverselObjective.getIDusati().clear();

        downReverselObjective.addIDusato(5);
        downReverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

    }
}