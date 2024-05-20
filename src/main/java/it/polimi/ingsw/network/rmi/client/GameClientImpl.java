package it.polimi.ingsw.network.rmi.client;

import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.network.GameServer;
import it.polimi.ingsw.observers.GameObserver;
import it.polimi.ingsw.ui.GraphicalUserInterface;
import it.polimi.ingsw.ui.TextUserInterface;
import it.polimi.ingsw.ui.UserInterfaceStrategy;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class GameClientImpl extends UnicastRemoteObject implements GameClient, GameObserver {
    private GameServer server; // reference to the RMI server
    private String username; // username of the client
    private Scanner scanner = new Scanner(System.in); // scanner to read input from the user
    private String gameId;
    private GameControllerRemote gc;
    private UserInterfaceStrategy uiStrategy;

    private ResourceCard starterCard;
    private List<Card> hand;
    private List<Card> codex;
    private Map<Resource, Integer> resources;
    private Card cardToPlay;

    public GameClientImpl(String host, int port, UserInterfaceStrategy uiStrategy) throws RemoteException {
        super();
        connectToServer(host, port);

        this.codex = new ArrayList<>();
        this.hand = new ArrayList<>();

        this.uiStrategy = uiStrategy;
        this.uiStrategy.initialize();
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
                gc = server.joinSession(gameId, username, this);
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
                gc = server.createNewSession(gameId, username, numPlayers, this);
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
                    gc.handleMove(username, new GameEvent("starterSideSelection", input.equals("f")));
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
                    gc.handleMove(username, new GameEvent("drawCard", new DrawCard(input)));
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
                    gc.handleMove(username, new GameEvent("secretObjectiveSelection", card));
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
                    gc.handleMove(username, new GameEvent("angleSelection", new CardToAttachSelected(input)));
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
                    gc.handleMove(username, new GameEvent("cardSelection", cs));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
                hand.remove(cardToPlay);
            }
            case "lastTurn" ->{
                System.out.println("Last turn! Select a card ID to play: ");
                int cardId = scanner.nextInt();
                try {
                    gc.handleMove(username, new GameEvent("cardSelection", new CardSelection(cardId, false)));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "endGame" -> {
                System.out.println("Game over!");
            }
        }
    }

    @Override
    public void notify(GameEvent event) throws RemoteException {}

    public static void main(String[] args) {
        UserInterfaceStrategy uiStrategy;

        String opt = "cli";
        if (opt.equals("cli")) {
            uiStrategy = new TextUserInterface();
        } else {
            uiStrategy = new GraphicalUserInterface();
        }

        try {
            GameClientImpl client = new GameClientImpl("localhost", 1099, uiStrategy);

            client.login(args);
        } catch (RemoteException e) {
            System.out.println("Error creating the client: " + e.getMessage());
        }
    }
}
