package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.message.request.MessageServer2Client;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.observers.GameObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable, GameObserver {
    private GameServer server;
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String username;
    private String gameId;
    private GameControllerRemote gc;

    public ClientHandler(Socket clientSocket, GameServer server) {
        this.clientSocket = clientSocket;
        this.server = server;

        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error initializing streams: " + e.getMessage());
            closeConnection();
        }
    }

    @Override
    public void run() {
        try {
            // read username from client
            username = (String) inputStream.readObject();

            // check is username is already taken
            if(!username.endsWith("reconnected")) {
                while (server.isUsernameTaken(username)) {
                    outputStream.writeObject("Username already taken. ");
                    username = (String) inputStream.readObject();
                }
                server.addUsername(username);
            }
            else{
                username = username.substring(0, username.length()-11);
            }
            System.out.println("New client '" + username + "' connected on socket from " + clientSocket.getInetAddress().getHostAddress());

            // ask client if they want to join an existing game session or create a new one
            outputStream.writeObject("Do you want to join an existing game session or create a new one? (join/create): ");
            // read client response
            String response = (String) inputStream.readObject();
            if (response.equals("join")) {
                // send list of available game sessions
                outputStream.writeObject(server.listGameSessionsComplete());
                // read gameId from client
                gameId = (String) inputStream.readObject();
                // add client as observer of the game session
                if(server.listGameSessions().contains("ID: "+gameId)) {
                    try {
                        gc = server.joinSession(gameId, username, this);
                    } catch (Exception e) {
                        System.out.println("Error joining game session: " + e.getMessage());
                    }
                    if (server.allPlayersConnected(gameId)) {
                        System.out.println("Game is full. Waiting for it to start...");
                        // server.getGameController(gameId);
                        gc.startGame();
                    } else
                        System.out.println("Waiting for more players to join...");
                }
                else{
                    int numPlayers = (int) inputStream.readObject();
                    // create new game session
                    gc = server.createNewSession(gameId, username, numPlayers, this);
                }
            } else if (response.equals("create")) {
                outputStream.writeObject(server.listGameSessionsComplete());
                // wait for game id
                gameId = (String) inputStream.readObject();
                // wait for number of players
                //int numPlayers = Integer.parseInt((String) inputStream.readObject());
                int numPlayers = (int) inputStream.readObject();
                // create new game session
                gc = server.createNewSession(gameId, username, numPlayers, this);
            }

        /* try {
            // read username from client
            username = (String) inputStream.readObject();

            // check is username is already taken
            /* if (server.isUsernameTaken(username)) {
                outputStream.writeObject("Username already taken. Please choose another one: ");
                username = (String) inputStream.readObject();
            }*/

            /*System.out.println("New client '" + username + "' connected on socket from " + clientSocket.getInetAddress().getHostAddress());

            // ask client if they want to join an existing game session or create a new one
            outputStream.writeObject("Do you want to join an existing game session or create a new one? (join/create): ");
            // read client response
            String response = (String) inputStream.readObject();
            if (response.equals("join")) {
                // send list of available game sessions
                outputStream.writeObject(server.listGameSessions());
                // read gameId from client
                gameId = (String) inputStream.readObject();
                // add client as observer of the game session
                try {
                    gc = server.joinSession(gameId, username, this);
                } catch (Exception e) {
                    System.out.println("Error joining game session: " + e.getMessage());
                }
                if (server.allPlayersConnected(gameId)) {
                    System.out.println("Game is full. Waiting for it to start...");
                    // server.getGameController(gameId);
                    gc.startGame();
                }
                else
                    System.out.println("Waiting for more players to join...");
            } else if (response.equals("create")) {
                // wait for game id
                gameId = (String) inputStream.readObject();
                // wait for number of players
                int numPlayers = (int) inputStream.readObject();
                // create new game session
                gc = server.createNewSession(gameId, username, numPlayers, this);
            } */

            while (true) {
                // receive messages from client
                gc.handleMove(username, (MessageClient2Server) inputStream.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error communicating with client: " + e.getMessage());
            System.out.println("Client disconnected: " + username);
            try {
                gc.exitGame(username);
            } catch (Exception ex) {
                System.out.println("Error exiting game: " + ex.getMessage());
            }
        } finally {
            // closeConnection();
        }
    }

    @Override
    public void update(MessageServer2Client event) {
        try {
            outputStream.writeObject(event);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error sending event to client: " + e.getMessage());
            try {
                gc.exitGame("error");
            }catch (RemoteException ex){
                System.out.println("Error exiting game: " + ex.getMessage());
            }
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing client connection: " + e.getMessage());
        }
    }
}