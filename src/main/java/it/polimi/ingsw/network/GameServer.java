package it.polimi.ingsw.network;

import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.network.rmi.client.GameClient;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GameServer extends Remote {

    public List<String> players = new ArrayList<>();
    // Metodo per creare una nuova sessione di gioco
    GameControllerRemote createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException;

    // Metodo per unirsi a una sessione di gioco esistente
    GameControllerRemote joinSession(String gameId, String username, GameObserver observer) throws RemoteException;

    // Metodo per elencare tutte le sessioni di gioco disponibili
    String listGameSessions() throws RemoteException;

    // Metodo per registrare un client
    void registerClient(GameObserver client) throws RemoteException;

    boolean allPlayersConnected(String gameId) throws RemoteException;
}