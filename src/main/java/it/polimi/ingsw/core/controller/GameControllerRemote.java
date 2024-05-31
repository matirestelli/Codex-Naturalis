package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameControllerRemote extends Remote {
    void startGame() throws RemoteException;

    // void playerSelectsCard(String username, CardSelection cardSelection) throws RemoteException;

    void notifyCurrentPlayerTurn() throws RemoteException;

    void advanceTurn() throws RemoteException;

    void addObserver(String username, GameObserver observer) throws RemoteException;

    void removeObserver(GameObserver observer) throws RemoteException;

    // void notifyObservers(GameEvent event) throws RemoteException;

    void handleMove(String username, MessageClient2Server event) throws RemoteException;

    void processMoves() throws RemoteException;

    // void processMove(String username, GameMessage message) throws RemoteException;

    // void addClient(String username, ClientAbstract client);
    // void advanceTurn() throws RemoteException;
}