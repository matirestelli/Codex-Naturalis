package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

        @Test
        void testGetCharacter() {
            Cell cell = new Cell();
            cell.setCharacter('a');
            assertEquals('a', cell.getCharacter());
        }

        @Test
        void testSetCharacter() {
            Cell cell = new Cell();
            cell.setCharacter('a');
            assertEquals('a', cell.getCharacter());
        }

        @Test
        void testGetCard() {
            Cell cell = new Cell();
            cell.setCard(new ResourceCard());
            assertNotNull(cell.getCard());
        }

        @Test
        void testSetCard() {
            Cell cell = new Cell();
            cell.setCard(new ResourceCard());
            assertNotNull(cell.getCard());
        }

        @Test
        void testGetColor() {
            Cell cell = new Cell();
            cell.setColor("red");
            assertEquals("red", cell.getColor());
        }

        @Test
        void testSetColor() {
            Cell cell = new Cell();
            cell.setColor("red");
            assertEquals("red", cell.getColor());
        }

}