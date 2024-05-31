package it.polimi.ingsw.core.model;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    private GameState game;
    private String id;
    private int maxPlayers;
    private Map<Player, Map<Integer, Integer>> players;
    private int numPlayers;
    private Objective[] commonObjectives;
    private Deck goldDeck;
    private Deck resourceDeck;
    private Deck starterDeck;
    private PlayerState player;

    @BeforeEach
    void setUp() {
        game = new GameState();
        id = "game1";
        maxPlayers = 4;
        players = new HashMap<>();
        numPlayers = 2;
        commonObjectives = new Objective[2];
        goldDeck = new Deck("goldCards", new TypeToken<List<Card>>(){}.getType());
        resourceDeck = new Deck("resourceCards", new TypeToken<List<Card>>(){}.getType());
        starterDeck = new Deck("starterCards", new TypeToken<List<Card>>(){}.getType());
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        player.setUsername("us1");

        game.addPlayer(player);
        game.setCommonObjective(commonObjectives, game.getPlayerId(player.getUsername()));
        game.setStarterDeck(starterDeck);
    }

    @Test
    void testGameState() {
        GameState game = new GameState();
        assertNotNull(game);
    }

    @Test
    void testgetPlayerState() {
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        Integer index = game.getPlayerId(player.getUsername());
        assertEquals(player, game.getPlayerState(index));
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
    }

    @Test
    void testaddPlayer() {
        PlayerState player = new PlayerState();
        player.setUsername("us1");
        game.addPlayer(player);
        assertEquals("us1", game.getPlayerState(player.getUsername()));
    }

    @Test
    void testgetCommonObjectives() {
        game.setCommonObjective(commonObjectives, game.getPlayerId(player.getUsername()));
        assertEquals(commonObjectives, game.getCommonObjectives());
    }

    @Test
    void testsetCommonObjectives() {
        game.setCommonObjective(commonObjectives, game.getPlayerId(player.getUsername()));
        assertEquals(commonObjectives, game.getCommonObjectives());
    }

    @Test
    void testgetGoldDeck() {
        assertEquals(goldDeck, game.getGoldDeck());
    }

    @Test
    void testsetGoldDeck() {
        game.setGoldDeck(goldDeck);
        assertEquals(goldDeck, game.getGoldDeck());
    }

    @Test
    void testgetResourceDeck() {
        assertEquals(resourceDeck, game.getResourceDeck());
    }

    @Test
    void testsetResourceDeck() {
        game.setResourceDeck(resourceDeck);
        assertEquals(resourceDeck, game.getResourceDeck());
    }

    @Test
    void testgetStarterDeck() {
        assertEquals(starterDeck, game.getStarterDeck());
    }

    @Test
    void testsetStarterDeck() {
        game.setStarterDeck(starterDeck);
        assertEquals(starterDeck, game.getStarterDeck());
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
    void testinitializedecks() {
        game.loadDecks();
        assertNotNull(game.getResourceDeck());
        assertNotNull(game.getGoldDeck());
        assertNotNull(game.getStarterDeck());
    }

    @Test
    void testAddCommonObjective() {
        Objective objective = new Objective();
        game.addCommonObjective(objective);
        assertTrue(game.getCommonObjectives().contains(objective));
    }

    @Test
    void testInitilizeMatrixPlayers() {
        game.initializeMatrixPlayers(2);
        assertEquals(2, game.getPlayerState(0).getMatrix().length);
        assertEquals(2, game.getPlayerState(1).getMatrix().length);
    }

    @Test
    void testshuffleDecks() {
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

        game.getGoldDeck().getCards().add(new ResourceCard());
        game.getGoldDeck().getCards().getFirst().setId(5);
        game.getGoldDeck().getCards().add(new ResourceCard());
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
    }

}
