package it.polimi.ingsw.network.rmi.server;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.core.model.GameSession;
import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.rmi.client.GameClient;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class GameServerImpl extends UnicastRemoteObject implements it.polimi.ingsw.network.GameServer {
    private GameSessionManager gameSessionManager;

    public GameServerImpl(GameSessionManager gameSessionManager) throws RemoteException {
        super();
        this.gameSessionManager = gameSessionManager;
    }

    public void startGame(String gameId) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        session.startGame();
    }

    @Override
    public void registerClient(GameClient client) throws RemoteException {
        System.out.println("\nNew client registered on RMI server: " + client + "...");
    }

    public synchronized void createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException {
        GameSession newSession = new GameSession(gameId, desiredPlayers);
        newSession.addPlayer(username);
        newSession.addObserver(observer);
        gameSessionManager.addSession(newSession);
    }

    @Override
    public synchronized void joinGameSession(String gameId, String username, GameObserver observer) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        if (session.allPlayersConnected()) {
            throw new RemoteException("Session is full");
        }
        session.addPlayer(username);
        session.addObserver(observer);
    }

    public String listGameSessions() {
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

    public void playerSelectsCard(String gameId, String username, CardSelection card) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        GameController gameController = session.getGameController();
        gameController.playerSelectsCard(username, card);
        gameController.advanceTurn();
    }
}