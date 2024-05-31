package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequirementTest {
    private Requirement requirement;
    private int qta;
    private Resource resource;

    @BeforeEach
    void setUp() {
        qta = 5;
        resource = Resource.ANIMAL; // Assuming Resource enum is properly implemented for testing
        requirement = new Requirement(resource, qta);
        requirement.setQta(qta);
        requirement.setResource(resource);
    }

    @Test
    void testGetQta() {
        assertEquals(qta, requirement.getQta());
    }

    @Test
    void testSetQta() {
        int newQta = 10;
        requirement.setQta(newQta);
        assertEquals(newQta, requirement.getQta());
    }

    @Test
    void testGetResource() {
        assertEquals(resource, requirement.getResource());
    }

    @Test
    void testSetResource() {
        Resource newResource = Resource.INSECT;
        requirement.setResource(newResource);
        assertEquals(newResource, requirement.getResource());
    }
}