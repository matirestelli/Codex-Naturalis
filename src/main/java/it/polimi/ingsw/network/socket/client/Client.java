package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.ui.GraphicalUserInterface;
import it.polimi.ingsw.ui.TextUserInterface;
import it.polimi.ingsw.ui.UserInterfaceStrategy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Scanner scanner = new Scanner(System.in);
    private UserInterfaceStrategy uiStrategy;

    private ResourceCard starterCard;
    private List<Card> hand;
    private List<Card> codex;
    private Map<Resource, Integer> resources;
    private Card cardToPlay;

    public Client(String host, int port, UserInterfaceStrategy uiStrategy) throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        this.codex = new ArrayList<>();
        this.hand = new ArrayList<>();

        this.uiStrategy = uiStrategy;
        this.uiStrategy.initialize();
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

        System.out.println("\nWaiting for server updates...\n\n");
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
            case "notYourTurn" -> {
                System.out.println("Not your turn! Wait for your turn...");
            }
            case "loadedStarter"-> {
                starterCard = (ResourceCard) event.getData();
                // TODO: change parameters and set as class attribute
                starterCard.setCentre(new Coordinate(10 / 2 * 7 - 5,10 / 2 * 3 - 5));
                codex.add(starterCard);
                uiStrategy.displayCard(starterCard);
                uiStrategy.displayStarterCardBack(starterCard);
            }
            case "starterSide"-> {
                System.out.print("Choose front side or back side of starter card (f/b): ");
                String input = scanner.nextLine();
                while (!input.equals("f") && !input.equals("b")) {
                    System.out.print("Invalid Input! Retry: ");
                    input = scanner.nextLine();
                }

                starterCard.setSide(input.equals("f"));
                try {
                    outputStream.writeObject(new GameEvent("starterSideSelection", input.equals("f")));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }

                // place starter card on the board
                uiStrategy.placeCard(starterCard, null);

                // display board
                uiStrategy.displayBoard();
            }
            case "askWhereToDraw"-> {
                List<Integer> ids = (List<Integer>) event.getData();
                for(int id : ids) {
                    System.out.println(id);
                }
                System.out.println("Vuoi perscare una di queste carte o vuoi pescare da A (Resource) o da B (Gold)?");
                String input = scanner.nextLine();
                while (!input.equals("A") && !input.equals("B") && !ids.contains(parseInt(input))) {
                    System.out.print("Input non valido, riprova: ");
                    input = scanner.nextLine();
                }
                try {
                    outputStream.writeObject(new DrawCard(input));
                    System.out.println("Wait for your turn...");
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "loadedCommonObjective" -> {
                List<Objective> objectives = (List<Objective>) event.getData();
                System.out.println("Game's Common objectives: ");
                for (Objective objective : objectives) {
                    // TODO: Fix and implement displayObjective method
                    System.out.println(objective.getId());
                }
                System.out.println("\n");
            }
            case "chooseObjective" -> {
                System.out.println("Secret objectives received:");
                List<Objective> objectives = (List<Objective>) event.getData();
                for (Objective objective : objectives) {
                    // TODO: Fix and implement displayObjective method
                    System.out.println(objective.getId());
                }
                uiStrategy.displayMessage("Choose an objective card to keep: ");
                List<Integer> ids = new ArrayList<>();
                for(Objective objective : objectives){
                    ids.add(objective.getId());
                }

                int cardId = scanner.nextInt();
                while(!ids.contains(cardId)){
                    uiStrategy.displayMessage("Invalid Input! Retry: ");
                    cardId = scanner.nextInt();
                }
                scanner.nextLine();
                // get card from objective list given the id
                int finalCardId = cardId;
                Objective card = objectives.stream().filter(o -> o.getId() == finalCardId).findFirst().orElse(null);

                try {
                    outputStream.writeObject(new GameEvent("secretObjectiveSelection", card));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "askAngle" -> {
                String input = uiStrategy.displayAngle((List<Coordinate>) event.getData());

                // get card from player's hand by id
                // TODO: create object for handling card selection
                String[] splitCardToPlay = input.split("\\.");
                int cardToAttachId = Integer.parseInt(splitCardToPlay[0]);
                int cornerSelected = Integer.parseInt(splitCardToPlay[1]);

                // card where to attach the selected card
                Card targetCard = codex.stream().filter(c -> c.getId() == cardToAttachId).findFirst().orElse(null);
                if (cornerSelected == 0)
                    uiStrategy.placeCard(cardToPlay, uiStrategy.placeBottomLeft(targetCard, cardToPlay));
                else if (cornerSelected == 1)
                    uiStrategy.placeCard(cardToPlay, uiStrategy.placeTopLeft(targetCard, cardToPlay));
                else if (cornerSelected == 2)
                    uiStrategy.placeCard(cardToPlay, uiStrategy.placeTopRight(targetCard, cardToPlay));
                else
                    uiStrategy.placeCard(cardToPlay, uiStrategy.placeBottomRight(targetCard, cardToPlay));

                uiStrategy.displayBoard();

                try {
                    outputStream.writeObject(new GameEvent("angleSelection", new CardToAttachSelected(input)));
                } catch (IOException e) {
                    System.out.println("Error sending angles: " + e.getMessage());
                }
            }
            case "assignedStarterCard" -> {
                ResourceCard card = (ResourceCard) event.getData();
                System.out.println("Setted starter card: " + card.getId());
            }
            case "updateHand" -> {
                hand = (List<Card>) event.getData();
                for (Card card : hand) {
                    uiStrategy.displayCard(card);
                    uiStrategy.displayCardBack(card);
                }
            }
            case "updateCodex" -> {
                codex = (List<Card>) event.getData();
            }
            case "beforeTurnEvent" -> {
                resources = (Map<Resource, Integer>) event.getData();
            }
            case "currentPlayerTurn" -> {
                System.out.println("It's your turn!");
                PlayableCardIds ids = (PlayableCardIds) event.getData();
                for (Card card : hand) {
                    if (ids.getPlayingHandIds().contains(card.getId()))
                        uiStrategy.displayCard(card);
                    uiStrategy.displayCardBack(card);
                }
                CardSelection cs = uiStrategy.askCardSelection(ids.getPlayingHandIds(), ids.getPlayingHandIdsBack());
                cardToPlay = hand.stream().filter(c -> c.getId() == cs.getId()).findFirst().orElse(null);
                try {
                    outputStream.writeObject(new GameEvent("cardSelection", cs));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
                hand.remove(cardToPlay);
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
        UserInterfaceStrategy uiStrategy;

        String opt = "cli";
        if (opt.equals("cli")) {
            uiStrategy = new TextUserInterface();
        } else {
            uiStrategy = new GraphicalUserInterface();
        }

        try {
            Client client = new Client("localhost", 12345, uiStrategy);
            client.start(args);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
