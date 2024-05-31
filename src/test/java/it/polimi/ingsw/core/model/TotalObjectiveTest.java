package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TotalObjectiveTest {
    private SxDiagonalObjective sxDiagonalObjective;
    private DxDiagonalObjective dxDiagonalObjective;
    private LObjective LObjective;
    private PlayerState player;

    @BeforeEach
    void setUp() {
        sxDiagonalObjective = new SxDiagonalObjective();
        dxDiagonalObjective = new DxDiagonalObjective();
        LObjective = new LObjective();
        List<Card> codex = new ArrayList<>();
        sxDiagonalObjective.setPoints(3);// Assuming the objective's points is set to 1
        dxDiagonalObjective.setPoints(3);// Assuming the objective's points is set to 1
        LObjective.setPoints(2);// Assuming the objective's points is set to 1
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        Card card1 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card2 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card3 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card4 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card5 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card6 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card7 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card1.setId(1);
        card2.setId(2);
        card3.setId(3);
        card4.setId(4);
        card5.setId(5);
        card6.setId(6);
        card7.setId(7);
        player.setCodex(codex);
        player.addCardToCodex(card1);
        player.addCardToCodex(card2);
        player.addCardToCodex(card3);
        player.addCardToCodex(card4);
        player.addCardToCodex(card5);
        player.addCardToCodex(card6);
        player.addCardToCodex(card7);
        card1.setColor(Color.RED);
        card2.setColor(Color.RED);
        card3.setColor(Color.RED);
        card4.setColor(Color.RED);
        card5.setColor(Color.RED);
        card6.setColor(Color.BLUE);
        card7.setColor(Color.BLUE);
    }

    @Test
    void testDiagonalSxDx() {
        player.setMatrix(new int[][] {
                { -1, -1, -1, -1 },
                {  3, -1,  5, -1 },
                { -1,  1, -1, -1 },
                {  4, -1,  2, -1 },
                { -1, -1, -1, -1 },
                { -1, -1, -1, -1 }
        });
        sxDiagonalObjective.setColor(Color.RED);
        dxDiagonalObjective.setColor(Color.RED);
        sxDiagonalObjective.CalculatePoints(player);
        dxDiagonalObjective.CalculatePoints(player);
        assertEquals(6, player.getScore()); // Assuming the objective's points is set to 1
    }

    @Test
    void testLObjectiveDigonalObjective() {
        player.setMatrix(new int[][] {
                {  7, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                {  6, -1, -1, -1, -1, -1, -1},
                { -1,  1, -1, -1, -1, -1, -1},
                { -1, -1,  2, -1, -1, -1, -1},
                { -1, -1, -1,  3, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });
        sxDiagonalObjective.setColor(Color.RED);
        LObjective.setColor1(Color.BLUE);
        LObjective.setColor2(Color.RED);
        sxDiagonalObjective.CalculatePoints(player);
        LObjective.CalculatePoints(player);
        assertEquals(5, player.getScore()); // Assuming the objective's points is set to 1
    }
}
