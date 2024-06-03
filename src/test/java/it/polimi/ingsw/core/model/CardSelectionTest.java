package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardSelectionTest {

            @Test
            void testconstructor() {
                CardSelection cardSelection = new CardSelection(1, true);
                assertEquals(1, cardSelection.getId());
                assertTrue(cardSelection.getSide());
            }

            @Test
            void testGetId() {
                CardSelection cardSelection = new CardSelection(1, true);
                assertEquals(1, cardSelection.getId());
            }

            @Test
            void testGetSide() {
                CardSelection cardSelection = new CardSelection(1, true);
                assertTrue(cardSelection.getSide());
            }

}