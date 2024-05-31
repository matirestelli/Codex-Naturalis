package it.polimi.ingsw.network.rmi.client;

import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.message.request.MessageServer2Client;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.network.GameClientProxy;
import it.polimi.ingsw.network.GameServer;
import it.polimi.ingsw.network.ClientAbstract;
import it.polimi.ingsw.ui.GUI.GUI;
import it.polimi.ingsw.ui.TextUserInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GameClientImpl extends ClientAbstract implements GameClient {
    private GameServer server; // reference to the RMI server
    private String username; // username of the client
    private String gameId;
    private GameControllerRemote gc;
    private GameClientProxy clientProxy;

    @Override
    public void sendMessage(MessageClient2Server message) {
        message.appendToType(" from " + username);
        try {
            gc.handleMove(username, message);
        } catch (RemoteException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    public GameClientImpl(ModelView modelView, String host, int port, String opt) throws RemoteException {
        super(modelView);

        if (opt.equals("cli")) {
            this.uiStrategy = new TextUserInterface(this);
        } else {
            this.uiStrategy = new GUI();
            this.uiStrategy.setClient(this);
            new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(GUI.class);
                }
            }.start();
            this.uiStrategy.setViewModel(modelView);
        }

        connectToServer(host, port);
    }

    private void connectToServer(String host, int port) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (GameServer) registry.lookup("GameServer");
            this.clientProxy = new GameClientProxy(this);
            server.registerClient(clientProxy);
            System.out.println("Connected to the game server at " + host + ":" + port);
        } catch (Exception e) {
            System.out.println("Failed to connect to the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void login(String args[]) throws RemoteException {
        System.out.print("Enter your username: ");
        // String nickname = scanner.nextLine();
        System.out.println(args[0]);
        username = args[0];

        // join/create game
        System.out.print("Do you want to join an existing game session or create a new one? (join/create): ");
        System.out.println(args[1]);
        String in = args[1];
        // String choice = scanner.nextLine();
        if (in.equals("join")) {
            // get list of available game sessions
            System.out.println(server.listGameSessions());
            System.out.print("Enter the game id to join: ");
            // String gameId = scanner.nextLine();
            gameId = args[2];
            System.out.println(args[2]);
            try {
                gc = server.joinSession(gameId, username, clientProxy);
                if (server.allPlayersConnected(gameId)) {
                    System.out.println("Game is full. Waiting for it to start...");
                    gc.startGame();
                }
                else
                    System.out.println("Waiting for more players to join...");
            } catch (RemoteException e) {
                System.out.println("Error joining the game: " + e.getMessage());
            }
        } else if (in.equals("create")) {
            System.out.print("Enter the game id: ");
            gameId = args[2];
            System.out.println(args[2]);
            // gameId = scanner.nextLine();
            System.out.print("Insert number of players: ");
            System.out.println(args[3]);
            int numPlayers = Integer.parseInt(args[3]);
            // int number = scanner.nextInt();
            try {
                gc = server.createNewSession(gameId, username, numPlayers, clientProxy);
            } catch (RemoteException e) {
                System.out.println("Error creating the game session: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Error creating the game session: " + e.getMessage());
            }

            System.out.println("Waiting for server updates...");
        }
    }

    @Override
    public void update(MessageServer2Client message) throws RemoteException {
        message.doAction(this);
    }

    public static void main(String[] args) {
        ModelView modelView = new ModelView();
        try {
            GameClientImpl client = new GameClientImpl(modelView, "localhost", 1099, args[args.length - 1]);
            client.login(args);
        } catch (RemoteException e) {
            System.out.println("Error creating the client: " + e.getMessage());
        }
    }
}
