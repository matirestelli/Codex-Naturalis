package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.ui.GraphicalUserInterface;
import it.polimi.ingsw.ui.ObserverUI;
import it.polimi.ingsw.ui.TextUserInterface;
import it.polimi.ingsw.ui.UserInterfaceStrategy;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Scanner scanner = new Scanner(System.in);
    private String username;
    private int unreadedMessages;
    private boolean inCurrentTurn;

    private UserInterfaceStrategy uiStrategy;

    private ResourceCard starterCard;
    private Chat chat;
    private List<Card> hand;
    private List<Card> codex;
    private Map<Resource, Integer> resources;
    private Card cardToPlay;

    private ObserverUI observerUI;

    public Client(String host, int port, String opt) throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        this.codex = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.chat = new Chat();

        this.unreadedMessages = 0;
        this.inCurrentTurn = false;

        opt = "cli";
        if (opt.equals("cli")) {
            this.uiStrategy = new TextUserInterface(this);
        } else {
            this.uiStrategy = new GraphicalUserInterface();
        }

        this.uiStrategy.initialize();
    }

    public void setObserverUI(ObserverUI observerUI) {
        this.observerUI = observerUI;
    }

    public void start(String[] args) throws IOException, ClassNotFoundException {

        String username = uiStrategy.askUsername();
        String joinCreate = uiStrategy.askJoinCreate();
        //username = args[0];
        outputStream.writeObject(username);

        // wait for server response (join/create)
        String message = (String) inputStream.readObject();
        System.out.print(message);

        // in = scanner.nextLine();
        System.out.println(joinCreate);
        outputStream.writeObject(joinCreate);
        // outputStream.writeObject(in);

        // TODO: remove later, for testing
        String in = joinCreate;
        String gameId = uiStrategy.askGameId(joinCreate);
        if (in.equals("join")) {
            // get list of available game sessions
            message = (String) inputStream.readObject();
            System.out.println(message);
            // send gameId to server
            // in = scanner.nextLine();
            outputStream.writeObject(gameId);
            //outputStream.writeObject(args[3]);
            // outputStream.writeObject(in);
        } else if (in.equals("create")) {
            // create new game session
            // in = scanner.nextLine();
            // outputStream.writeObject(in);
            outputStream.writeObject(gameId);
            //outputStream.writeObject(args[3]);

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

    public void handleEvent(GameEvent event) {
        switch (event.getType()) {
            case "notYourTurn" -> {
                inCurrentTurn = false;
                // display message
                uiStrategy.displayMessage("Not your turn! Wait for your turn...\n");

                if (unreadedMessages > 0)
                    uiStrategy.displayMessage("\nThere are " + unreadedMessages + " messages you have not read\n\n");
                uiStrategy.selectFromMenu();

            }
            case "loadedStarter"-> {
                // notify TUI that the starter card is loaded, using the listener/observer pattern
                observerUI.updateUI(event);

                // get starter card from server
                starterCard = (ResourceCard) event.getData();
                // TODO: change parameters and set as class attribute
                // useful for the visualization of the starter card (TUI)
                starterCard.setCentre(new Coordinate(10 / 2 * 7 - 5,10 / 2 * 3 - 5));

                // add starter card to codex
                codex.add(starterCard);

                // display starter card back
                // uiStrategy.visualizeStarterCard(starterCard);
            }
            case "loadedPawn"-> {
                observerUI.updateUI(event);
                Color colors = (Color) event.getData();
            }
            case "starterSide"-> {
                observerUI.updateUI(event);
                // ask user to set side of the starter card
                // boolean side = uiStrategy.setStarterSide();
            }
            case "askWhereToDraw"-> {
                String input = uiStrategy.askWhereToDraw((List<Card>) event.getData());

                try {
                    outputStream.writeObject(new GameEvent("drawCard", input));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "loadedCommonObjective" -> {
                uiStrategy.displayCommonObjective((List<Objective>) event.getData());
            }
            case "chooseObjective" -> {
                Objective card = uiStrategy.chooseObjective((List<Objective>) event.getData());

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

                // card where to attach the selected card
                Card targetCard = codex.stream().filter(c -> c.getId() == cardToAttachId).findFirst().orElse(null);

                uiStrategy.place(cardToPlay, targetCard, Integer.parseInt(splitCardToPlay[1]));

                uiStrategy.displayBoard();

                try {
                    outputStream.writeObject(new GameEvent("angleSelection", new CardToAttachSelected(input)));
                } catch (IOException e) {
                    System.out.println("Error sending angles: " + e.getMessage());
                }
            }
            case "updateHand" -> {
                hand = (List<Card>) event.getData();

                uiStrategy.displayHand(hand);
            }
            case "updateCodex" -> {
                codex = (List<Card>) event.getData();
            }
            case "beforeTurnEvent" -> {
                resources = (Map<Resource, Integer>) event.getData();
            }
            case "currentPlayerTurn" -> {
                inCurrentTurn = true;
                CardSelection cs = uiStrategy.askCardSelection((PlayableCardIds) event.getData(), hand);

                cardToPlay = hand.stream().filter(c -> c.getId() == cs.getId()).findFirst().orElse(null);
                hand.remove(cardToPlay);

                try {
                    outputStream.writeObject(new GameEvent("cardSelection", cs));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
    }
            case "lastTurn" -> {
                System.out.println("Last turn! \n");
            }
            case "reachedPoints" -> {
                uiStrategy.displayMessage("You have reached 20 points, wait for the other players to finish their turn!\n");
            }
            case "endGame" -> {
                uiStrategy.displayMessage("Game over!\nResults:\n");
                for(int i = 1; i<((List<Pair<String, Integer>>) event.getData()).size()+1; i++){
                    uiStrategy.displayMessage(i+") Player: "+((List<Pair<String, Integer>>) event.getData()).get(i-1).getKey() + " Points: " + ((List<Pair<String, Integer>>) event.getData()).get(i-1).getValue() + "\n");
                }
                closeConnection();
            }
            case "mexIncoming" -> {
                if (!inCurrentTurn)
                    uiStrategy.displayMessage("New message received! You have not read it yet\n");
                unreadedMessages++;
                chat.addMsg((Message) event.getData());
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
        try {
            Client client = new Client("localhost", 12345, opt);
            client.start(args);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleMoveUI(GameEvent gameEvent) {
        switch (gameEvent.getType()) {
            case "starterSide" -> {
                // set side of the starter card
                boolean side = (boolean) gameEvent.getData();
                starterCard.setSide(side);

                // place starter card on the board
                uiStrategy.placeCard(starterCard, null);

                // display board
                uiStrategy.displayBoard();

                // send side to server
                try {
                    outputStream.writeObject(new GameEvent("starterSideSelection", side));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "displayChat" -> {
                uiStrategy.displayChat(chat, username);
                if (!inCurrentTurn)
                    uiStrategy.selectFromMenu();
            }
            case "continue" -> {
                if (unreadedMessages > 0)
                    uiStrategy.displayMessage("New message received! You have not read it yet\n");

                if (!inCurrentTurn)
                    uiStrategy.displayMessage("Wait for your turn...\n");
            }
            case "writeNewMex" -> {
                // send list of players connected to the same game session
                List<String> playersConnected = new ArrayList<>();

                if (username.equals("us1"))
                    playersConnected.add("us2");
                else
                    playersConnected.add("us1");

                gameEvent.setData(playersConnected);
                observerUI.updateUI(gameEvent);

                if (!inCurrentTurn)
                    uiStrategy.selectFromMenu();
            }
            case "sendNewMex" -> {
                // send message to server
                Message message = (Message) gameEvent.getData();
                message.setSender(username);
                chat.addMsg(message);
                try {
                    outputStream.writeObject(new GameEvent("newMessage", message));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
        }
    }
}
