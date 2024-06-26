package it.polimi.ingsw.network.rmi.server;

import it.polimi.ingsw.network.rmi.client.GameClient;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the methods that a game server should implement.
 */
public interface GameServer extends Remote {

    /**
     * Registers a client with the server.
     *
     * @param client the client to register
     * @throws RemoteException if the remote invocation fails
     */
    void registerClient(GameClient client) throws RemoteException;

    /**
     * Creates a new game session.
     *
     * @param gameId the ID of the game session to create
     * @param username the username of the player creating the session
     * @param desiredPlayers the number of players desired for the game session
     * @param observer the observer to notify of updates
     * @throws RemoteException if the remote invocation fails
     */
    void createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException;

    /**
     * Allows a player to join an existing game session.
     *
     * @param gameId the ID of the game session to join
     * @param username the username of the player
     * @param observer the observer to notify of updates
     * @throws RemoteException if the remote invocation fails
     */
    void joinGameSession(String gameId, String username, GameObserver observer) throws RemoteException;

    /**
     * Prints a message to the server console.
     *
     * @param message the message to print
     * @throws RemoteException if the remote invocation fails
     */
    void print(String message) throws RemoteException;

    /**
     * Lists all available game sessions.
     *
     * @return a string representation of all available game sessions
     * @throws RemoteException if the remote invocation fails
     */
    String listGameSessions() throws RemoteException;
}
