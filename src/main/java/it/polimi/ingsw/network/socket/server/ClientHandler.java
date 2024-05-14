package it.polimi.ingsw.network.socket.server;

import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.core.model.GameEvent;
import it.polimi.ingsw.observers.GameObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable, GameObserver {
    private GameServer server;
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String username;
    private String gameId;

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
            /* if (server.isUsernameTaken(username)) {
                outputStream.writeObject("Username already taken. Please choose another one: ");
                username = (String) inputStream.readObject();
            } */
            System.out.println("New client '" + username + "' connected on socket from " + clientSocket.getInetAddress().getHostAddress());

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
                    server.joinGameSession(gameId, username, this);
                } catch (Exception e) {
                    System.out.println("Error joining game session: " + e.getMessage());
                }
                if (server.allPlayersConnected(gameId)) {
                    System.out.println("Game is full. Waiting for it to start...");
                    // server.getGameController(gameId);
                    server.startGame(gameId);
                }
                else
                    System.out.println("Waiting for more players to join...");
            } else if (response.equals("create")) {
                // wait for game id
                gameId = (String) inputStream.readObject();
                // wait for number of players
                int numPlayers = Integer.parseInt((String) inputStream.readObject());
                // create new game session
                server.createNewSession(gameId, username, numPlayers, this);
            }

            while (true) {
                // Ricevi messaggi dal client
                Object message = inputStream.readObject();
                if (message instanceof CardSelection) {
                    server.playerSelectsCard(gameId, username, (CardSelection) message);
                    // gameController.playerSelectsCard(username, (CardSelection) message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error communicating with client: " + e.getMessage());
        } finally {
            // closeConnection();
        }
    }

    @Override
    public void update(GameEvent event) {
        if (!event.getType().equals("sessionUpdate")) {
            System.out.println("Sending event to client: " + event.getType());
            System.out.println("Event data: " + event.getData());

            try {
                outputStream.writeObject(event);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Error sending event to client: " + e.getMessage());
                // closeConnection();
            }
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

