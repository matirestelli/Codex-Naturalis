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
import it.polimi.ingsw.ui.ViewModel;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameClientImpl extends UnicastRemoteObject implements GameClient, GameObserver {
    private GameServer server; // reference to the RMI server
    private ViewModel viewModel;
    private Player client;
    private Card cardToPlay;
    private UserInterfaceStrategy uiStrategy;
    private GameObserver uiObserver;
    private String username; // username of the client
    private Scanner scanner = new Scanner(System.in); // scanner to read input from the user
    private String gameId;
    private GameControllerRemote gc;


    private ResourceCard starterCard;
    private List<Card> hand;
    private List<Card> codex;


    private Map<Resource, Integer> resources;


    public GameClientImpl(String host, int port, UserInterfaceStrategy uiStrategy) throws RemoteException {
        super();
        connectToServer(host, port);

       // this.viewModel.getPlayerStates().get(client).setCodex(new ArrayList<>());
       // this.viewModel.getPlayerStates().get(client).setHand(new ArrayList<>());
        this.viewModel.setMyCodex(new ArrayList<>());
        this.viewModel.setMyHand(new ArrayList<>());

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
            //TODO evento di inizio partita che setta il view model con i giocatori

            case "notYourTurn" -> {
                uiObserver.update(event);
                /*
                // display message
               ->   uiStrategy.displayMessage("Not your turn! Wait for your turn...\n");
                // uiStrategy.displayMenu();

                 */
            }
            case "loadedStarter"-> {
                starterCard = (ResourceCard) event.getData();
               // this.viewModel.getPlayerStates().get(client).setStarterCard(starterCard);
                this.viewModel.setMyStarterCard(starterCard);
              //  this.viewModel.getPlayerStates().get(client).getCodex().add(starterCard);
                this.viewModel.getMyCodex().add(starterCard);
                uiObserver.update(event);

                /* da mettere in  cli
                // get starter card from server
                starterCard = (ResourceCard) event.getData();
                // TODO: change parameters and set as class attribute
                // useful for the visualization of the starter card (TUI)
                starterCard.setCentre(new Coordinate(10 / 2 * 7 - 5,10 / 2 * 3 - 5));

                // add starter card to codex
                codex.add(starterCard);

                // display starter card back
                uiStrategy.visualizeStarterCard(starterCard);
                 */
            }
            case "starterSide"-> {
                uiObserver.update(event);

                // da mettere in cli
                // ask user to set side of the starter card
                boolean side = uiStrategy.setStarterSide();

                // set side of the starter card
                starterCard.setSide(side);

                // place starter card on the board
                uiStrategy.placeCard(starterCard, null);

                // display board
                uiStrategy.displayBoard();



                // send side to server
                //TODO: da spostare nell'altro update
                try {
                    gc.handleMove(username, new GameEvent("starterSideSelection", side));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }

            case "askWhereToDraw"-> {
                uiObserver.update(event);
                // da mettere in cli
                String input = uiStrategy.askWhereToDraw((List<Card>) event.getData());


                //TODO: da spostare nell'altro update
                try {
                    gc.handleMove(username, new GameEvent("drawCard", input));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }

            case "loadedCommonObjective" -> {
                uiObserver.update(event);
                /* da mettere in cli
                uiStrategy.displayCommonObjective((List<Objective>) event.getData());

                 */
            }
            case "chooseObjective" -> {
                Objective card = uiStrategy.chooseObjective((List<Objective>) event.getData());

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

                // card where to attach the selected card
                Card targetCard = codex.stream().filter(c -> c.getId() == cardToAttachId).findFirst().orElse(null);

                uiStrategy.place(cardToPlay, targetCard, Integer.parseInt(splitCardToPlay[1]));

                uiStrategy.displayBoard();

                try {
                    gc.handleMove(username, new GameEvent("angleSelection", new CardToAttachSelected(input)));
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
                CardSelection cs = uiStrategy.askCardSelection((PlayableCardIds) event.getData(), hand);

                cardToPlay = hand.stream().filter(c -> c.getId() == cs.getId()).findFirst().orElse(null);
                hand.remove(cardToPlay);

                try {
                    gc.handleMove(username, new GameEvent("cardSelection", cs));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "lastTurn" -> {
                // TODO: bug fix
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
