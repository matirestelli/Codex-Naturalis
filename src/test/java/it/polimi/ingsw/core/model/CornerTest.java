package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CornerTest {

    @Test
    public void testisHidden() {
        Corner corner = new Corner();
        assertFalse(corner.isHidden());
        corner.setHidden(true);
        assertTrue(corner.isHidden());
    }

    @Test
    public void testisEmpty() {
        Corner corner = new Corner();
        assertTrue(corner.isEmpty());
        corner.setEmpty(false);
        assertFalse(corner.isEmpty());
    }

    @Test
    public void testsetEmpty() {
        Corner corner = new Corner();
        assertTrue(corner.isEmpty());
        corner.setEmpty(false);
        assertFalse(corner.isEmpty());
    }

    @Test
    public void testsetHidden() {
        Corner corner = new Corner();
        assertFalse(corner.isHidden());
        corner.setHidden(true);
        assertTrue(corner.isHidden());
    }

    @Test
    public void testsetResource() {
        Corner corner = new Corner();
        assertNull(corner.getResource());
        corner.setResource(Resource.PLANT);
        assertEquals(Resource.PLANT, corner.getResource());
    }

    @Test
    public void testgetResource() {
        Corner corner = new Corner();
        assertNull(corner.getResource());
        corner.setResource(Resource.ANIMAL);
        assertEquals(Resource.ANIMAL, corner.getResource());
    }
}
