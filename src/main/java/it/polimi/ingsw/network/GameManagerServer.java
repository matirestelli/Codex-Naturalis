package it.polimi.ingsw.network;

import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.rmi.RMIServerApp;
import it.polimi.ingsw.network.socket.server.GameServer;

import java.rmi.RemoteException;

/**
 * This class is responsible for managing the game server.
 * It creates and starts the socket server and RMI server.
 */
public class GameManagerServer {
    private static GameSessionManager sessionManager;

    /**
     * The main method that starts the game server.
     * It creates and starts the socket server and RMI server.
     *
     * @param args the command-line arguments
     */
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
