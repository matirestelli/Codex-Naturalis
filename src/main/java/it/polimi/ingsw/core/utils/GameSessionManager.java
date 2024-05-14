package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.model.GameSession;

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

    public synchronized void addSession(GameSession session) {
        sessions.put(session.getGameId(), session);
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
