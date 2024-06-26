package it.polimi.ingsw.network;

import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.network.rmi.client.GameClient;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
/**
 * This interface represents a game server.
 * It provides methods for creating and joining game sessions, listing available game sessions, and registering clients.
 */
public interface GameServer extends Remote {

    public List<String> players = new ArrayList<>();

    /**
     * Creates a new game session.
     *
     * @param gameId the ID of the game session
     * @param username the username of the player
     * @param desiredPlayers the number of players desired for the game session
     * @param observer the observer to notify of updates
     * @return the game controller for the new game session
     * @throws RemoteException if the remote invocation fails
     */
    GameControllerRemote createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException;

    /**
     * Joins an existing game session.
     *
     * @param gameId the ID of the game session
     * @param username the username of the player
     * @param observer the observer to notify of updates
     * @return the game controller for the game session
     * @throws RemoteException if the remote invocation fails
     */
    GameControllerRemote joinSession(String gameId, String username, GameObserver observer) throws RemoteException;

    /**
     * Lists all available game sessions.
     *
     * @return a string representation of all available game sessions
     * @throws RemoteException if the remote invocation fails
     */
    String listGameSessions() throws RemoteException;

    /**
     * Registers a client.
     *
     * @param client the client to register
     * @throws RemoteException if the remote invocation fails
     */
    void registerClient(GameObserver client) throws RemoteException;

    /**
     * Prints a message.
     *
     * @param message the message to print
     * @throws RemoteException if the remote invocation fails
     */
    void print(String message) throws RemoteException;

    /**
     * Lists all game sessions with complete details.
     *
     * @return a string representation of all game sessions with complete details
     * @throws RemoteException if the remote invocation fails
     */
    String listGameSessionsComplete() throws RemoteException;

    /**
     * Lists all players in a game session.
     *
     * @param gameId the ID of the game session
     * @return a string representation of all players in the game session
     * @throws RemoteException if the remote invocation fails
     */
    boolean allPlayersConnected(String gameId) throws RemoteException;

    /**
     * Checks if the specified username is already taken.
     *
     * @param username the username to check
     * @return true if the username is taken, false otherwise
     * @throws RemoteException if the remote invocation fails
     */
    boolean isUsernameTaken(String username) throws RemoteException;

    /**
     * Adds a new username to the list of taken usernames.
     *
     * @param username the username to add
     * @throws RemoteException if the remote invocation fails
     */
    void addUsername(String username) throws RemoteException;
}