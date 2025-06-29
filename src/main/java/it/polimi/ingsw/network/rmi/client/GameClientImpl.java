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
/**
 * This class represents the implementation of a game client in the RMI network.
 * It extends the abstract class ClientAbstract and implements the GameClient interface.
 * It manages the connection to the server, sending and receiving messages, and user interactions.
 *
 * @author alessiovilla
 */
public class GameClientImpl extends ClientAbstract implements GameClient {
    private GameServer server; // reference to the RMI server
    private String username; // username of the client
    private String gameId;
    private GameControllerRemote gc;
    private GameClientProxy clientProxy;
    private ExecutorService notificationExecutor;
    private Boolean firstGui = true;
    private GUI playerGui;
    private static String serverAddress;

    /**
     * Sends a message from the client to the server.
     *
     * @param message The message to be sent to the server.
     */
    @Override
    public void sendMessage(MessageClient2Server message) {
        try{
        message.appendToType(" from " + username);
        try {
            gc.handleMove(username, message);
        } catch (RemoteException e) {
            try {
                server.print("Error sending message: " + e.getMessage());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            System.out.println("Error sending message: " + e.getMessage());
        }
        }catch (Exception e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            try{
            gc.exitGame("error");
            }catch (RemoteException ex) {
                System.out.println("Error exiting the game");
            }
        }
    }

    /**
     * Constructor for the GameClientImpl class.
     *
     * @param modelView The model view of the game.
     * @param host The IP address of the server.
     * @param port The port number of the server.
     * @param opt The user interface option.
     * @throws RemoteException If there is an error in the remote connection.
     */
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

        serverAddress = host;
        connectToServer(host, port);
    }

    /**
     * Disconnects the client from the server.
     */
    public void disconnect(){
            String usernameask = this.username;
            // Shutdown the notification executor
            if (notificationExecutor != null && !notificationExecutor.isShutdown()) {
                notificationExecutor.shutdownNow();
            }
            // Annulla il riferimento al server e al GameControllerRemote
            server = null;
            gc = null;
            System.out.println("Disconnected from the server.");
            String bool = "true";
            restart(bool,usernameask);
    }

    /**
     * Connects the client to the server.
     *
     * @param host The IP address of the server.
     * @param port The port number of the server.
     * @throws RemoteException If there is an error in the remote connection.
     */
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

    /**
     * Logs in the client to the server.
     *
     * @param args The arguments for the login.
     * @param usernameask The username of the client.
     * @throws RemoteException If there is an error in the remote connection.
     */
    public void login(String args, String usernameask) throws RemoteException {
        //System.out.print("Enter your username: ");
        // String nickname = scanner.nextLine();
        //System.out.println(args[0]);
        if(args == "true") {
            if(usernameask.endsWith("reconnected")) {
                usernameask = usernameask.substring(0, username.length()-11);
            }
            username = usernameask;
        }
        else {
            username = uiStrategy.askUsername();
        }
        if(!args.equals("true")) {
            while (server.isUsernameTaken(username)) {
                System.out.println("Username already taken. ");
                username = uiStrategy.askUsername();
            }
            server.addUsername(username);
        }

        // join/create game
        System.out.print("Do you want to join an existing game session or create a new one? (join/create): ");
        //System.out.println(args[1]);
        String listGameSessions = "";
        String in = uiStrategy.askJoinCreate();
        if(in.equals("join")) {
            listGameSessions = server.listGameSessions();
            if (!listGameSessions.contains("ID")) {
                System.out.println("No game sessions available, creating a new one...");
                in = "create";
            }
        }
        if (in.equals("join")) {
            // get list of available game sessions
            System.out.println(listGameSessions);
            System.out.print("Enter the game id to join: ");
            gameId =  uiStrategy.askGameId(in, listGameSessions);
            //System.out.println(args[2]);
            String opt = uiStrategy.askUI();
            if (opt.equals("cli")) {
                this.uiStrategy = new TextUserInterface(this);
            } else {
                if(firstGui) {
                    this.uiStrategy = new GUI();
                    playerGui = (GUI) this.uiStrategy;
                    firstGui = false;
                }
                else {
                    this.uiStrategy = playerGui;
                }
                this.uiStrategy.setClient(this);
                new Thread() {
                    @Override
                    public void run() {
                        //javafx.application.Application.launch(GUI.class);
                        ((GUI) uiStrategy).myLaunch(GUI.class);
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
            gameId = uiStrategy.askGameId(in, server.listGameSessionsComplete());
            //System.out.println(args[2]);
            System.out.print("Insert number of players (2-4): ");
            //System.out.println(args[3]);
            int numPlayers = uiStrategy.askNumberOfPlayers();
            // int number = scanner.nextInt();
            String opt = uiStrategy.askUI();
            if (opt.equals("cli")) {
                this.uiStrategy = new TextUserInterface(this);
            } else {
                if(firstGui) {
                    this.uiStrategy = new GUI();
                    playerGui = (GUI) this.uiStrategy;
                    firstGui = false;
                }
                else {
                    this.uiStrategy = playerGui;
                }
                this.uiStrategy.setClient(this);
                new Thread() {
                    @Override
                    public void run() {
                        //javafx.application.Application.launch(GUI.class);
                        ((GUI) uiStrategy).myLaunch(GUI.class);
                    }
                }.start();

                this.uiStrategy.setViewModel(modelView);
            }
            System.out.println("\nWaiting for server updates...\n\n");
            try {
                gc = server.createNewSession(gameId, username, numPlayers, clientProxy);
            } catch (RemoteException e) {
                System.out.println("Error creating the game session: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Error creating the game session: " + e.getMessage());
            }
        }
    }

    /**
     * Updates the client with a message from the server.
     *
     * @param message The message from the server.
     */
    @Override
    public void update(MessageServer2Client message) {
        try{
        notificationExecutor.submit(() -> {
            try {
                message.doAction(this);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        }catch (Exception e) {
            String bug = "Task java.util.concurrent.FutureTask";
            String notARealException = e.getMessage().substring(0,bug.length());
            if(!( notARealException.equals(bug))) {
                System.out.println( e.getMessage());
            }
            //System.out.println("RemoteException caught: \n" + e.getMessage());
            //todo capire perchè eccezione se esco da gui in client rmi dice che c'è un task futuro che non viene eseguito
            try{
                gc.exitGame("error");
            }catch (RemoteException ex) {
                System.out.println("Error exiting the game");
            }
        }
    }

    /**
     * Main method for the GameClientImpl class.
     *
     * @param args The arguments for the main method.
     */
    public static void main(String[] args) {
        ModelView modelView = new ModelView();
        try {
            GameClientImpl client = new GameClientImpl(modelView, args[0], 1099, "cli");
            client.login("false", "null");
        } catch (RemoteException e) {
            System.out.println("Error creating the client: " + e.getMessage());
        }
    }

    /**
     * Restarts the client.
     *
     * @param bool The boolean value for the restart.
     * @param usernameask The username of the client.
     */
    public static void restart(String bool, String usernameask) {
        System.out.println("Restarting the client... on" + serverAddress);
        ModelView modelView = new ModelView();
        try {
            GameClientImpl client = new GameClientImpl(modelView, serverAddress, 1099, "cli");
            client.login(bool, usernameask);
        } catch (RemoteException e) {
            System.out.println("Error creating the client: " + e.getMessage());
        }
    }
}
