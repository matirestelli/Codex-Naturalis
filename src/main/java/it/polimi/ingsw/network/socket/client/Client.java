package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.core.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Scanner scanner = new Scanner(System.in);

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void start(String[] args) throws IOException, ClassNotFoundException {
        System.out.print("Enter your username: ");
        // String in = scanner.nextLine();
        // outputStream.writeObject(in);
        System.out.println(args[0]);
        outputStream.writeObject(args[0]);

        // wait for server response (join/create)
        String message = (String) inputStream.readObject();
        System.out.print(message);

        // in = scanner.nextLine();
        System.out.println(args[1]);
        outputStream.writeObject(args[1]);
        // outputStream.writeObject(in);

        // TODO: remove later, for testing
        String in = args[1];
        if (in.equals("join")) {
            // get list of available game sessions
            message = (String) inputStream.readObject();
            System.out.println(message);
            System.out.print("Enter game id to join: ");
            // send gameId to server
            // in = scanner.nextLine();
            outputStream.writeObject(args[2]);
            // outputStream.writeObject(in);
        } else if (in.equals("create")) {
            // create new game session
            System.out.print("Enter the game id: ");
            // in = scanner.nextLine();
            // outputStream.writeObject(in);
            System.out.println(args[2]);
            outputStream.writeObject(args[2]);

            System.out.print("Insert number of players: ");
            // int id = scanner.nextInt();
            // outputStream.writeObject(id);
            System.out.println(args[3]);
            outputStream.writeObject(args[3]);
        }

        System.out.println("\nWaiting for server updates...");
        listenForUpdates();
    }

    public void listenForUpdates() {
        try {
            while (!socket.isClosed()) {
                Object data = inputStream.readObject();
                if (data instanceof GameEvent) {
                    handleEvent((GameEvent) data);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection closed or error receiving data: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void handleEvent(GameEvent event) {
        switch (event.getType()) {
            case "loadedStarterDeck", "loadedResourceDeck" -> {
                List<ResourceCard> cards = (List<ResourceCard>) event.getData();
                System.out.println("Received cards: ");
                for (ResourceCard card : cards) {
                    System.out.println(card.getId());
                }
            }
            case "chooseObjective" -> {
                List<Objective> objectives = (List<Objective>) event.getData();
                System.out.println("Extracted objective: ");
                for (Objective objective : objectives) {
                    System.out.println(objective.getId());
                }
                System.out.print("Choose one: ");
                int cardId = scanner.nextInt();
                List<Integer> ids = new ArrayList<>();
                for(Objective objective : objectives){
                    ids.add(objective.getId());
                }
                while(!ids.contains(cardId)){
                    System.out.print("Choose one: ");
                    cardId = scanner.nextInt();
                }
                try {
                    outputStream.writeObject(new SecreteObjectiveCard(cardId));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "loadedObjective" -> {
                List<CardGame> objectives = (List<CardGame>) event.getData();
                System.out.println("Received objectives: ");
                for (CardGame objective : objectives) {
                    System.out.println(objective.getId());
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
                    outputStream.writeObject(new CardSelection(cardId, true));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "lastTurn" ->{
                System.out.println("Last turn! Select a card ID to play: ");
                int cardId = scanner.nextInt();
                try {
                    outputStream.writeObject(new CardSelection(cardId, false));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "endGame" -> {
                System.out.println("Game over!");
                closeConnection();
            }
        }
    }

    private void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing connections: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 12345);
            client.start(args);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
