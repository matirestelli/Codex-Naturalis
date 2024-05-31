package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResourceObjectiveTest {
    private ResourceObjective resourceObjective1;
    private ResourceObjective resourceObjective2;
    private ResourceObjective resourceObjectiveFalse;
    private PlayerState player;
    private Map<Resource, Integer> playerResources;

    @BeforeEach
    void setUp() {
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        resourceObjective1 = new ResourceObjective();
        resourceObjective2 = new ResourceObjective();
        resourceObjectiveFalse = new ResourceObjective();
        resourceObjectiveFalse.setPoints(5);
        resourceObjective1.setPoints(2);
        resourceObjective2.setPoints(3);
        List<Resource> resourcelist = new ArrayList<>();
        List<Resource> resourcelist2 = new ArrayList<>();
        Requirement requirement1 = new Requirement(Resource.ANIMAL, 1);
        Requirement requirement2 = new Requirement(Resource.INSECT, 3);
        Requirement requirement3 = new Requirement(Resource.PLANT, 2);

        resourcelist.add(Resource.PLANT);
        resourcelist.add(Resource.ANIMAL);

        resourcelist2.add(Resource.INSECT);


        requirement1.setResource(Resource.PLANT);
        requirement1.setQta(1);

        requirement2.setResource(Resource.ANIMAL);
        requirement2.setQta(1);

        requirement3.setResource(Resource.INSECT);
        requirement3.setQta(1);

        resourceObjective1.requirements.add(requirement1);
        resourceObjective2.requirements.add(requirement2);
        resourceObjective2.requirements.add(requirement3);


        List<Card> codex = new ArrayList<>();
        Card card1 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card1.setId(1);
        card1.setSide(false);
        card1.setBackResources(resourcelist);
        // Card1 (back ANIMAL+PLANT)
        codex.add(card1);
        // Assuming Player class has a setResources method for testing
        Card card2 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card2.setId(2);
        card2.setSide(false);
        card2.setBackResources(resourcelist);
        codex.add(card2);
        // Card2 (back ANIMAL+PLANT)
        Card card3 = new ResourceCard(); // Assuming Card class is properly implemented for testing
        card3.setId(3);
        card3.setSide(false);
        card3.setBackResources(resourcelist2);
        codex.add(card3);
        player.setCodex(codex);
        // Card3 (back INSECT)
    }

    @Test
    void testCalculatePoints() {
        resourceObjective1.CalculatePoints(player);
        resourceObjective2.CalculatePoints(player);
        assertEquals(7, player.getScore());
    }


}