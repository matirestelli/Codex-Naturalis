package it.polimi.ingsw.network.rmi.server;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.core.model.GameSession;
import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.GameClientProxy;
import it.polimi.ingsw.network.rmi.client.GameClient;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class GameServerImpl extends UnicastRemoteObject implements it.polimi.ingsw.network.GameServer {
    private GameSessionManager gameSessionManager;

    public GameServerImpl(GameSessionManager gameSessionManager) throws RemoteException {
        this.gameSessionManager = gameSessionManager;
    }

    @Override
    public void registerClient(GameObserver client) throws RemoteException {
        System.out.println("\nNew client registered on RMI server: " + client + "...");
    }

    public boolean isUsernameTaken(String username) {
        return players.contains(username);
    }

    public void addUsername(String username) {
        players.add(username);
    }

    @Override
    public GameControllerRemote createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException {
        GameControllerRemote gc = gameSessionManager.createNewSession(gameId, username, desiredPlayers);
        gc.addObserver(username, observer);
        return gc;
    }

    @Override
    public GameControllerRemote joinSession(String gameId, String username, GameObserver observer) throws RemoteException {
        GameControllerRemote gc = gameSessionManager.joinSession(gameId, username);
        gc.addObserver(username, observer);
        return gc;
    }

    public String listGameSessions() {
        Map<String, GameSession> sessions = gameSessionManager.getAllSessions();
        StringBuilder sb = new StringBuilder("Available game sessions:\n");
        for (GameSession session : sessions.values()) {
            if(session.availableSlots()!=0) {
                sb.append("\tID: ").append(session.getGameId())
                        .append("\t|\tAvailable places: ").append(session.availableSlots())
                        .append("\n");
            }
        }
        return sb.toString();
    }

    public synchronized String listGameSessionsComplete() {
        Map<String, GameSession> sessions = gameSessionManager.getAllSessions();
        StringBuilder sb = new StringBuilder("Available game sessions:\n");
        for (GameSession session : sessions.values()) {
            sb.append("\tID: ").append(session.getGameId())
                    .append("\t|\tAvailable places: ").append(session.availableSlots())
                    .append("\n");
        }
        return sb.toString();
    }

    public boolean allPlayersConnected(String gameId) {
        GameSession session = gameSessionManager.getSession(gameId);
        return session.allPlayersConnected();
    }
}