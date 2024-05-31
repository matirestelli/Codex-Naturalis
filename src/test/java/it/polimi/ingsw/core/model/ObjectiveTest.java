package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveTest {
    private Objective objective;
    private int id;
    private boolean frontSide;
    private String frontCover;
    private String backCover;
    private int points;
    private boolean isCompleted;
    private int completed;
    private List<Integer> IDusati;
    private String pattern;

    @BeforeEach
    void setUp() {
        id = 1;
        frontSide = true;
        frontCover = "frontCover";
        backCover = "backCover";
        points = 10;
        isCompleted = false;
        completed = 0;
        IDusati = new ArrayList<>();
        pattern = "pattern";
        objective = new Objective();
        objective.setId(id);
        objective.setSide(frontSide);
        objective.setFrontCover(frontCover);
        objective.setBackCover(backCover);
        objective.setPoints(points);
        // objective.setCompleted(isCompleted); // This method is commented out in your Objective class
    }


    @Test
    void testgetId() {
        assertEquals(id, objective.getId());
    }

    @Test
    void testSetCompleted() {
        int newCompleted = objective.setCompleted();
        assertEquals(completed + 1, newCompleted);
    }

    @Test
    void testResetCompleted() {
        objective.setCompleted();
        objective.resetCompleted();
        assertEquals(0, objective.getCompleted());
    }

    @Test
    void testGetIDusati() {
        assertNotNull(objective.getIDusati());
    }

    @Test
    void testAddIDusato() {
        objective.addIDusato(1);
        assertEquals(1, objective.getIDusati().size());
    }

    @Test
    void testGetCompleted() {
        assertEquals(completed, objective.getCompleted());
    }

    @Test
    void testSetId() {
        int newId = 2;
        objective.setId(newId);
        assertEquals(newId, objective.getId());
    }

    @Test
    void testIsFrontSide() {
        assertEquals(frontSide, objective.isFrontSide());
    }

    @Test
    void testSetSide() {
        boolean newFrontSide = false;
        objective.setSide(newFrontSide);
        assertEquals(newFrontSide, objective.isFrontSide());
    }

    @Test
    void testGetFrontCover() {
        assertEquals(frontCover, objective.getFrontCover());
    }

    @Test
    void testSetFrontCover() {
        String newFrontCover = "newFrontCover";
        objective.setFrontCover(newFrontCover);
        assertEquals(newFrontCover, objective.getFrontCover());
    }

    @Test
    void testGetBackCover() {
        assertEquals(backCover, objective.getBackCover());
    }

    @Test
    void testSetBackCover() {
        String newBackCover = "newBackCover";
        objective.setBackCover(newBackCover);
        assertEquals(newBackCover, objective.getBackCover());
    }

    @Test
    void testIsCompleted() {
        assertEquals(isCompleted, objective.isCompleted());
    }

    @Test
    void testsetpoints() {
        int newPoints = 20;
        objective.setPoints(newPoints);
        assertEquals(newPoints, objective.getPoints());
    }

    @Test
    void testgetPoints() {
        assertEquals(points, objective.getPoints());
    }
}