package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {
    private ResourceCard resourceCard;
    private int point;
    private String ANSI_COLOR;

    @BeforeEach
    void setUp() {
        point = 5;
        ANSI_COLOR = "\u001B[31m"; // ANSI color code for red
        resourceCard = new ResourceCard();
        resourceCard.setPoint(point);
        resourceCard.setANSI_COLOR(ANSI_COLOR);
    }

    @Test
    void testGetPoint() {
        assertEquals(point, resourceCard.getPoint());
    }

    @Test
    void testSetPoint() {
        int newPoint = 10;
        resourceCard.setPoint(newPoint);
        assertEquals(newPoint, resourceCard.getPoint());
    }

    @Test
    void testGetANSI_COLOR() {
        assertEquals(ANSI_COLOR, resourceCard.getANSI_COLOR());
    }

    @Test
    void testSetANSI_COLOR() {
        String newANSI_COLOR = "\u001B[32m"; // ANSI color code for green
        resourceCard.setANSI_COLOR(newANSI_COLOR);
        assertEquals(newANSI_COLOR, resourceCard.getANSI_COLOR());
    }
}