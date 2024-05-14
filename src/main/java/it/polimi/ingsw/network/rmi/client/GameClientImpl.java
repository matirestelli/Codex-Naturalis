package it.polimi.ingsw.network.rmi.client;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.network.GameServer;
import it.polimi.ingsw.observers.GameObserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class GameClientImpl extends UnicastRemoteObject implements GameClient, GameObserver {
    private GameServer server; // reference to the RMI server
    private String username; // username of the client
    private Scanner scanner = new Scanner(System.in); // scanner to read input from the user
    private String gameId;

    public GameClientImpl(String host, int port) throws RemoteException {
        super();
        connectToServer(host, port);
    }

    private void connectToServer(String host, int port) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            server = (GameServer) registry.lookup("GameServer");
            server.registerClient(this);
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
                server.joinGameSession(gameId, username, this);
                if (server.allPlayersConnected(gameId)) {
                    System.out.println("Game is full. Waiting for it to start...");
                    server.startGame(gameId);
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
                server.createNewSession(gameId, username, numPlayers, this);
            } catch (RemoteException e) {
                System.out.println("Error creating the game session: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Error creating the game session: " + e.getMessage());
            }

            System.out.println("Waiting for server updates...");
        }
    }

    @Override
    public void update(GameEvent event) throws RemoteException {
        switch (event.getType()) {
            case "loadedStarterDeck", "loadedResourceDeck" -> {
                List<Card> cards = (List<Card>) event.getData();
                // gameSession.getGameState().setStarterCards(cards);
                // gameSession.setGameState((GameState) event.getData());
                System.out.println("Loaded starter cards!");
                for (Card card : cards) {
                    ResourceCard resourceCard = (ResourceCard) card;
                    System.out.println(resourceCard.getId());
                }
            }
            case "loadedGoldDeck" -> {
                List<GoldCard> cards = (List<GoldCard>) event.getData();
                System.out.println("Received gold cards: ");
                for (GoldCard card : cards) {
                    System.out.println(card.getId());
                }
            }
            case "assignedStarterCard" -> {
                ResourceCard card = (ResourceCard) event.getData();
                // gameSession.getGameState().setStarterCard(card));
                System.out.println("Received starter card: " + card.getId());
            }
            case "updateHand" -> {
                List<Card> cards = (List<Card>) event.getData();
                System.out.println("Updated hand: ");
                for (Card card : cards) {
                    System.out.println(card.getId());
                }
            }
            case "cardRemoved" -> System.out.println("Card " + event.getData() + " has been selected by someone.");
            case "currentPlayerTurn" -> {
                System.out.print("It's your turn! Select a card ID to play: ");
                int cardId = scanner.nextInt();
                try {
                    server.playerSelectsCard(gameId, username, new CardSelection(cardId, true));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void notify(GameEvent event) throws RemoteException {}

    public static void main(String[] args) {
        try {
            GameClientImpl client = new GameClientImpl("localhost", 1099);

            client.login(args);
        } catch (RemoteException e) {
            System.out.println("Error creating the client: " + e.getMessage());
        }
    }
}
