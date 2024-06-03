package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardToAttachSelectedTest {

    @Test
    void testconstructor() {
        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("card", null);
        assertEquals("card", cardToAttachSelected.getString());
        assertNull(cardToAttachSelected.getCodex());
    }

    @Test
    void testGetString() {
        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("card", null);
        assertEquals("card", cardToAttachSelected.getString());
    }

    @Test
    void testGetCodex() {
        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("card", null);
        assertNull(cardToAttachSelected.getCodex());
    }

}