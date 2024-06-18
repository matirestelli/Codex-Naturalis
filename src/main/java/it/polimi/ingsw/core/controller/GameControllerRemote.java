package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the methods that can be called remotely by the client.
 * It extends the Remote interface for RMI (Remote Method Invocation) functionality.
 */
public interface GameControllerRemote extends Remote {

    /**
     * This method is called by the client to start the game.
     * @throws RemoteException if there is a problem with the connection.
     */
    void startGame() throws RemoteException;

    /**
     * Notifies the current player about their turn.
     * @throws RemoteException if there is a problem with the connection.
     */
    void notifyCurrentPlayerTurn() throws RemoteException;

    /**
     * Advances the game to the next turn.
     * @throws RemoteException if there is a problem with the connection.
     */
    void advanceTurn() throws RemoteException;

    /**
     * Adds an observer to the game.
     * @param username Username of the player.
     * @param observer The observer to be added.
     * @throws RemoteException if there is a problem with the connection.
     */
    void addObserver(String username, GameObserver observer) throws RemoteException;

    /**
     * Removes an observer from the game.
     * @param observer The observer to be removed.
     * @throws RemoteException if there is a problem with the connection.
     */
    void removeObserver(GameObserver observer) throws RemoteException;

    /**
     * Handles a move made by a player.
     * @param username Username of the player.
     * @param event The event representing the move.
     * @throws RemoteException if there is a problem with the connection.
     */
    void handleMove(String username, MessageClient2Server event) throws RemoteException;

    void exitGame(String username) throws RemoteException;

    /**
     * Processes all the moves made by the players.
     * @throws RemoteException if there is a problem with the connection.
     */
    void processMoves() throws RemoteException;
}