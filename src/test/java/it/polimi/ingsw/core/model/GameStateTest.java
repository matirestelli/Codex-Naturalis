package it.polimi.ingsw.core.model;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
    void testGameState() {
        GameState game = new GameState();
        assertNotNull(game);
    }

    @Test
    void testgetPlayerState() {
        assertTrue(game.getPlayerStates().containsValue(game.getPlayerState(player.getUsername())));
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

}
