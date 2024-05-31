package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    private Point point;
    private int qta;
    private boolean simple;
    private Resource resource;

    @BeforeEach
    void setUp() {
        qta = 5;
        simple = true;
        resource = Resource.ANIMAL;
        point = new Point();
        point.setQta(qta);
        point.setSimple(simple);
        point.setResource(resource);
    }

    @Test
    void testGetQta() {
        assertEquals(qta, point.getQta());
    }

    @Test
    void testSetQta() {
        int newQta = 10;
        point.setQta(newQta);
        assertEquals(newQta, point.getQta());
    }

    @Test
    void testIsSimple() {
        assertTrue(point.isSimple());
    }

    @Test
    void testSetSimple() {
        point.setSimple(false);
        assertFalse(point.isSimple());
    }

    @Test
    void testGetResource() {
        assertEquals(resource, point.getResource());
    }

    @Test
    void testSetResource() {
        Resource newResource = Resource.FUNGI;
        point.setResource(newResource);
        assertEquals(newResource, point.getResource());
    }
}