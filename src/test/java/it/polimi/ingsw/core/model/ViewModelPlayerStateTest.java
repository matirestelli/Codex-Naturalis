package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ViewModelPlayerStateTest {
    @BeforeEach
    void setUp() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
    }

    @Test
    void testGetScore() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
        assertEquals(0, viewModelPlayerState.getScore());
    }

    @Test
    void testSetScore() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
        viewModelPlayerState.setScore(10);
        assertEquals(10, viewModelPlayerState.getScore());
    }

    @Test
    void testGetCodex() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
        assertEquals(new ArrayList<>(), viewModelPlayerState.getCodex());
    }

    @Test
    void testSetCodex() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
        ArrayList<Card> codex = new ArrayList<>();
        codex.add(new ResourceCard());
        viewModelPlayerState.setCodex(codex);
        assertEquals(codex, viewModelPlayerState.getCodex());
    }

    @Test
    void testGetPersonalResources() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
        assertEquals(new HashMap<>(), viewModelPlayerState.getPersonalResources());
    }

    @Test
    void testSetPersonalResources() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
        HashMap<Resource, Integer> personalResources = new HashMap<>();
        personalResources.put(Resource.ANIMAL, 1);
        viewModelPlayerState.setPersonalResources(personalResources);
        assertEquals(personalResources, viewModelPlayerState.getPersonalResources());
    }

    @Test
    void testGetColor() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
        assertNull(viewModelPlayerState.getColor());
    }

    @Test
    void testSetColor() {
        ViewModelPlayerState viewModelPlayerState = new ViewModelPlayerState();
        viewModelPlayerState.setColor(Color.BLUE);
        assertEquals(Color.BLUE, viewModelPlayerState.getColor());
    }

}