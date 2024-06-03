package it.polimi.ingsw.core.model;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    private GameState game;
    private Map<Player, Map<Integer, Integer>> players;
    private List<Objective> commonObjectives;
    private Deck goldDeck;
    private Deck resourceDeck;
    private Deck starterDeck;
    private PlayerState player;
    private String configFile;

    @BeforeEach
    void setUp() {
        game = new GameState();
        commonObjectives = new ArrayList<>();
        commonObjectives.add(new Objective());
        commonObjectives.add(new Objective());
        configFile = new StringBuilder().append("src/main/resources/it/polimi/ingsw/").append(configFile).append(".json").toString();
        starterDeck = new Deck("starter", new TypeToken<List<ResourceCard>>() {}.getType());
        resourceDeck = new Deck("resource", new TypeToken<List<ResourceCard>>() {}.getType());
        goldDeck = new Deck("gold", new TypeToken<List<GoldCard>>() {}.getType());
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        player.setUsername("us1");
        game.addPlayer(player);
        game.addCommonObjective(new Objective());
        game.addCommonObjective(new Objective());
        game.setStarterDeck(starterDeck);
    }

    @Test
    void testAddCardToResourceCardsVisible() {
        ResourceCard c = new ResourceCard();
        game.addCardToResourceCardsVisible(c);
        assertNotNull(game.getResourceCardsVisible().contains(c));
    }

    @Test
    void testAddCardToGoldCardsVisible() {
        GoldCard c = new GoldCard();
        game.addCardToGoldCardsVisible(c);
        assertNotNull(game.getGoldCardsVisible().contains(c));
    }

    @Test
    void testInitializePawn() {
        game.initializePawn();
        for(PlayerState player : game.getPlayerStates().values()){
            assertNotNull(player.getPawn());
        }
    }

    @Test
    void testGameState() {
        GameState game = new GameState();
        assertNotNull(game);
    }

    @Test
    void testgetPlayerState() {
        assertTrue(game.getPlayerStates().containsValue(game.getPlayerState(player.getUsername())));
        assertEquals(null, game.getPlayerState("us2"));
        game.getPlayerStates().clear();
        assertEquals(null, game.getPlayerState(player.getUsername()));
    }

    @Test
    void testgetPlayerOrder() {
        List<String> Order = new ArrayList<>();
        Order.add("us1");
        Order.add("us2");
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        PlayerState player2 = new PlayerState();
        player2.setUsername("us2");
        game.addPlayer(player2);
        game.orderPlayers();
        assertTrue(Order.containsAll(game.getPlayerOrder()));
    }

    @Test
    void testgetPlayerId() {
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        assertEquals(0, game.getPlayerId(player.getUsername()));
        assertEquals(-1, game.getPlayerId("us2"));
        game.getPlayerStates().clear();
        assertEquals(-1, game.getPlayerId(player.getUsername()));
    }

    @Test
    void testaddPlayer() {
        PlayerState player1 = new PlayerState();
        player1.setUsername("us2");
        game.addPlayer(player1);
    }

    @Test
    void testSetAndGetCommonObjectives() {
        game.setCommonObjective(commonObjectives);
        assertEquals(commonObjectives, game.getCommonObjectives());
    }


    @Test
    void testsetGoldDeck() {
        game.setGoldDeck(goldDeck);
        assertEquals(goldDeck, game.getGoldDeck());
    }

    @Test
    void testsetResourceDeck() {
        game.setResourceDeck(resourceDeck);
        assertEquals(resourceDeck, game.getResourceDeck());
    }

    @Test
    void testsetStarterDeck() {
        game.setStarterDeck(starterDeck);
        assertEquals(starterDeck.getCards(), game.getStarterDeck());
    }

    @Test
    void testinitializeResourceDeck() {
        game.initializeResourceDeck();
        assertNotNull(game.getResourceDeck());
    }

    @Test
    void testinitializeGoldDeck() {
        game.initializeGoldDeck();
        assertNotNull(game.getGoldDeck());
    }

    @Test
    void testinitializeStarterDeck() {
        game.initializeStarterDeck();
        assertNotNull(game.getStarterDeck());
    }

    @Test
    void testinitializeObjectiveDeck() {
        game.initializeObjectiveDeck();
        assertNotNull(game.getObjectiveDeck());
    }

    @Test
    void testinitializedecks() {
        game.loadDecks();
        assertNotNull(game.getResourceDeck());
        assertNotNull(game.getGoldDeck());
        assertNotNull(game.getStarterDeck());
        assertNotNull(game.getObjectiveDeck());
    }

    @Test
    void testAddCommonObjective() {
        Objective objective = new Objective();
        game.addCommonObjective(objective);
        assertTrue(game.getCommonObjectives().contains(objective));
    }

    @Test
    void testInitilizeMatrixPlayers() {
        Player player2 = new Player();
        player2.setUsername("us2");
        game.addPlayer(player2);
        game.initializeMatrixPlayers(2);
        assertEquals(2, game.getPlayerState(0).getMatrix().length);
        assertEquals(2, game.getPlayerState(1).getMatrix().length);
    }

    @Test
    void testshuffleDecks() {
        game.initializeObjectiveDeck();
        game.getResourceDeck().getCards().add(new ResourceCard());
        game.getResourceDeck().getCards().getFirst().setId(1);
        game.getResourceDeck().getCards().add(new ResourceCard());
        game.getResourceDeck().getCards().get(1).setId(2);
        List<Card> ResourceCards = new ArrayList<>(game.getResourceDeck().getCards());
        assertEquals(ResourceCards.getFirst().getId(),1);
        assertEquals(ResourceCards.get(1).getId(),2);
        boolean orderChanged = false;
        while(!orderChanged){
            game.shuffleDecks();
            if(!ResourceCards.equals(game.getResourceDeck().getCards())){
                orderChanged = true;
            }
        }
        assertTrue(orderChanged);
        assertTrue(game.getResourceDeck().getCards().containsAll(ResourceCards));

        game.getStarterDeck().add(new ResourceCard());
        game.getStarterDeck().getFirst().setId(3);
        game.getStarterDeck().add(new ResourceCard());
        game.getStarterDeck().get(1).setId(4);
        List<Card> StarterCards = new ArrayList<>(game.getStarterDeck());
        assertEquals(StarterCards.getFirst().getId(),3);
        assertEquals(StarterCards.get(1).getId(),4);
        orderChanged = false;
        while(!orderChanged){
            game.shuffleDecks();
            if(!StarterCards.equals(game.getStarterDeck())){
                orderChanged = true;
            }
        }
        assertTrue(orderChanged);
        assertTrue(game.getStarterDeck().containsAll(StarterCards));

        game.getGoldDeck().getCards().add(new GoldCard());
        game.getGoldDeck().getCards().getFirst().setId(5);
        game.getGoldDeck().getCards().add(new GoldCard());
        game.getGoldDeck().getCards().get(1).setId(6);
        List<Card> GoldCards = new ArrayList<>(game.getGoldDeck().getCards());
        assertEquals(GoldCards.getFirst().getId(),5);
        assertEquals(GoldCards.get(1).getId(),6);
        orderChanged = false;
        while(!orderChanged){
            game.shuffleDecks();
            if(!GoldCards.equals(game.getGoldDeck().getCards())){
                orderChanged = true;
            }
        }
        assertTrue(orderChanged);
        assertTrue(game.getGoldDeck().getCards().containsAll(GoldCards));

        game.getObjectiveDeck().getCards().add(new Objective());
        game.getObjectiveDeck().getCards().getFirst().setId(5);
        game.getObjectiveDeck().getCards().add(new Objective());
        game.getObjectiveDeck().getCards().get(1).setId(6);
        List<Card> objectives = new ArrayList<>(game.getObjectiveDeck().getCards());
        assertEquals(objectives.getFirst().getId(),5);
        assertEquals(objectives.get(1).getId(),6);
        orderChanged = false;
        while(!orderChanged){
            game.shuffleDecks();
            if(!objectives.equals(game.getObjectiveDeck().getCards())){
                orderChanged = true;
            }
        }
        assertTrue(orderChanged);
        assertTrue(game.getObjectiveDeck().getCards().containsAll(objectives));
    }

    @Test
    void testInitializeChat() {
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        game.initializeChat();
        assertNotNull(game.getPlayerState("us1").getChat());
        game.getPlayerStates().clear();
        game.initializeChat();
    }

    @Test
    void testgetObjectiveDeckCopy() {
        game.initializeObjectiveDeck();
        List<CardGame> objectiveDeckCopy = game.getObjectiveDeckCopy();
        assertEquals(game.getObjectiveDeckCopy(), objectiveDeckCopy);
    }

    @Test
    void testAssignStarterCardToPlayers() {
        PlayerState player1 = new PlayerState();
        player1.setUsername("us1");
        game.addPlayer(player1);
        game.initializeStarterDeck();
        game.assignStarterCardToPlayers();
        for(PlayerState player : game.getPlayerStates().values()){
            assertNotNull(player.getStarterCard());
        }
        game.getStarterDeck().clear();
        game.assignStarterCardToPlayers();

    }

    @Test
    void testGetPlayerHand() {
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        assertNotNull(game.getPlayerHand("us1"));
        ArrayList<Card> hand = new ArrayList<>();
        game.setPlayerHand("us3", hand);
        game.setPlayerHand("us1", hand);
        assertEquals(null, game.getPlayerHand("us2"));
        assertEquals(hand, game.getPlayerHand("us1"));
        game.getPlayerStates().clear();
        game.setPlayerHand("us1", hand);
        assertEquals(null, game.getPlayerHand("us1"));
    }

    @Test
    void testAssignFirstHandToPlayers() {
        game.initializeResourceDeck();
        game.initializeGoldDeck();
        game.assignFirstHandToPlayers();
        for(PlayerState player : game.getPlayerStates().values()){
            assertNotNull(player.getHand());
        }
        game.getResourceDeck().getCards().clear();
        game.getGoldDeck().getCards().clear();
        game.assignFirstHandToPlayers();

    }

    @Test
    void testGetCommonObjective() {
        List<Objective> objectives = new ArrayList<>();
        Objective test = new Objective();
        objectives.add(test);
        game.setCommonObjective(objectives);
        assertEquals(test, game.getCommonObjective(0));
        assertNull(game.getCommonObjective(2));
    }

    @Test
    void testsetsecretObjective() {
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        Objective objective = new Objective();
        game.setSecretObjective("us1", objective);
        assertEquals(objective, game.getPlayerState("us1").getSecretObj());
    }

    @Test
    void testAssignStarterSide(){
        game.initializeStarterDeck();
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        game.assignStarterCardToPlayers();
        game.assignStarterSide("us1", true);
        assertTrue(game.getPlayerState("us1").getStarterCard().isFrontSide());
    }

   @Test
    void testPlaceStarter(){
        game.getPlayerStates().clear();
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        game.initializeStarterDeck();
        ResourceCard starter = new ResourceCard();
        starter.setId(1);
        game.getPlayerState("us1").initializeMatrix(50);
        game.getPlayerState("us1").setStarterCard(starter);
        game.placeStarter(7, 3, 50);
        assertEquals(1, game.getPlayerState("us1").getMatrix()[25][25]);
    }

    @Test
    void testPlaceStarterInMatrix(){
        PlayerState player = new PlayerState();
        ResourceCard starter = new ResourceCard();
        starter.setId(1);
        game.addPlayer(player);
        player.setUsername("us1");
        game.getPlayerState("us1").setStarterCard(starter);
        game.getPlayerState("us1").initializeMatrix(10);
        game.placeStarterInMatrix("us1", 10);
        assertEquals(1, game.getPlayerState("us1").getMatrix()[5][5]);
    }

    @Test
    void testIsOver(){
        assertFalse(game.isOver());
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        game.getPlayerState("us1").setScore(25);
        assertTrue(game.isOver());
    }

    @Test
    void testGetPlayableCardIdsFromHand(){
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        Card card1 = new ResourceCard();
        card1.setId(1);
        Card card2 = new ResourceCard();
        card2.setId(2);
        GoldCard card3 = new GoldCard();
        card3.setId(3);
        ArrayList<Requirement> requirements = new ArrayList<>();
        Requirement requirement1 = new Requirement(Resource.PLANT, 1);
        requirements.add(requirement1);
        Requirement requirement2 = new Requirement(Resource.ANIMAL, 5);
        requirements.add(requirement2);
        card3.setRequirements(requirements);
        game.getPlayerState("us1").setPersonalResources(new HashMap<Resource, Integer>(){{
            put(Resource.PLANT, 1);
            put(Resource.ANIMAL, 3);
            put(Resource.FUNGI, 0);
            put(Resource.INSECT, 0);
            put(Resource.QUILL, 0);
            put(Resource.NOUN, 0);
            put(Resource.INKWELL, 0);
            put(Resource.MANUSCRIPT, 0);
            put(Resource.ANGLE_COVERED, 0);
            put(Resource.NO_RESOURCE, 0);
        }});
        game.getPlayerState("us1").addCardToHand(card1);
        game.getPlayerState("us1").addCardToHand(card2);
        game.getPlayerState("us1").addCardToHand(card3);
        List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, game.getPlayableCardIdsFromHand("us1").getPlayingHandIds());
    }

    @Test
    void testCalculateResource(){
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);

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

        Map<Resource, Integer> expectedResources = new HashMap<>();
        expectedResources.put(Resource.PLANT, 1);
        expectedResources.put(Resource.ANIMAL, 1);
        expectedResources.put(Resource.FUNGI, 0);
        expectedResources.put(Resource.INSECT, 0);
        expectedResources.put(Resource.QUILL, 0);
        expectedResources.put(Resource.NOUN, 0);
        expectedResources.put(Resource.INKWELL, 0);
        expectedResources.put(Resource.MANUSCRIPT, 0);
        expectedResources.put(Resource.ANGLE_COVERED, 0);
        expectedResources.put(Resource.NO_RESOURCE, 0);

        game.getPlayerState("us1").setCodex(new ArrayList<Card>(){{add(card1);}});;
        assertEquals(expectedResources, game.calculateResource("us1"));
    }

    @Test
    void testRemoveResourceCardVisible(){
        ResourceCard card = new ResourceCard();
        game.addCardToResourceCardsVisible(card);
        game.removeResourceCardVisible(card);
        assertFalse(game.getResourceCardsVisible().contains(card));
    }

    @Test
    void testRemoveGoldCardVisible(){
        GoldCard card = new GoldCard();
        game.addCardToGoldCardsVisible(card);
        game.removeGoldCardVisible(card);
        assertFalse(game.getGoldCardsVisible().contains(card));
    }
}
