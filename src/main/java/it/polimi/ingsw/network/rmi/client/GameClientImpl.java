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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameClientImpl extends ClientAbstract implements GameClient {
    private GameServer server; // reference to the RMI server
    private String username; // username of the client
    private String gameId;
    private GameControllerRemote gc;
    private GameClientProxy clientProxy;
    private ExecutorService notificationExecutor;

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
        this.notificationExecutor = Executors.newSingleThreadExecutor();

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
        //System.out.print("Enter your username: ");
        // String nickname = scanner.nextLine();
        //System.out.println(args[0]);
        String username = uiStrategy.askUsername();
        while (server.isUsernameTaken(username)) {
            System.out.println("Username already taken. ");
            username = uiStrategy.askUsername();
        }

        // join/create game
        System.out.print("Do you want to join an existing game session or create a new one? (join/create): ");
        //System.out.println(args[1]);
        String in = uiStrategy.askJoinCreate();
        // String choice = scanner.nextLine();
        if (in.equals("join")) {
            // get list of available game sessions
            String listGameSessions = server.listGameSessions();
            System.out.println(listGameSessions);
            System.out.print("Enter the game id to join: ");
            gameId =  uiStrategy.askGameId(in, listGameSessions);
            //System.out.println(args[2]);
            String opt = uiStrategy.askUI();
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
            gameId = uiStrategy.askGameId(in, null);
            //System.out.println(args[2]);
            System.out.print("Insert number of players (2-4): ");
            //System.out.println(args[3]);
            int numPlayers = uiStrategy.askNumberOfPlayers();
            // int number = scanner.nextInt();
            String opt = uiStrategy.askUI();
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
            try {
                gc = server.createNewSession(gameId, username, numPlayers, clientProxy);
            } catch (RemoteException e) {
                System.out.println("Error creating the game session: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Error creating the game session: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(MessageServer2Client message) {
        notificationExecutor.submit(() -> {
            try {
                message.doAction(this);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
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
