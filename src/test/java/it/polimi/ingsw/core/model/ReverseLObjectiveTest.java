package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReverseLObjectiveTest {
    private ReverseLObjective reverselObjective;
    private PlayerState player;
    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    void setUp() {
        reverselObjective = new ReverseLObjective();
        List<Card> codex = new ArrayList<>();
        reverselObjective.setPoints(3); // Assuming the objective's points is set to 1
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        card1 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card2 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card3 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card4 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card5 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card6 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        Card card7 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card1.setId(2);
        card2.setId(3);
        card3.setId(4);
        card4.setId(5);
        card5.setId(6);
        card6.setId(7);
        card7.setId(8);
        player.setCodex(codex);
        player.addCardToCodex(card1);
        player.addCardToCodex(card2);
        player.addCardToCodex(card3);
        player.addCardToCodex(card4);
        player.addCardToCodex(card5);
        player.addCardToCodex(card6);
        player.addCardToCodex(card7);
        card1.setColor(Color.BLUE);
        card2.setColor(Color.BLUE);
        card3.setColor(Color.GREEN);
        card4.setColor(Color.BLUE);
        card5.setColor(Color.GREEN);
        card6.setColor(Color.BLUE);
        card7.setColor(Color.GREEN);
        reverselObjective.setColor1(Color.BLUE);
        reverselObjective.setColor2(Color.GREEN);
    }

    @Test
    void testgetType(){
        assertEquals("ReverseL", reverselObjective.getType());
    }

    @Test
    void testCalculatePoints() {
        player.setMatrix(new int[][] {
                { -1, -1,  2, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1,  3, -1, -1, -1, -1},
                { -1,  4, -1, -1, -1, -1, -1},
                { -1, -1,  5, -1, -1, -1, -1},
                { -1,  6, -1, -1, -1, -1, -1},
                { -1, -1,  7, -1, -1, -1, -1},
                { -1,  8, -1, -1, -1, -1, -1}
        });
        reverselObjective.CalculatePoints(player);
        assertEquals(6, player.getScore()); // Assuming the objective's points is set to 1
    }

    @Test
    void testCalculatePoints2() {
        player.setMatrix(new int[][] {
                { -1, -1, 90, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1,  3, -1, -1, -1, -1},
                { -1,  4, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });
        reverselObjective.CalculatePoints(player);
        assertEquals(0, reverselObjective.getCompleted());

        player.setMatrix(new int[][] {
                { -1, -1,  2, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, 90, -1, -1, -1, -1},
                { -1,  4, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });
        reverselObjective.CalculatePoints(player);
        assertEquals(0, reverselObjective.getCompleted());

        player.setMatrix(new int[][] {
                { -1, -1,  2, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1,  3, -1, -1, -1, -1},
                { -1, 90, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });
        reverselObjective.CalculatePoints(player);
        assertEquals(0, reverselObjective.getCompleted());

        player.setMatrix(new int[][] {
                { -1, -1,  2, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1,  3, -1, -1, -1, -1},
                { -1,  4, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });

        card1.setColor(Color.RED);
        reverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        card1.setColor(Color.BLUE);
        card2.setColor(Color.RED);
        reverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());

        card2.setColor(Color.BLUE);
        card3.setColor(Color.RED);
        reverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        card3.setColor(Color.GREEN);

        reverselObjective.addIDusato(2);
        reverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        reverselObjective.getIDusati().clear();

        reverselObjective.addIDusato(3);
        reverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
        reverselObjective.getIDusati().clear();

        reverselObjective.addIDusato(4);
        reverselObjective.CalculatePoints(player);
        assertEquals(0, player.getScore());
    }
}