package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {
    private PlayerState player;
    private int score;
    private List<Card> playingHand;
    private List<Card> codex;
    private int[][] matrix;
    private Objective secretObjective;
    private Map<Resource, Integer> personalResources;
    private Color pawn;

    @BeforeEach
    void setUp() {
        score = 10;
        playingHand = new ArrayList<>();
        codex = new ArrayList<>();
        matrix = new int[4][4];
        secretObjective = new Objective(); // Assuming Card class is properly implemented for testing
        personalResources = new HashMap<>();
        pawn = Color.RED; // Assuming Color enum is properly implemented for testing
        player = new PlayerState();
        player.getUsername();
        player.setScore(score);
        player.setHand(playingHand);
        player.setCodex(codex);
        player.setMatrix(matrix);
        player.setSecretObj(secretObjective);
        player.setPersonalResources(personalResources);
        player.setPawn(pawn);
    }

    @Test
    void testgetScore() {
        assertEquals(score, player.getScore());
    }

    @Test
    void testsetScore() {
        int newScore = 20;
        player.setScore(newScore);
        assertEquals(newScore, player.getScore());
    }

    @Test
    void testgetHand() {
        assertEquals(playingHand, player.getHand());
    }

    @Test
    void testsetHand() {
        List<Card> newPlayingHand = new ArrayList<>();
        player.setHand(newPlayingHand);
        assertEquals(newPlayingHand, player.getHand());
    }

    @Test
    void testgetCodex() {
        assertEquals(codex, player.getCodex());
    }

    @Test
    void testsetCodex() {
        List<Card> newCodex = new ArrayList<>();
        player.setCodex(newCodex);
        assertEquals(newCodex, player.getCodex());
    }

    @Test
    void testgetMatrix() {
        assertArrayEquals(matrix, player.getMatrix());
    }

    @Test
    void testsetMatrix() {
        int[][] newMatrix = new int[4][4];
        player.setMatrix(newMatrix);
        assertArrayEquals(newMatrix, player.getMatrix());
    }

    @Test
    void testaddScore() {
        int scoreToAdd = 5;
        player.addScore(scoreToAdd);
        assertEquals(score + scoreToAdd, player.getScore());
    }

    @Test
    void testsetAndgetSecretObjective() {
        Objective newSecretObjective = new Objective(); // Assuming Card class is properly implemented for testing
        player.setSecretObj(newSecretObjective);
        assertEquals(newSecretObjective, player.getSecretObj());
    }

    @Test
    void testgetPersonalResources() {
        assertEquals(personalResources, player.getPersonalResources());
    }

    @Test
    void testsetPersonalResources() {
        Map<Resource, Integer> newPersonalResources = new HashMap<>();
        player.setPersonalResources(newPersonalResources);
        assertEquals(newPersonalResources, player.getPersonalResources());
    }

    @Test
    void testgetPawn() {
        assertEquals(pawn, player.getPawn());
    }

    @Test
    void testsetPawn() {
        Color newPawn = Color.BLUE; // Assuming Color enum is properly implemented for testing
        player.setPawn(newPawn);
        assertEquals(newPawn, player.getPawn());
    }

    @Test
    void testinizializematrix() {
        int matrixDimension = 4;
        player.initializeMatrix(matrixDimension);

        assertEquals(matrixDimension, player.getMatrix().length);
        assertEquals(matrixDimension, player.getMatrix()[0].length);

        for (int i = 0; i < matrixDimension; i++) {
            for (int j = 0; j < matrixDimension; j++) {
                assertEquals(-1, player.getMatrix()[i][j]);
            }
        }
    }

    @Test
    void testaddcardtoPlayingHand() {
        Card card = new ResourceCard(); // Assuming Card class is properly
        player.addCardToHand(card);
        assertTrue(player.getHand().contains(card));
    }

    @Test
    void testaddcardtoCodex() {
        Card card = new ResourceCard(); // Assuming Card class is properly
        player.addCardToCodex(card);
        assertTrue(player.getCodex().contains(card));
    }

    @Test
    void testremovecardfromPlayingHand() {
        Card card = new ResourceCard(); // Assuming Card class is properly
        player.addCardToHand(card);
        player.removeCardFromHand(card);
        assertFalse(player.getHand().contains(card));
    }

    @Test
    public void testCalculateResources() {
        Card card1 = new ResourceCard();
        card1.setSide(true);
        Map<Integer, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(0, new Corner() {{
            setResource(Resource.PLANT);
        }});
        frontCorners1.put(1, new Corner() {
            {
                setResource(Resource.ANIMAL);
            }
        });
        frontCorners1.put(2, new Corner() {
            {
                setResource(Resource.ANIMAL);
                setEmpty(true);
            }
        });
        card1.setFrontCorners(frontCorners1);

        Card card2 = new ResourceCard();
        card2.setSide(false);
        List<Resource> backResources2 = Arrays.asList(Resource.ANIMAL, Resource.ANIMAL);
        card2.setBackResources(backResources2);

        player.addCardToCodex(card1);
        player.addCardToCodex(card2);

        Map<Resource, Integer> expectedResources = new HashMap<>();
        expectedResources.put(Resource.PLANT, 1);
        expectedResources.put(Resource.ANIMAL, 3);
        expectedResources.put(Resource.FUNGI, 0);
        expectedResources.put(Resource.INSECT, 0);
        expectedResources.put(Resource.QUILL, 0);
        expectedResources.put(Resource.NOUN, 0);
        expectedResources.put(Resource.INKWELL, 0);
        expectedResources.put(Resource.MANUSCRIPT, 0);
        expectedResources.put(Resource.ANGLE_COVERED, 0);
        expectedResources.put(Resource.NO_RESOURCE, 0);

        Map<Resource, Integer> calculatedResources = player.calculateResources();

        assertEquals(expectedResources, calculatedResources);
    }

    @Test
    void testsetstaterSide() {
        ResourceCard startercard = new ResourceCard(); // Assuming Card class is properly
        player.setStarterCard(startercard);
        player.setStarterSide(true);
        assertTrue(startercard.isFrontSide());
    }

    @Test
    void testAddCardToMatrix() {
        Card card = new ResourceCard();// Assuming Card class is properly
        card.setId(5);
        player.addCardToMatrix(0, 0, 5);
        assertEquals(card.getId(), player.getMatrix()[0][0]);
    }

    @Test
    void testgetcardfromhand() {
        Card card = new ResourceCard(); // Assuming Card class is properly
        card.setId(5);
        player.addCardToHand(card);
        assertEquals(card, player.getCardFromHand(5));
        assertEquals(null, player.getCardFromHand(6));
    }

    @Test
    void testSetAndGetBoard() {
        Cell[][] board = new Cell[5][5];
        player.setBoard(board);
        assertEquals(board, player.getBoard());
    }

    @Test
    void testGetChat() {
        assertNotNull(player.getChat());
    }

    @Test
    void testSetAndGetStarterCard() {
        ResourceCard starterCard = new ResourceCard();
        player.setStarterCard(starterCard);
        assertEquals(starterCard, player.getStarterCard());
    }

    @Test
    void testInitializeChat() {
        player.initializeChat();
        assertNotNull(player.getChat());
    }

    @Test
    void testSetConnected() {
        player.setConnected(true);
        assertTrue(player.isConnected());
    }

    @Test
    void testPing() {
        player.ping();
        assertTrue(player.isConnected());
    }

    @Test
    void testCheckConnection() {
        player.ping();
        player.checkConnection(1000);
        assertTrue(player.isConnected());
        player.checkConnection(-1);
        assertFalse(player.isConnected());
    }
}