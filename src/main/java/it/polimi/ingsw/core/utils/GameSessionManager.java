package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.GameSession;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class manages the game sessions.
 * It maintains a map of sessions and provides methods to create, join, get, and remove sessions.
 */
public class GameSessionManager {
    private static GameSessionManager instance;
    private Map<String, GameSession> sessions;

    /**
     * Private constructor for the singleton pattern.
     */
    private GameSessionManager() {
        sessions = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    /**
     * Returns the instance of the GameSessionManager.
     * @return The instance of the GameSessionManager.
     */
    public static GameSessionManager getInstance() {
        if (instance == null) {
            synchronized (GameSessionManager.class) {
                if (instance == null) {
                    instance = new GameSessionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Creates a new session and adds the player to it.
     * @param gameId The id of the game.
     * @param username The username of the player.
     * @param desiredPlayers The number of desired players.
     * @return The game controller of the session.
     * @throws RemoteException If the session already exists.
     */
    public synchronized GameControllerRemote createNewSession(String gameId, String username, int desiredPlayers) throws RemoteException {
        if (sessions.containsKey(gameId)) {
            throw new RemoteException("Game session already exists");
        }
        GameSession session = new GameSession(gameId, desiredPlayers);
        sessions.put(gameId, session);
        session.addPlayer(username);

        return session.getGameController();
    }

    /**
     * Joins a session.
     * @param gameId The id of the game.
     * @param username The username of the player.
     * @return The game controller of the session.
     */
    public synchronized GameControllerRemote joinSession(String gameId, String username) {
        GameSession session = sessions.get(gameId);
        if (session != null) {
            session.addPlayer(username);
            return session.getGameController();
        }
        return null;
    }

    /**
     * Returns a session.
     * @param gameId The id of the game.
     * @return The session.
     */
    public synchronized GameSession getSession(String gameId) {
        return sessions.get(gameId);
    }

    /**
     * Removes a session.
     * @param gameId The id of the game.
     * @return True if the session was removed, false otherwise.
     */
    public synchronized boolean removeSession(String gameId) {
        if (sessions.containsKey(gameId)) {
            sessions.remove(gameId);
            return true;
        }
        return false;
    }

    /**
     * Returns all sessions.
     * @return A map of all sessions.
     */
    public synchronized Map<String, GameSession> getAllSessions() {
        return new LinkedHashMap<>(sessions);
    }
}