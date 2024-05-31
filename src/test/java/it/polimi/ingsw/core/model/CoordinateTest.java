package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {
    private Coordinate coordinate;

    @BeforeEach
    void setUp() {
        coordinate = new Coordinate(1, 2);
    }

    @Test
    void testConstructor() {
        assertNotNull(coordinate);
        assertEquals(1, coordinate.getX());
        assertEquals(2, coordinate.getY());
    }

    @Test
    void testGetX() {
        assertEquals(1, coordinate.getX());
    }

    @Test
    void testSetX() {
        coordinate.setX(3);
        assertEquals(3, coordinate.getX());
    }

    @Test
    void testGetY() {
        assertEquals(2, coordinate.getY());
    }

    @Test
    void testSetY() {
        coordinate.setY(4);
        assertEquals(4, coordinate.getY());
    }

    @Test
    void testEquals() {
        int x = 1;
        Coordinate coordinate1 = new Coordinate(1, 2);
        Coordinate coordinate2 = new Coordinate(2, 2);
        Coordinate coordinate3 = new Coordinate(1, 3);
        assertEquals(coordinate, coordinate1);
        assertEquals(coordinate, coordinate);
        assertNotEquals(coordinate, x);
        assertFalse(coordinate.equals(null));
        assertNotEquals(coordinate, coordinate2);
        assertNotEquals(coordinate, coordinate3);
    }


    @Test
    void testHashCode() {
        Coordinate coordinate2 = new Coordinate(1, 2);
        assertEquals(coordinate.hashCode(), coordinate2.hashCode());
    }
}