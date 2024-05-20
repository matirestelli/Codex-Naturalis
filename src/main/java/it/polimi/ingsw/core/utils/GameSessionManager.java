package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.GameSession;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameSessionManager {
    private static GameSessionManager instance;
    private Map<String, GameSession> sessions;

    private GameSessionManager() {
        sessions = Collections.synchronizedMap(new LinkedHashMap<>());
    }

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

    public synchronized GameControllerRemote createNewSession(String gameId, String username, int desiredPlayers) throws RemoteException {
        if (sessions.containsKey(gameId)) {
            throw new RemoteException("Game session already exists");
        }
        GameSession session = new GameSession(gameId, desiredPlayers);
        sessions.put(gameId, session);
        session.addPlayer(username);

        return session.getGameController();
    }

    public synchronized GameControllerRemote joinSession(String gameId, String username) {
        GameSession session = sessions.get(gameId);
        if (session != null) {
            session.addPlayer(username);
            return session.getGameController();
        }
        return null;
    }

    public synchronized GameSession getSession(String gameId) {
        return sessions.get(gameId);
    }

    public synchronized boolean removeSession(String gameId) {
        if (sessions.containsKey(gameId)) {
            sessions.remove(gameId);
            return true;
        }
        return false;
    }

    public synchronized Map<String, GameSession> getAllSessions() {
        return new LinkedHashMap<>(sessions);
    }
}
