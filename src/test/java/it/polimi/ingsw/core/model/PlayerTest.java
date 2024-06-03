package it.polimi.ingsw.core.model;

import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

        @Test
        void setUsername() {
            Player player = new Player("test");
            player.setUsername("test2");
            assertEquals("test2", player.getUsername());
        }

        @Test
        void getUsername() {
            Player player = new Player("test");
            assertEquals("test", player.getUsername());
        }

        @Test
        void getCurrentSession() throws RemoteException {
            Player player = new Player("test");
            GameSession gameSession = new GameSession("test", 4);
            player.setCurrentSession(gameSession);
            assertEquals(gameSession, player.getCurrentSession());
        }

        @Test
        void setCurrentSession() throws RemoteException {
            Player player = new Player("test");
            GameSession gameSession = new GameSession("test", 4);
            player.setCurrentSession(gameSession);
            assertEquals(gameSession, player.getCurrentSession());
        }
}