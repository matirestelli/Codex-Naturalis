package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecreteObjectiveCardTest {
    private SecreteObjectiveCard secreteObjectiveCard;

    @BeforeEach
    void setUp() {
        secreteObjectiveCard = new SecreteObjectiveCard(new Objective());
    }

    @Test
    void testSecreteObjectiveCard() {
        assertNotNull(secreteObjectiveCard);
    }

}