package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.message.request.MessageServer2Client;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.network.ClientAbstract;
import it.polimi.ingsw.ui.GUI.GUI;
import it.polimi.ingsw.ui.TextUserInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class represents a client in the network communication via socket.
 * It is responsible for sending and receiving messages to/from the server.
 * It also manages the user interface strategy (CLI or GUI).
 */
public class Client extends ClientAbstract {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String username;
    private Thread pingThread;
    private Boolean firstGui = true;
    private GUI playerGui;
    private String serverAddres;

    /**
     * Constructor for the client.
     * @param modelView the model view
     * @param host the host address
     * @param port the port number
     * @throws IOException if an I/O error occurs when creating the socket
     */
    public Client(ModelView modelView, String host, int port) throws IOException {
        super(modelView);

        this.socket = new Socket(host, port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.serverAddres = host;

       /* if (opt.equals("cli")) {
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

        */
        this.uiStrategy = new TextUserInterface(this);
    }

    /**
     * Starts the client and establishes a connection with the server.
     * It also handles the user's choice to join or create a game session.
     *
     * @param arg The argument to determine if the client is reconnecting.
     * @param usernameask The username of the client.
     * @throws IOException If there is an error in the input or output stream.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public void start(String arg, String usernameask) throws IOException, ClassNotFoundException {
        if(arg == "true") {
            if(usernameask.endsWith("reconnected")) {
                usernameask = usernameask.substring(0, username.length()-11);
            }
            username = usernameask;
            outputStream.writeObject(usernameask+"reconnected");
        }
        else {
            username = uiStrategy.askUsername();
            outputStream.writeObject(username);
        }

            // wait for server asking (join/create)
            String message = (String) inputStream.readObject();
            System.out.print(message);

            while (!message.contains("join/create")) {
                username = uiStrategy.askUsername();
                outputStream.writeObject(username);
                message = (String) inputStream.readObject();
                System.out.print(message);
            }

        String joinCreate = uiStrategy.askJoinCreate();
        outputStream.writeObject(joinCreate);

        // TODO: remove later, for testing

       // String in = args[1];
        String in = joinCreate;
        StringBuilder filteredResult = new StringBuilder();
        message = (String) inputStream.readObject();
        if(in.equals("join")) {
            String[] lines = message.split("\n");
            for (String line : lines) {
                if (!line.contains("Available places: 0")) {
                    filteredResult.append(line).append("\n");
                }
            }
            if (!filteredResult.toString().contains("ID")) {
                System.out.println("No game sessions available, creating a new one...");
                in = "create";
            }
        }
        if (in.equals("join")) {
            // get list of available game sessions
            System.out.println(filteredResult.toString());
            System.out.print("Enter game id to join: ");
            String gameId = uiStrategy.askGameId(in, message);
            outputStream.writeObject(gameId);
        } else if (in.equals("create")) {
            // create new game session
            System.out.print("Enter the game id: ");

            String gameId = uiStrategy.askGameId(in, message);
            outputStream.writeObject(gameId);

            System.out.print("Insert number of players (2-4): ");
            int numberOfPlayers = uiStrategy.askNumberOfPlayers();
            outputStream.writeObject(numberOfPlayers);
        }

        //todo ask cli or gui
        String opt = uiStrategy.askUI();
        System.out.println(opt);
        if (opt.equals("cli")) {
            this.uiStrategy = new TextUserInterface(this);
        }
        else {
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
        listenForUpdates();
    }

    /**
     * Listens for updates from the server.
     */
    public void listenForUpdates() {
        try {
            while (!socket.isClosed()) {
                Object data = inputStream.readObject();
                if (data instanceof MessageServer2Client message) {
                    message.doAction(this);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection closed or error receiving data: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /**
     * Closes the connection with the server.
     */
    private void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing connections: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the server.
     * @param message The message to send.
     */
    @Override
    public void sendMessage(MessageClient2Server message) {
        message.appendToType(" from " + username);
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    /**
     * Main method to start the client.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        ModelView modelView = new ModelView();
        try {
            Client client = new Client(modelView, args[0], 12345);
            client.start("false","null");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restarts the client.
     * @param bool The boolean value to determine if the client is reconnecting.
     * @param usernameask The username of the client.
     * @param ip The IP address of the server.
     */
    public static void restart(String bool, String usernameask, String ip){
        System.out.println("Reconnecting to the server..." + ip + " 12345");
        ModelView modelView = new ModelView();
        try {
            Client client = new Client(modelView, ip, 12345);
            client.start(bool, usernameask);
            //client.startPinging();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects the client from the server.
     */
    public void disconnect() {
        String usernameask = this.username;
        closeConnection();
        //pingThread.interrupt();
        System.out.println("Disconnected from the server.");
        String bool = "true";
        restart(bool,usernameask, serverAddres);
    }
}
