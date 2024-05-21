package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.GameEvent;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameControllerRemote extends Remote {
    void startGame() throws RemoteException;

    // void playerSelectsCard(String username, CardSelection cardSelection) throws RemoteException;

    void notifyCurrentPlayerTurn() throws RemoteException;

    // void advanceTurn() throws RemoteException;

    void addObserver(String username, GameObserver observer) throws RemoteException;

    void removeObserver(GameObserver observer) throws RemoteException;

    // void notifyObservers(GameEvent event) throws RemoteException;

    void handleMove(String username, GameEvent event) throws RemoteException;

    void processMoves() throws RemoteException;

    void advanceTurn() throws RemoteException;
}