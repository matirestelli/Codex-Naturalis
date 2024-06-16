package it.polimi.ingsw.core.model;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.socket.server.ClientHandler;
import it.polimi.ingsw.network.socket.server.GameServer;
import it.polimi.ingsw.observers.GameObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StartGameTest {

    private GameState game;
    private Map<Player, Map<Integer, Integer>> players;
    private List<Objective> commonObjectives;
    private Deck goldDeck;
    private Deck resourceDeck;
    private Deck starterDeck;
    private PlayerState player;
    private String configFile;

    private GameState gameState;
    private GameControllerRemote gameController;
    private Map<String, GameObserver> observers;
    private int currentPlayerIndex; // index of the current player
    private boolean last = false;
    private int matrixDimension;


    @BeforeEach
    void setUp() throws RemoteException {
        //setup for gamecontroller
        gameState = new GameState();
        gameController = new GameController(gameState);
        observers = new HashMap<>();
        Socket socket = new Socket();

        //setup for clienthandler
        GameSessionManager gameSessionManager = GameSessionManager.getInstance();
        GameServer gameServer = new GameServer(gameSessionManager);
        gameController = gameServer.createNewSession("us1", "us1", 1, new ClientHandler(socket, gameServer));
        ClientHandler clientHandler = new ClientHandler(socket, gameServer);
        observers.put("us1", clientHandler);

        //setup for gamestate
        commonObjectives = new ArrayList<>();
        commonObjectives.add(new Objective());
        commonObjectives.add(new Objective());
        configFile = new StringBuilder().append("src/main/resources/it/polimi/ingsw/").append(configFile).append(".json").toString();
        starterDeck = new Deck("starter", new TypeToken<List<ResourceCard>>() {
        }.getType());
        resourceDeck = new Deck("resource", new TypeToken<List<ResourceCard>>() {
        }.getType());
        goldDeck = new Deck("gold", new TypeToken<List<GoldCard>>() {
        }.getType());
        player = new PlayerState(); // Assuming Player class is properly implemented for testing
        player.setUsername("us1");
        gameState.addPlayer(player);
        gameState.addCommonObjective(new Objective());
        gameState.addCommonObjective(new Objective());
        gameState.setStarterDeck(starterDeck);
    }
    /*
    @Test
    void testStartGame() throws RemoteException {
        // Act
        gameController.startGame();
        // Assert
        assertNotNull(gameState.getPlayerOrder());
    }

     */
}
