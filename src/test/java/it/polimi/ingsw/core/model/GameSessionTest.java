package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.GameSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    private GameSession gameSession;
    private final String gameId = "testGameId";
    private final int desiredPlayers = 4;

    @BeforeEach
    void setUp() throws RemoteException {
        gameSession = new GameSession(gameId, desiredPlayers);
    }

    @Test
    void testGetGameController() {
        assertNotNull(gameSession.getGameController());
    }

    @Test
    void testAvailableSlots() {
        assertEquals(desiredPlayers, gameSession.availableSlots());

        gameSession.addPlayer("player1");
        assertEquals(desiredPlayers - 1, gameSession.availableSlots());

        gameSession.addPlayer("player2");
        assertEquals(desiredPlayers - 2, gameSession.availableSlots());
    }

    @Test
    void testGetGameId() {
        assertEquals(gameId, gameSession.getGameId());
    }

    @Test
    void testAddPlayer() {
        assertDoesNotThrow(() -> gameSession.addPlayer("player1"));
        assertEquals(desiredPlayers - 1, gameSession.availableSlots());

        gameSession.addPlayer("player2");
        gameSession.addPlayer("player3");
        gameSession.addPlayer("player4");

        assertEquals(0, gameSession.availableSlots());
        assertTrue(gameSession.allPlayersConnected());
    }

    @Test
    void testAllPlayersConnected() {
        assertFalse(gameSession.allPlayersConnected());

        gameSession.addPlayer("player1");
        gameSession.addPlayer("player2");
        gameSession.addPlayer("player3");
        gameSession.addPlayer("player4");

        assertTrue(gameSession.allPlayersConnected());
    }
}
