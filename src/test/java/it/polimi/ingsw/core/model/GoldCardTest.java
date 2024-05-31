package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    private GoldCard goldCard;
    private Point point;
    private List<Requirement> requirements;

    @BeforeEach
    void setUp() {
        point = new Point(); // Assuming Point class is properly implemented for testing
        requirements = new ArrayList<>(); // Assuming Requirement class is properly implemented for testing
        goldCard = new GoldCard();
        goldCard.setPoint(point);
        goldCard.setRequirements(requirements);
    }

    @Test
    void testSetPoint() {
        Point newPoint = new Point();
        goldCard.setPoint(newPoint);
        assertEquals(newPoint, goldCard.getPoint());
    }

    @Test
    void testSetRequirements() {
        List<Requirement> newRequirements = new ArrayList<>();
        goldCard.setRequirements(newRequirements);
        assertEquals(newRequirements, goldCard.getRequirements());
    }

    @Test
    void testGetPoint() {
        assertEquals(point, goldCard.getPoint());
    }

    @Test
    void testGetRequirements() {
        assertEquals(requirements, goldCard.getRequirements());
    }
}