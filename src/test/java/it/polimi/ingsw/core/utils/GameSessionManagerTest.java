package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.model.GameSession;
import jdk.dynalink.beans.StaticClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameSessionManagerTest {
    private GameSessionManager game;
    private static GameSessionManager instance;
    private Map<String, GameSession> sessions;

    @BeforeEach
    void SetUp() {
        sessions = new LinkedHashMap<>();
        game = GameSessionManager.getInstance();
    }

    @Test
    void testgetInstance() {
        assertEquals(game, GameSessionManager.getInstance());
    }

    @Test
    void testcreateNewSession() {
        try {
            game.createNewSession("gameId", "username", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, game.getAllSessions().size());
        assertThrows((Exception.class), () -> game.createNewSession("gameId", "username", 2));
        game.removeSession("gameId");
        assertEquals(0, game.getAllSessions().size());
        game.removeSession("gameId");
    }

    @Test
    void testjoinSession() throws RemoteException {
        game.createNewSession("gameId2", "username", 2);
        game.joinSession("gameId2", "username2");
        assertEquals(0, game.getSession("gameId2").availableSlots());
        assertNull(game.joinSession("gameI", "username"));
    }


}
