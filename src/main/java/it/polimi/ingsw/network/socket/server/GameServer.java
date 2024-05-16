package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.core.model.CardToAttachSelected;
import it.polimi.ingsw.core.model.GameSession;
import it.polimi.ingsw.core.model.SecreteObjectiveCard;
import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.rmi.client.GameClient;
import it.polimi.ingsw.observers.GameObserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Map;

public class GameServer implements it.polimi.ingsw.network.GameServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private GameSessionManager gameSessionManager;

    public GameServer(GameSessionManager gameSessionManager) {
        this.gameSessionManager = gameSessionManager;
    }

    public void startGame(String gameId) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        session.startGame();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Game Server Socket is running on port " + PORT + "...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("\nNew client accepted on socket from " + clientSocket.getInetAddress().getHostAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    System.out.println("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not start server: " + e.getMessage());
        }
    }

    public synchronized void createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException {
        System.out.println("\nCreating new game session '" + gameId + "' with " + desiredPlayers + " players");
        GameSession newSession = new GameSession(gameId, desiredPlayers);
        newSession.addPlayer(username);
        newSession.addObserver(observer);
        gameSessionManager.addSession(newSession);
    }

    public synchronized String listGameSessions() {
        Map<String, GameSession> sessions = gameSessionManager.getAllSessions();
        StringBuilder sb = new StringBuilder("Available game sessions:\n");
        for (GameSession session : sessions.values()) {
            sb.append("\tID: ").append(session.getGameId())
                    .append("\t|\tAvailable places: ").append(session.availableSlots())
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public void registerClient(GameClient client) throws RemoteException {
        System.out.println("New client registered.");
    }

    public void joinGameSession(String gameId, String username, GameObserver observer) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        if (session.allPlayersConnected()) {
            throw new RemoteException("Session is full");
        }
        session.addPlayer(username);
        session.addObserver(observer);
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
    public void chooseObjective(String gameId, String username, SecreteObjectiveCard card) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        GameController gameController = session.getGameController();
        gameController.chooseObjective(username, card);
    }
    public void angleChosen(String gameId, String username, CardToAttachSelected card) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        GameController gameController = session.getGameController();
        gameController.angleChosen(username, card);
    }
}
