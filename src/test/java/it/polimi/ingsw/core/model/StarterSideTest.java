package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarterSideTest {
    private StarterSide starterSide;

    @BeforeEach
    void setUp() {
        starterSide = new StarterSide(1, true);
    }

    @Test
    void testStarterSide() {
        assertNotNull(starterSide);
    }

    @Test
    void testGetId() {
        assertEquals(1, starterSide.getId());
    }

    @Test
    void testGetSide() {
        assertTrue(starterSide.getSide());
    }

}