package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawCardTest {

        @Test
        void testGetId() {
            DrawCard drawCard = new DrawCard("1");
            assertEquals("1", drawCard.getId());
        }

}