package it.polimi.ingsw.network.rmi.client;

import it.polimi.ingsw.core.controller.GameControllerRemote;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.network.GameServer;
import it.polimi.ingsw.observers.GameObserver;
import it.polimi.ingsw.ui.*;
import it.polimi.ingsw.ui.GUI.GUI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameClientImpl extends UnicastRemoteObject implements GameClient, GameObserver, ObserverUI {
    private GameServer server; // reference to the RMI server
    private ViewModel viewModel;
    private Player client;
    private Card cardToPlay;
    private UserInterfaceStrategy uiStrategy;
    private String username; // username of the client
    private Scanner scanner = new Scanner(System.in); // scanner to read input from the user
    private String gameId;
    private GameControllerRemote gc;
    private ObserverUI observerUI;

    private ResourceCard starterCard;
    private List<Card> hand;
    private List<Card> codex;


    private Map<Resource, Integer> resources;


    public GameClientImpl(String host, int port, UserInterfaceStrategy uiStrategy) throws RemoteException {
        super();
        connectToServer(host, port);

       // this.viewModel.getPlayerStates().get(client).setCodex(new ArrayList<>());
       // this.viewModel.getPlayerStates().get(client).setHand(new ArrayList<>());
        this.viewModel = new ViewModel();
        this.viewModel.setMyCodex(new ArrayList<>());
        this.viewModel.setMyHand(new ArrayList<>());

        this.observerUI = (ObserverUI) uiStrategy;
        this.uiStrategy = uiStrategy;
        if(uiStrategy instanceof GUI){
            new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(GUI.class);
                }
            }.start();
            //TODO poi fare classe astratta che estende gui e cli così si spostano lì questi attributi
            ((GUI) uiStrategy).setObserverUIClient((ObserverUI)this);
            ((GUI) uiStrategy).setViewModel(viewModel);
        }
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
        //todo farlo ad eventi
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
            case "loadedUsername" -> {
                System.out.println("Username loaded event arrived to client\n");
                viewModel.setMyUsername((String) event.getData());
            }
            case "notYourTurn" -> {
                observerUI.updateUI(event);
                /*
                // display message
               ->   uiStrategy.displayMessage("Not your turn! Wait for your turn...\n");
                // uiStrategy.displayMenu();

                 */
            }
            case "loadedPlayers" ->{
                viewModel.setPlayers((List<String>) event.getData());
                //for now it doesn't update the view, it's not needed
            }
            case "loadedStarter"-> {
                starterCard = (ResourceCard) event.getData();
                this.viewModel.setMyStarterCard(starterCard);
                this.viewModel.getMyCodex().add(starterCard);

                observerUI.updateUI(event);

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
            case "updateDecks" -> {
                //TODO pensare a come parametrizzarlo meglio (se necessario)
                System.out.println("Update decks event arrived to client\n");
                List<Card> updatedDecks = (List<Card>) event.getData();
                viewModel.getResourceCardsVisible().add(updatedDecks.get(0));
                viewModel.getResourceCardsVisible().add(updatedDecks.get(1));
                viewModel.getGoldCardsVisible().add(updatedDecks.get(2));
                viewModel.getGoldCardsVisible().add(updatedDecks.get(3));
                viewModel.setDeckRBack(updatedDecks.get(4));
                viewModel.setDeckGBack(updatedDecks.get(5));
                observerUI.updateUI(event);
            }

            case "starterSide"-> {
                System.out.println("Starter side event arrived to client\n");
                observerUI.updateUI(event);

                // ask user to set side of the starter card
                //todo put in cli
                boolean side = uiStrategy.setStarterSide();

                // set side of the starter card
               // starterCard.setSide(side);

                // place starter card on the board
                //uiStrategy.placeCard(starterCard, null);

                // display board
                //uiStrategy.displayBoard();
                // send side to server
                //TODO: da spostare nell'altro update
                /*
                try {
                    gc.handleMove(username, new GameEvent("starterSideSelection", side));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }

                 */
            }

            case "askWhereToDraw"-> {
                System.out.println("Ask where to draw event arrived to client\n");
                observerUI.updateUI(event);
                // todo to put in cli
                //String input = uiStrategy.askWhereToDraw((List<Card>) event.getData());


                //TODO: da spostare nell'altro update
                /*
                try {
                    gc.handleMove(username, new GameEvent("drawCard", input));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }

                 */
            }

            case "loadedCommonObjective" -> {
                System.out.println("Common objective event arrived to client\n");
                observerUI.updateUI(event);
                //in realtà la gui non ci deve fare nulla con questo evento perchè quando si inizializza prende dal view model
                viewModel.setCommonObj((List<Objective>) event.getData());
                /* da mettere in cli
                uiStrategy.displayCommonObjective((List<Objective>) event.getData());

                 */
            }
            case "chooseObjective" -> {
                System.out.println("Choose objective event arrived to client\n");
                observerUI.updateUI(event);
                //todo put in cli
                //Objective card = uiStrategy.chooseObjective((List<Objective>) event.getData());

                //todo move in the other update method
                /*
                try {
                    gc.handleMove(username, new GameEvent("secretObjectiveSelection", card));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }

                 */
            }
            case "askAngle" -> {
                System.out.println("ask angle arrived to client ");
                observerUI.updateUI(event);


                //todo put in cli
                //String input = uiStrategy.displayAngle((List<Coordinate>) event.getData());

                // get card from player's hand by id
                // TODO: create object for handling card selection
                //String[] splitCardToPlay = input.split("\\.");
                //int cardToAttachId = Integer.parseInt(splitCardToPlay[0]);

                // card where to attach the selected card
                //Card targetCard = codex.stream().filter(c -> c.getId() == cardToAttachId).findFirst().orElse(null);

               // uiStrategy.place(cardToPlay, targetCard, Integer.parseInt(splitCardToPlay[1]));

               // uiStrategy.displayBoard();

                //todo move in the other method update
                /*
                try {
                    gc.handleMove(username, new GameEvent("angleSelection", new CardToAttachSelected(input)));
                } catch (IOException e) {
                    System.out.println("Error sending angles: " + e.getMessage());
                }

                 */
            }
            case "updateHand" -> {
                System.out.println("Update hand event arrived to client\n");
                viewModel.setMyHand((List<Card>) event.getData());
                observerUI.updateUI(event);
                //todo put in cli:
                //hand = (List<Card>) event.getData();
                //uiStrategy.displayHand(hand);
            }
            case "updateCodex" -> {
                //TODO probabilmente togliere
                System.out.println("Update codex event arrived to client\n");
                codex = (List<Card>) event.getData();
            }
            case "beforeTurnEvent" -> {
                //TODO probabilmente togliere
                System.out.println("Before turn event arrived to client\n");
                resources = (Map<Resource, Integer>) event.getData();
            }
            case "currentPlayerTurn" -> {
                System.out.println("Current player turn event arrived to client\n");
                observerUI.updateUI(event);

                //TODO PUT IN CLI:
                //CardSelection cs = uiStrategy.askCardSelection((PlayableCardIds) event.getData(), hand);

                //cardToPlay = hand.stream().filter(c -> c.getId() == cs.getId()).findFirst().orElse(null);
               // hand.remove(cardToPlay);

                //todo move in the orther update method
                /*
                try {
                    gc.handleMove(username, new GameEvent("cardSelection", cs));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }

                 */
            }
            case "lastTurn" -> {
                // TODO: bug fix
                System.out.println("Last turn event arrived to client\n");
                observerUI.updateUI(event);

                //todo put in cli -> maybe only printf last turn, but then usually event for card selection
               // System.out.println("Last turn! Select a card ID to play: ");
               // int cardId = scanner.nextInt();
                 //todo moved in the other update method
                /*
                try {
                    gc.handleMove(username, new GameEvent("cardSelection", new CardSelection(cardId, false)));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }

                 */
            }
            case "endGame" -> {
                System.out.println("End game event arrived to client\n");
                observerUI.updateUI(event);

                //todo put in cli
                //System.out.println("Game over!");
            }

            case "updatePlayerstate"-> {
                Map<String, ViewModelPlayerstate> playerUpdated = (Map<String, ViewModelPlayerstate>) event.getData();
                String usernameTarget = playerUpdated.keySet().stream().findFirst().orElse(null);
                if(usernameTarget.equals(viewModel.getMyUsername())){
                    viewModel.setMyCodex(playerUpdated.get(viewModel.getMyUsername()).getCodex());
                    viewModel.setMyResources(playerUpdated.get(viewModel.getMyUsername()).getPersonalResources());
                    viewModel.setMyScore(playerUpdated.get(viewModel.getMyUsername()).getScore());
                    observerUI.updateUI(new GameEvent("updateMyPlayerstate", null));
                }
                else{
                    viewModel.setStateOfPlayer(usernameTarget, playerUpdated.get(usernameTarget));
                    observerUI.updateUI(new GameEvent("updatePlayerstate", (String) usernameTarget));
                }
            }
        }
    }

    @Override
    public void notify(GameEvent event) throws RemoteException {}

    public static void main(String[] args) {
        UserInterfaceStrategy uiStrategy;

        //String opt = "cli";
        String opt = "gui";
        if (opt.equals("cli")) {
            uiStrategy = new TextUserInterface();
        } else {
            uiStrategy = new GUI();
        }

        try {
            GameClientImpl client = new GameClientImpl("localhost", 1099, uiStrategy);

            client.login(args);
        } catch (RemoteException e) {
            System.out.println("Error creating the client: " + e.getMessage());
        }
    }

    @Override
    public void updateUI(GameEvent gameEvent) {
        switch (gameEvent.getType()) {
        case "starterSide" -> {
            // set side of the starter card

            boolean side = (boolean) gameEvent.getData();
            //added the side choosen to the starter card in the view model
            viewModel.getMyStarterCard().setSide(side);
            //added the starter card to the codex in the view model
            viewModel.getMyMatrix()[40][40] = viewModel.getMyStarterCard().getId();

            // place starter card on the board
            // uiStrategy.placeCard(starterCard, null);

            // display board
            //  uiStrategy.displayBoard();

            // send side to server
            try {
                gc.handleMove(username, new GameEvent("starterSideSelection", side));
            } catch (IOException e) {
                System.out.println("Error sending card ID: " + e.getMessage());
            }
        }

        case "chooseObjective" -> {
            Objective card = (Objective) gameEvent.getData();
            //added the objective card to the view model
            viewModel.setSecretObj(card);
            try {
                gc.handleMove(username, new GameEvent("secretObjectiveSelection", card));
            } catch (IOException e) {
                System.out.println("Error sending card ID: " + e.getMessage());
            }
        }

        case "cardToPlaySelected" -> {
            CardSelection cs = (CardSelection) gameEvent.getData();
            try {
                gc.handleMove(username, new GameEvent("cardSelection", cs));
            } catch (IOException e) {
                System.out.println("Error sending card ID: " + e.getMessage());
            }
        }

        case "angleSelected" -> {
            try {
                gc.handleMove(username, new GameEvent("angleSelection", new CardToAttachSelected((String) gameEvent.getData())));
            } catch (IOException e) {
                System.out.println("Error sending angles: " + e.getMessage());
            }
        }

        case "whereToDrawSelected" -> {
            String input = (String) gameEvent.getData();
            try {
                gc.handleMove(username, new GameEvent("drawCard", input));
            } catch (IOException e) {
                System.out.println("Error sending card ID: " + e.getMessage());
            }
        }

    }

}
}
