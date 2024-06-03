package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DxDiagonalObjectiveTest {
    private DxDiagonalObjective dxDiagonalObjective;
    private PlayerState player;
    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    void setUp() {
        dxDiagonalObjective = new DxDiagonalObjective();
        List<Card> codex = new ArrayList<>();
        dxDiagonalObjective.setPoints(3); // Assuming the objective's points is set to 1
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        card1 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card2 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card3 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card4 = new ResourceCard();
        Card card5 = new ResourceCard();
        Card card6 = new ResourceCard();
        Card card7 = new ResourceCard();
        Card card8 = new ResourceCard();
        Card card9 = new ResourceCard();
        card1.setId(2);
        card2.setId(4);
        card3.setId(6);
        card4.setId(8);
        card5.setId(10);
        card6.setId(12);
        card7.setId(5);
        card8.setId(7);
        card9.setId(9);
        player.setCodex(codex);
        player.addCardToCodex(card1);
        player.addCardToCodex(card2);
        player.addCardToCodex(card3);
        player.addCardToCodex(card4);
        player.addCardToCodex(card5);
        player.addCardToCodex(card6);
        player.addCardToCodex(card7);
        player.addCardToCodex(card8);
        player.addCardToCodex(card9);
        card1.setColor(Color.RED);
        card2.setColor(Color.RED);
        card3.setColor(Color.RED);
        card4.setColor(Color.RED);
        card5.setColor(Color.RED);
        card6.setColor(Color.RED);
        card7.setColor(Color.RED);
        card8.setColor(Color.RED);
        card9.setColor(Color.RED);
        dxDiagonalObjective.setColor(Color.RED);
    }

    @Test
    void testgetType() {
        assertEquals("DxDiagonal", dxDiagonalObjective.getType());
    }

    @Test
    void testCalculatePoints() {
        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 , -1, 12},
                { -1, -1, -1, -1 , 10, -1},
                { -1, -1, -1, 8 , -1, -1 },
                { -1, -1, 6, -1 , -1, 9 },
                { -1, 4, -1, -1 , 7, -1 },
                { 2, -1, -1, 5 , -1, -1 },
                { -1, -1, -1, -1 , -1, -1}
        });
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(9, player.getScore()); // Assuming the objective's points is set to 1
    }

    @Test
    void testCalculatePoints2() {
        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                { -1, -1, 90, -1 },
                { -1,  2, -1, -1 },
                {  4, -1, -1, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore()); // Assuming the objective's points is set to 1

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                { -1, -1,  6, -1 },
                { -1, 90, -1, -1 },
                {  4, -1, -1, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                { -1, -1,  6, -1 },
                { -1,  2, -1, -1 },
                { 90, -1, -1, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                { -1, -1,  6, -1 },
                { -1,  2, -1, -1 },
                {  4, -1, -1, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });

        card1.setColor(Color.PURPLE);
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        card1.setColor(Color.RED);
        card2.setColor(Color.PURPLE);
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        card2.setColor(Color.RED);
        card3.setColor(Color.PURPLE);
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        card3.setColor(Color.RED);

        dxDiagonalObjective.addIDusato(2);
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        dxDiagonalObjective.getIDusati().clear();

        dxDiagonalObjective.addIDusato(4);
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        dxDiagonalObjective.getIDusati().clear();

        dxDiagonalObjective.addIDusato(6);
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
    }
}