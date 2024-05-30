package it.polimi.ingsw.network;

import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.rmi.RMIServerApp;
import it.polimi.ingsw.network.socket.server.GameServer;

import java.rmi.RemoteException;


public class GameManagerServer {
    private static GameSessionManager sessionManager;

    public static void main(String[] args) {
        sessionManager = GameSessionManager.getInstance();

        // Creazione del thread per il server socket
        Thread socketServerThread = new Thread(() -> {
            GameServer server = new GameServer(sessionManager);
            server.startServer();
        });

        // Creazione del thread per il server RMI
        Thread rmiServerThread = new Thread(() -> {
            RMIServerApp rmiServer = new RMIServerApp(sessionManager);
            try {
                rmiServer.startServer();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        // Avvio dei thread
        socketServerThread.start();
        rmiServerThread.start();

        try {
            // Attendere la terminazione di entrambi i server (opzionale)
            socketServerThread.join();
            rmiServerThread.join();
        } catch (InterruptedException e) {
            System.err.println("Server thread interrupted: " + e.getMessage());
        }
    }
}
