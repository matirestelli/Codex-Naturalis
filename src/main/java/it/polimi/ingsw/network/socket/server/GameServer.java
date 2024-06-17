package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.GameClientProxy;
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

    public GameControllerRemote createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException {
        System.out.println("\nCreating new game session '" + gameId + "' with " + desiredPlayers + " players");
        GameControllerRemote gc = gameSessionManager.createNewSession(gameId, username, desiredPlayers);
        gc.addObserver(username, observer);
        return gc;
    }

    public synchronized String listGameSessions() {
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

    @Override
    public void registerClient(GameObserver client) throws RemoteException {
        System.out.println("New client registered.");
    }

    public GameControllerRemote joinSession(String gameId, String username, GameObserver observer) throws RemoteException {
        GameControllerRemote gc = gameSessionManager.joinSession(gameId, username);
        gc.addObserver(username, observer);

        return gc;
    }

    public boolean allPlayersConnected(String gameId) {
        GameSession session = gameSessionManager.getSession(gameId);
        return session.allPlayersConnected();
    }

    public boolean isUsernameTaken(String username) {
        return players.contains(username);
    }

    public void addUsername(String username) {
        players.add(username);
    }

    /* public void playerSelectsCard(String gameId, String username, CardSelection card) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        GameController gameController = session.getGameController();
        gameController.playerSelectsCard(username, card);
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
    public void assignStarterSide(String gameId, String username, StarterSide card) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        GameController gameController = session.getGameController();
        gameController.assignStarterSide(username, card);
    }
    public void drawCard(String gameId, String username, DrawCard card) throws RemoteException {
        GameSession session = gameSessionManager.getSession(gameId);
        GameController gameController = session.getGameController();
        gameController.drawCard(username, card);
    } */
}
