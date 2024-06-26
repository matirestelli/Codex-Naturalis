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

/**
 * This class implements the GameServer interface and provides the server-side logic for the game.
 * It manages the game sessions and handles the communication with the clients.
 */
public class GameServerImpl extends UnicastRemoteObject implements it.polimi.ingsw.network.GameServer {
    private GameSessionManager gameSessionManager;

    /**
     * Constructs a new GameServerImpl with the specified game session manager.
     *
     * @param gameSessionManager the game session manager
     * @throws RemoteException if the exportObject call fails
     */
    public GameServerImpl(GameSessionManager gameSessionManager) throws RemoteException {
        this.gameSessionManager = gameSessionManager;
    }

    /**
     * Registers a new client.
     *
     * @param client the client to register
     * @throws RemoteException if the remote invocation fails
     */
    @Override
    public void registerClient(GameObserver client) throws RemoteException {
        System.out.println("\nNew client registered on RMI server: " + client + "...");
    }

    /**
     * Prints a message to the console.
     *
     * @param message the message to print
     */
    public void print(String message) {
        System.out.println(message);
    }

    /**
     * Checks if the specified username is already taken.
     *
     * @param username the username to check
     * @return true if the username is taken, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        return players.contains(username);
    }

    /**
     * Adds a new username to the list of taken usernames.
     *
     * @param username the username to add
     */
    public void addUsername(String username) {
        players.add(username);
    }

    /**
     * Creates a new game session.
     *
     * @param gameId the ID of the game session
     * @param username the username of the player
     * @param desiredPlayers the number of players in the game session
     * @param observer the observer to notify of updates
     * @return the remote game controller for the game session
     * @throws RemoteException if the remote invocation fails
     */
    @Override
    public GameControllerRemote createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException {
        GameControllerRemote gc = gameSessionManager.createNewSession(gameId, username, desiredPlayers);
        gc.addObserver(username, observer);
        return gc;
    }

    /**
     * Allows a player to join an existing game session.
     *
     * @param gameId the ID of the game session to join
     * @param username the username of the player
     * @param observer the observer to notify of updates
     * @return the remote game controller for the game session
     * @throws RemoteException if the remote invocation fails
     */
    @Override
    public GameControllerRemote joinSession(String gameId, String username, GameObserver observer) throws RemoteException {
        GameControllerRemote gc = gameSessionManager.joinSession(gameId, username);
        gc.addObserver(username, observer);
        return gc;
    }

    /**
     * Lists all available game sessions.
     *
     * @return a string representation of all available game sessions
     * @throws RemoteException if the remote invocation fails
     */
    public String listGameSessions() throws RemoteException{
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

    /**
     * Lists all available game sessions, including those that are full.
     *
     * @return a string representation of all available game sessions
     * @throws RemoteException if the remote invocation fails
     */
    public synchronized String listGameSessionsComplete() throws RemoteException{
        Map<String, GameSession> sessions = gameSessionManager.getAllSessions();
        StringBuilder sb = new StringBuilder("Available game sessions:\n");
        for (GameSession session : sessions.values()) {
            sb.append("\tID: ").append(session.getGameId())
                    .append("\t|\tAvailable places: ").append(session.availableSlots())
                    .append("\n");
        }
        return sb.toString();
    }

    /**
     * Checks if all players in a game session are connected.
     *
     * @param gameId the ID of the game session to check
     * @return true if all players are connected, false otherwise
     * @throws RemoteException if the remote invocation fails
     */
    public boolean allPlayersConnected(String gameId) throws RemoteException{
        GameSession session = gameSessionManager.getSession(gameId);
        return session.allPlayersConnected();
    }
}