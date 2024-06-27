package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.request.*;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.core.utils.PlayerMove;
import it.polimi.ingsw.observers.GameObserver;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameController extends UnicastRemoteObject implements GameControllerRemote {
    private GameState gameState; // reference to the game state
    private Map<String, GameObserver> observers; // every observer is associated with the username of the player it represents
    private Map<String, GameObserver> orderedObserversMap; // map of observers ordered by the order of player turns
    private BlockingQueue<PlayerMove> moveQueue; // queue of moves to be processed
    private Thread moveProcessor; // thread that processes moves
    private int currentPlayerIndex; // index of the current player
    private boolean last = false;
    private int matrixDimension;
    private List<String> playersReadyToPlayer = new ArrayList<>();

    private Map<Integer, Map<Integer, List<Coordinate>>> test;
    private Card cardToPlace;

    /**
     * Constructs a new GameController instance with the given game state.
     *
     * <p>This constructor initializes the game controller, setting up the game state,
     * observers, move queue, and move processor. It also starts the move processor thread
     * and initializes various game parameters.</p>
     *
     * @param gameState The initial state of the game to be managed by this controller.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public GameController(GameState gameState) throws RemoteException {
        super();
        this.gameState = gameState;
        this.observers = new LinkedHashMap<>();
        this.moveQueue = new LinkedBlockingQueue<>();
        this.moveProcessor = new Thread(this::processMoves);
        this.moveProcessor.start();
        this.currentPlayerIndex = 0;
        this.matrixDimension = 81;
    }

    /**
     * Initializes and starts the game.
     * This method performs several setup operations including:
     * <ul>
     *   <li>Ordering players and initializing their game state</li>
     *   <li>Loading decks and initializing the game board</li>
     *   <li>Assigning starter cards and initial hands to players</li>
     *   <li>Setting up common and secret objectives</li>
     *   <li>Notifying observers of various game state changes</li>
     * </ul>
     *
     * <p>The method uses a series of helper methods from the gameState object
     * to set up the game, and then notifies observers of the changes using
     * various message types.</p>
     *
     * @throws RemoteException if there's an error in remote method invocation
     */
    @Override
    public void startGame() throws RemoteException {
        try{
        // TODO: remove this or implement new logger system
        System.out.println("Game started");


        // define the order of players they will play
        gameState.orderPlayers();

        // create a map of observers ordered by the order of player turns
        orderedObserversMap = new LinkedHashMap<>();
        for (int i = 0; i < observers.size(); i++) {
            orderedObserversMap.put(gameState.getPlayerOrder().get(i), observers.get(gameState.getPlayerOrder().get(i)));
        }


        // initialize matrix for each player
        gameState.initializeMatrixPlayers(this.matrixDimension);

        gameState.initializeChat();

        // load decks
        gameState.loadDecks();

        //todo put in a message:

       /* List<Card> updatedDecks = new ArrayList<>();
        updatedDecks.addAll(gameState.getResourceCardsVisible());
        updatedDecks.addAll(gameState.getGoldCardsVisible());
        updatedDecks.add(gameState.getResourceDeck().getCards().getFirst());
        updatedDecks.add(gameState.getGoldDeck().getCards().getFirst());
        //notify observers of the updated decks
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new UpdatedDecksMessage("updateDecks", updatedDecks));

        */

        gameState.initializePawn();

        // assign the starter card to each player
        gameState.assignStarterCardToPlayers();

        //todo then cancel it, now I need it to put username in viewModel because now it's a parameter of the main
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new LoadedUsernameMessage("loadedUsername", us));

        //notify observers of the order of players and username of all the players
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new LoadedPlayersMessage("loadedPlayers", gameState.getPlayerOrder()));

        /*this.checker = new ConnectionChecker(gameState, 5000, 10000);
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new playerStates("playerStates", gameState.getPlayerState(us)));
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new startPinging("startPinging", us));
        checker.start();

         */



        // notify observers of the starter card assigned to each player
        for (String us : orderedObserversMap.keySet()) {
            orderedObserversMap.get(us).update(new StarterCardLoadedMessage("starterCardLoaded", gameState.getPlayerState(us).getStarterCard()));
        }

        Map<String, Color> playerPawns = new HashMap<>();
        for (String us : orderedObserversMap.keySet()){
            playerPawns.put(us, gameState.getPlayerState(us).getPawn());
            }

        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new LoadedPawnsMessage("loadedPawns", playerPawns));

        // assign the first hand of cards to each player
        gameState.assignFirstHandToPlayers();
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new UpdatedHandMessage("updateHand", gameState.getPlayerState(us).getHand()));



        List<Card> updatedDecks = new ArrayList<>();
        updatedDecks.addAll(gameState.getResourceCardsVisible());
        updatedDecks.addAll(gameState.getGoldCardsVisible());
        updatedDecks.add(gameState.getResourceDeck().getCards().getFirst());
        updatedDecks.add(gameState.getGoldDeck().getCards().getFirst());
        //notify observers of the updated decks
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new UpdatedDecksMessage("updateDecks", updatedDecks));


        // assign common objectives of the game
        // TODO: implement for loop to draw n cards. Define n as class variable
        gameState.addCommonObjective((Objective) gameState.getObjectiveDeck().drawCard());
        gameState.addCommonObjective((Objective) gameState.getObjectiveDeck().drawCard());

        // notify observers of the common objectives
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new LoadedCommonObjectiveMessage("loadedCommonObjective", gameState.getCommonObjectives()));


        // assign secret objectives to each player
        for (String us : orderedObserversMap.keySet()) {
            // TODO: implement for loop to draw n cards. Define n as class variable
            List<CardGame> secretChoose = new ArrayList();
            secretChoose.add(gameState.getObjectiveDeck().drawCard());
            secretChoose.add(gameState.getObjectiveDeck().drawCard());
            // notify observers of the secret objectives
            orderedObserversMap.get(us).update(new ChooseSecretObjMessage("chooseObjective", secretChoose));
        }

        // set side of starter card for each player
        for (String us : orderedObserversMap.keySet()) {
            // notify observers of the side of the starter card
            orderedObserversMap.get(us).update(new StarterSideSelectionMessage("starterSide", gameState.getPlayerState(us).getStarterCard()));
        }
        } catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
            }
    }

    /**
     * Handles a move made by a player in the game.
     *
     * <p>This method is synchronized to ensure thread-safe access to the move queue.
     * It attempts to add the player's move to a queue for processing. If the queue is full,
     * the move cannot be added, and a message is printed to the console. However, this
     * method does not throw an exception or otherwise notify the caller if the move
     * is not added.</p>
     *
     * @param username The username of the player making the move.
     * @param event The MessageClient2Server object representing the move event.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    @Override
    public synchronized void handleMove(String username, MessageClient2Server event) throws RemoteException {
        // add the move to the queue
        boolean offer = moveQueue.offer(new PlayerMove(username, event));
        if (!offer) {
            System.out.println("Move not added to the queue");
        }
    }

    /**
     * Handles the selection of a secret objective by a player.
     *
     * <p>This method is called when a player chooses their secret objective card. It logs the
     * selected objective and updates the game state with the player's choice. The chosen
     * objective is printed to the console, which may be useful for debugging or logging
     * purposes. Consider replacing this console output with a proper logging mechanism
     * in a production environment.</p>
     *
     * @param username The username of the player who chose the objective.
     * @param cardSelected The Objective card that was selected by the player.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void handleChoseObjective(String username, Objective cardSelected) throws RemoteException {
        System.out.println("Chose objective: " + cardSelected);
        // set secret objective of player given by username
        gameState.setSecretObjective(username, cardSelected);
    }

    /**
     * Handles the selection of a starter card side by a player and initiates the game if all players are ready.
     *
     * <p>This method is called when a player selects the side of their starter card. It updates the game state
     * with the player's choice, places the starter card in the matrix, and checks if all players are ready to
     * start the game. The selected side is printed to the console for debugging purposes. If all players have
     * selected their starter card sides, it notifies the current player's turn. In case of a RemoteException,
     * an error message is printed, and the game exits.</p>
     *
     * @param username The username of the player who selected the starter card side.
     * @param side A boolean representing the selected side of the starter card.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void handleStarterSideSelected(String username, boolean side) throws RemoteException {
        try{
        System.out.println("Starter side selected: " + side);
        // assign side of starter card to player given by username
        gameState.assignStarterSide(username, side);
        // place starter card in middle of matrix
        gameState.placeStarterInMatrix(username, matrixDimension);

        playersReadyToPlayer.add(username);

        if (playersReadyToPlayer.size() == gameState.getPlayerOrder().size()) {
            //send to all the players the players pawn
            System.out.println("Turn order: " + gameState.getPlayerOrder());
            notifyCurrentPlayerTurn();
        }
    } catch (RemoteException e) {
        System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
    }
    }

    /**
     * Handles the selection of a card by a player and determines possible placement angles.
     *
     * <p>This method processes a player's card selection, determines valid placement angles,
     * and updates the game state accordingly. If valid placements exist, it prepares for
     * the angle selection phase. If no valid placements exist, it advances to the next turn.
     * The method retrieves the selected card from the player's hand, calculates free angles
     * for card placement using the player's codex, and updates the player's hand and codex
     * accordingly. If no free angles are available, it notifies the player and advances the
     * turn. In case of a RemoteException, an error message is printed, and the game exits.</p>
     *
     * @param username The username of the player selecting the card.
     * @param cardSelection An object containing the selected card's ID and chosen side.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void handleCardSelected(String username, CardSelection cardSelection) throws RemoteException {
        try{
        // get card from player hand by id
        Card cardToPlay = gameState.getPlayerState(username).getCardFromHand(cardSelection.getId());
        // get list of possible angles to place the card
        List<Coordinate> freeAngles = new ArrayList<>();
        // TODO: make test as attribute of GameState class
        test = new HashMap<>();
        PlayerState ps = gameState.getPlayerState(username);
        // set side of selected card
        cardToPlay.setSide(cardSelection.getSide());
        for (Card c : ps.getCodex())
            freeAngles.addAll(c.findFreeAngles(ps.getMatrix(), ps.getCodex(), cardToPlay.getId(), test));
        if(freeAngles.isEmpty()){
            orderedObserversMap.get(username).update(new AvailableAnglesMessage("askAngleNull", null));
            advanceTurn();
        } else {
            // remove selected card from player hand
            gameState.getPlayerState(username).removeCardFromHand(cardToPlay);

            // add card to player codex
            gameState.getPlayerState(username).addCardToCodex(cardToPlay);
            // TODO: set card to place as attribute of PlayerState class
            cardToPlace = cardToPlay;
            orderedObserversMap.get(username).update(new AvailableAnglesMessage("askAngle", freeAngles));
        }
    } catch (RemoteException e) {
        System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
    }
    }

    /**
     * Processes a move made by a player in the game.
     *
     * <p>This method is responsible for executing the action associated with a player's move.
     * It logs the type of move being processed to the console, which may be useful for debugging
     * or logging purposes, and delegates the action to the message object. The actual processing
     * of the move is carried out by the {@code doAction} method of the {@code MessageClient2Server} object.
     * In case of a RemoteException, an error message is printed, and the game exits. Consider
     * replacing console output with a proper logging mechanism in a production environment.</p>
     *
     * @param username The username of the player making the move.
     * @param message The MessageClient2Server object representing the move to be processed.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void processMove(String username, MessageClient2Server message) throws RemoteException {
        try{
        System.out.println("Processing move: " + message.getType());
        message.doAction(username, this);
    } catch (RemoteException e) {
        System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
    }
    }

    public void processMoves() {
        while (true) {
            try {
                PlayerMove playerMove = moveQueue.take(); // Blocca fino a quando un elemento è disponibile
                // processMove(playerMove.getUsername(), playerMove.getEvent());
                processMove(playerMove.getUsername(), playerMove.getMex());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
                exitGame("error");
        }
        }
    }

    /**
     * Adds an observer to the game for a specific user.
     *
     * <p>This method is synchronized to ensure thread-safe access to the observers collection.
     * It attempts to add a new observer for a given username, but only if an observer
     * for that username doesn't already exist. Information about the addition attempt is printed
     * to the console for debugging purposes. If an observer for the given username already exists,
     * the method will print a message and return without making any changes. Observers are stored
     * in a map, with usernames as keys and GameObserver objects as values. Consider replacing
     * console output with a proper logging mechanism in a production environment.</p>
     *
     * @param username The username associated with the observer.
     * @param observer The GameObserver object to be added.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public synchronized void addObserver(String username, GameObserver observer) throws RemoteException {
        System.out.println("\nAdding observer " + observer);
        if (observers.containsKey(username)) {
            System.out.println("Observer already added");
            return;
        }
        observers.put(username, observer);
    }

    // TODO: Fix this method
    @Override
    public synchronized void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all players about the current player's turn and updates game state accordingly.
     *
     * <p>This method is responsible for:</p>
     * <ul>
     *   <li>Notifying non-current players that it's not their turn.</li>
     *   <li>Informing the current player about their turn and available resources.</li>
     *   <li>Sending the list of playable cards to the current player.</li>
     * </ul>
     * <p>It uses the `gameState` object to determine the current player and game information. The method sends
     * different types of update messages to players based on whether it's their turn:</p>
     * <ul>
     *   <li>NotYourTurnMessage: Sent to all players except the current player.</li>
     *   <li>BeforeTurnMessage: Sent to the current player with calculated resources.</li>
     *   <li>YourTurnMessage: Sent to the current player with playable card IDs.</li>
     * </ul>
     * <p>There's commented-out code for handling a "last turn" scenario, which may be implemented in the future.
     * In case of a RemoteException, an error message is printed, and the game exits. Consider implementing
     * proper error handling and recovery mechanisms instead of exiting the game on exceptions.</p>
     *
     * @throws RemoteException If there's an error in remote method invocation.
     */
    @Override
    public void notifyCurrentPlayerTurn() throws RemoteException {
        try {
            String us = gameState.getPlayerOrder().get(currentPlayerIndex);
            for (String username : orderedObserversMap.keySet()) {
                if (!username.equals(us)) {
                    orderedObserversMap.get(username).update(new NotYourTurnMessage("notYourTurn", us));
                }
            }

            orderedObserversMap.get(us).update(new BeforeTurnMessage("beforeTurnEvent", gameState.calculateResource(us)));

            // send list of ids of playable cards from playing hand
        /* if (last) {
            orderedObserversMap.get(us).update(new GameEvent("lastTurn", gameState.getPlayerState(currentPlayerIndex)));
        } */
            orderedObserversMap.get(us).update(new YourTurnMessage("currentPlayerTurn", gameState.getPlayableCardIdsFromHand(us)));
        }catch (RemoteException e) {
                System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
            }
    }

    /**
     * Advances the game to the next player's turn or triggers the last turn of the game.
     *
     * <p>This method is responsible for:</p>
     * <ul>
     *   <li>Checking if the current player has reached the winning score (20 points) or if it's the last turn.</li>
     *   <li>If either condition is met, it triggers the last turn of the game.</li>
     *   <li>Otherwise, it advances to the next player's turn.</li>
     * </ul>
     * <p>The method uses the `gameState` object to access player information and scores. If the current player's
     * score is 20 or higher, or if the `last` flag is true, it calls the `lastTurn` method. Otherwise, it updates
     * the `currentPlayerIndex` to the next player in a circular manner. After advancing the turn, it calls
     * `notifyCurrentPlayerTurn` to update all players. The method prints the current player's username and score
     * to the console for debugging. In case of a RemoteException, an error message is printed, and the game exits.
     * Consider replacing console output with a proper logging mechanism in a production environment. The error
     * handling could be improved to avoid exiting the game on exceptions, perhaps by implementing a retry mechanism
     * or graceful degradation.</p>
     *
     * @throws RemoteException If there's an error in remote method invocation.
     */
    @Override
    public void advanceTurn() throws RemoteException {
        try{
        if (gameState.getPlayerState(gameState.getPlayerOrder().get(currentPlayerIndex)).getScore() >= 20 || last == true) {
            lastTurn(gameState.getPlayerOrder().get(currentPlayerIndex));
        } else {
            System.out.println("User: " + gameState.getPlayerOrder().get(currentPlayerIndex) + " Score: " + gameState.getPlayerState(gameState.getPlayerOrder().get(currentPlayerIndex)).getScore());
            currentPlayerIndex = (currentPlayerIndex + 1) % orderedObserversMap.size();
            notifyCurrentPlayerTurn();
        }
        }catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
        }
    }

    /**
     * Updates the codex of a specific player in the game.
     *
     * <p>This method sets the given list of cards as the new codex for the specified player,
     * identified by their username. It updates the game state to reflect this change.</p>
     *
     * @param username The username of the player whose codex is being updated.
     * @param codex A list of Card objects representing the new codex to be set for the player.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void updateCodex(String username, List<Card> codex) throws RemoteException {
        gameState.getPlayerState(username).setCodex(codex);
    }

    /**
     * Processes the player's choice of angle for attaching a card to their codex.
     *
     * <p>This method is responsible for:</p>
     * <ul>
     *   <li>Updating the player's codex with the newly attached card.</li>
     *   <li>Handling the logic for card placement and corner hiding.</li>
     *   <li>Updating the player's matrix with the new card position.</li>
     *   <li>Notifying the player of the updated codex and prompting for the next action.</li>
     * </ul>
     * <p>The method uses complex logic to determine which corners of cards should be hidden after placement,
     * and calculates card placement coordinates based on the chosen corner (0-3) of the target card. The
     * player's codex and matrix are updated with the new card information. After placement, the method sends
     * an update to the player with the new codex state and prompts the player to draw a new card by sending a
     * list of visible resource and gold cards. It uses a global `test` map for some logic, which might benefit
     * from refactoring for clarity. Error handling is implemented by catching RemoteException, printing an
     * error message, and exiting the game. Consider implementing more robust error handling and recovery
     * mechanisms instead of exiting the game on exceptions. The method could benefit from additional comments
     * explaining the complex logic for corner hiding and card placement.</p>
     *
     * @param username The username of the player making the move.
     * @param cardToAttach An object containing information about the card to be attached and its placement.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void angleChosen(String username, CardToAttachSelected cardToAttach) throws RemoteException {
        try{
        PlayerState player = gameState.getPlayerState(username);
        player.setCodex(cardToAttach.getCodex());
        cardToPlace = player.getCodex().getLast();
        String[] splitCardToPlay = cardToAttach.getString().split("\\.");
        int cardToAttachId = Integer.parseInt(splitCardToPlay[0]);
        int cornerSelected = Integer.parseInt(splitCardToPlay[1]);
        Card targetCard = player.getCodex().stream()
                .filter(card1 -> card1.getId() == cardToAttachId)
                .findAny()
                .get();

        if (test.containsKey(cardToAttachId)) {
            if (test.get(cardToAttachId).containsKey(cornerSelected)) {
                List<Coordinate> co = test.get(cardToAttachId).get(cornerSelected);

                for (Coordinate c : co) {
                    if (c.getX() == cardToPlace.getId() && cardToPlace.getActualCorners().containsKey(c.getY())) {
                        cardToPlace.getActualCorners().get(c.getY()).setHidden(true);
                    } else {
                        Card cardTemp = player.getCodex().stream()
                                .filter(card1 -> card1.getId() == c.getX())
                                .findAny()
                                .get();
                        if (cardTemp.getActualCorners().containsKey(c.getY())) {
                            cardTemp.getActualCorners().get(c.getY()).setHidden(true);
                            cardTemp.getActualCorners().get(c.getY()).setEmpty(true);
                        }
                    }
                }
            }
        }

        if (cornerSelected == 0) {
            cardToPlace.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() - 1);
        } else if (cornerSelected == 1) {
            cardToPlace.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() - 1);
        } else if (cornerSelected == 2) {
            cardToPlace.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() + 1);
        } else if (cornerSelected == 3) {
            cardToPlace.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() + 1);
        }

        // notify observers of the updated codex
        orderedObserversMap.get(username).update(new UpdateCodexMessage("updateCodex", player.getCodex()));

        player.getMatrix()[cardToPlace.getyMatrixCord()][cardToPlace.getxMatrixCord()] = cardToPlace.getId();


        List<Card> visibileCards = new ArrayList<>();
        visibileCards.addAll(gameState.getResourceCardsVisible());
        visibileCards.addAll(gameState.getGoldCardsVisible());

        orderedObserversMap.get(username).update(new DrawNewCardMessage("askWhereToDraw", visibileCards));
    }catch (RemoteException e) {
        System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
    }
    }

    /**
     * Processes a player's action to draw a card and updates the game state accordingly.
     *
     * <p>This method is responsible for:</p>
     * <ul>
     *   <li>Drawing a card based on the player's choice (resource deck, gold deck, or visible cards).</li>
     *   <li>Updating the player's hand, score, and resources.</li>
     *   <li>Updating the game's visible cards and decks.</li>
     *   <li>Notifying all players of the updated game state.</li>
     *   <li>Advancing to the next player's turn.</li>
     * </ul>
     * <p>The method handles different card drawing scenarios based on the player's choice:</p>
     * <ul>
     *   <li>Drawing from the resource deck ("A").</li>
     *   <li>Drawing from the gold deck ("B").</li>
     *   <li>Selecting a visible resource card (ID &lt; 40).</li>
     *   <li>Selecting a visible gold card (ID &ge; 40).</li>
     * </ul>
     * <p>After drawing, it updates the player's hand and calculates a new score based on the card type:</p>
     * <ul>
     *   <li>For ResourceCards, it adds the card's point value to the score.</li>
     *   <li>For GoldCards, it calculates the score based on the player's resources and the card's requirements.</li>
     * </ul>
     * <p>It updates and notifies all players about the drawing player's updated hand, the new state of visible cards and decks, the drawing player's updated score, resources, and codex. Finally, it advances the turn to the next player.
     * Error handling is implemented by catching RemoteException, printing an error message, and exiting the game. Consider implementing more robust error handling and recovery mechanisms instead of exiting the game on exceptions. The method could benefit from additional comments explaining the logic for score calculation and state updates.</p>
     *
     * @param username The username of the player drawing the card.
     * @param choose A string representing the player's choice of card to draw.
     *               "A" for resource deck, "B" for gold deck, or a card ID for visible cards.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void drawCard(String username, String choose) throws RemoteException {
        try{
        PlayerState player = gameState.getPlayerState(username);

        Card extractedCard;
        Card backGDeck;
        Card backRDeck;
        if (choose.equals("A")) {
            extractedCard = (Card) gameState.getResourceDeck().drawCard();
        } else if (choose.equals("B")) {
            extractedCard = (Card) gameState.getGoldDeck().drawCard();
        } else {
            int id = Integer.parseInt(choose);
            if (id < 40) {
                extractedCard = gameState.getResourceCardsVisible().stream()
                        .filter(card1 -> card1.getId() == id)
                        .findAny()
                        .get();
                gameState.removeResourceCardVisible(extractedCard);
                gameState.addCardToResourceCardsVisible((Card) gameState.getResourceDeck().drawCard());
            } else {
                extractedCard = gameState.getGoldCardsVisible().stream()
                        .filter(card1 -> card1.getId() == id)
                        .findAny()
                        .get();
                gameState.removeGoldCardVisible(extractedCard);
                gameState.addCardToGoldCardsVisible((Card) gameState.getGoldDeck().drawCard());
            }
        }
        // add new card to player hand
        player.addCardToHand(extractedCard);

        if (cardToPlace instanceof ResourceCard x) {
            player.addScore(x.getPoint());
        } else {
            Map<Resource, Integer> res = player.calculateResources();
            var x = (GoldCard) cardToPlace;
            if (x.isFrontSide()) {
                player.addScore(res.get(x.getPoint().getResource()) * x.getPoint().getQta());
            }
        }

        player.calculateResources();

        // notify player of updated hand
        orderedObserversMap.get(username).update(new UpdatedHandMessage("updateHand", new ArrayList<>(player.getHand())));

        //notify al players of new decks states
        List<Card> updatedDecks = new ArrayList<>();
        updatedDecks.addAll(gameState.getResourceCardsVisible());
        updatedDecks.addAll(gameState.getGoldCardsVisible());
        updatedDecks.add(gameState.getResourceDeck().getCards().get(0));
        updatedDecks.add(gameState.getGoldDeck().getCards().get(0));
        //notify observers of the updated decks
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new UpdatedDecksMessage("updateDecks", updatedDecks)) ;

        //update all players of the player's resources count, score and codex, also the player that played
        ViewModelPlayerState updatedPlayerState = new ViewModelPlayerState();
        updatedPlayerState.setScore(player.getScore());
        updatedPlayerState.setCodex(player.getCodex());
        updatedPlayerState.setPersonalResources(player.calculateResources());
        Map<String, ViewModelPlayerState> updatedPlayer = new HashMap<>();
        updatedPlayer.put(username, updatedPlayerState);
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new UpdatedPlayerStateMessage("updatePlayerState", updatedPlayer ));

        // advance turn
        advanceTurn();
    }catch (RemoteException e) {
        System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
    }
    }

    /**
     * Broadcasts a received message to all registered observers.
     *
     * This method iterates through all observers stored in the orderedObserversMap
     * and calls their update method with a new chat message containing the received message.
     *
     * @param message The Message object to be broadcasted to all observers.
     * @throws RemoteException If there's an error during remote method invocation.
     *                         In case of a RemoteException, the method prints an error message
     *                         and calls exitGame("error").
     */
    public void receivedMessageBroadcast(Message message) throws RemoteException{
        try {
            for (String us : orderedObserversMap.keySet()) {
                orderedObserversMap.get(us).update(new newChatMessage("newMessage", message));
            }
        }catch (RemoteException e) {
                System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
        }
    }

    /**
     * Sends a private message to the specified sender and receiver.
     *
     * This method updates both the sender and the receiver of the private message
     * by calling their respective update methods with a new chat message.
     *
     * @param message The MessagePrivate object containing the private message details,
     *                including sender and receiver information.
     * @throws RemoteException If there's an error during remote method invocation.
     *                         In case of a RemoteException, the method prints an error message
     *                         and calls exitGame("error").
     */
    public void receivedMessagePrivate(MessagePrivate message) throws RemoteException{
        try {
            orderedObserversMap.get(message.getSender()).update(new newChatMessage("newMessage", message));
            orderedObserversMap.get(message.whoIsReceiver()).update(new newChatMessage("newMessage", message));
        }catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
        }
    }

    /**
     * Notifies a specific user that it's not their turn.
     *
     * This method sends a "not your turn" message to the specified user by updating
     * their observer with a NotYourTurnMessage.
     *
     * @param username The username of the player to be notified.
     * @throws RemoteException If there's an error during remote method invocation.
     *                         In case of a RemoteException, the method prints an error message
     *                         and calls exitGame("error").
     */
    public void notYourTurn(String username) throws RemoteException {
        try{
        orderedObserversMap.get(username).update(new NotYourTurnMessage("notYourTurn", username));
        }catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
        }
    }

    /**
     * Displays the menu for a specific player.
     *
     * <p>This method sends a request to display the menu to the specified player identified
     * by their username. It updates the observer associated with the player to show the menu.
     * If a RemoteException occurs, it catches the exception, prints an error message, and
     * exits the game.</p>
     *
     * @param username The username of the player for whom the menu should be displayed.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void displayMenu(String username) throws RemoteException {
        try{
        orderedObserversMap.get(username).update(new displayMenu("displayMenu", username));
        }catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
        }
    }

    /**
     * Updates and displays the scoreboard for a specific user.
     *
     * This method creates a scoreboard by collecting the current scores of all players
     * from the game state. It then sends this scoreboard to the specified user.
     *
     * @param username The username of the player to whom the scoreboard will be displayed.
     * @throws RemoteException If there's an error during remote method invocation.
     *                         In case of a RemoteException, the method prints an error message
     *                         and calls exitGame("error").
     */
    public void scoreboard(String username) throws RemoteException {
        try{
        Map<String, Integer> scoreboard = new HashMap<>();
        for (String player : orderedObserversMap.keySet()) {
            int newScore = gameState.getPlayerState(player).getScore();
            scoreboard.put(player, newScore);
        }
        orderedObserversMap.get(username).update(new DisplayScoreboard("displayScoreboard", scoreboard));
        }catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
        }
    }

    /**
     * Updates the boards for all observers with the provided data.
     *
     * <p>This method sends an update to all registered observers with the given board data.
     * It iterates through the list of observers and updates each one. If a RemoteException occurs
     * during the update of any observer, the exception is caught, an error message is printed,
     * and the game exits.</p>
     *
     * @param data A list of strings representing the board data to be sent to the observers.
     * @throws RemoteException If there's an error in remote method invocation.
     */
    public void updateBoards(List<String> data) throws RemoteException{
        for (String us : orderedObserversMap.keySet()) {
            try {
                orderedObserversMap.get(us).update(new updateBoards("Boards", data));
            }catch (RemoteException e) {
                System.out.println("RemoteException caught: " + e.getMessage());
                exitGame("error");
            }
        }
    }

    /**
     * Handles the logic for the last turn of the game.
     *
     * This method is called when a player's score reaches or exceeds 20 points, or when the last turn flag is set.
     * It calculates final scores, including objective card points, and determines the game's winner.
     * If it's not the last player's turn, it advances to the next player.
     *
     * @param username The username of the current player.
     * @throws RemoteException If there's an error during remote method invocation.
     *                         In case of a RemoteException, the method prints an error message
     *                         and calls exitGame("error").
     */
    public void lastTurn(String username) throws RemoteException {
        try{
        PlayerState player = gameState.getPlayerState(username);
        //System.out.println("User: " + username + " Score: " + player.getScore());
        if (player.getScore() >= 20 || last == true) {  // also if checked in advanceTurn
            last = true;
            if(username == orderedObserversMap.keySet().toArray()[orderedObserversMap.size() - 1]){
                // calculate points
                List<Pair<String, Integer>> rank = new ArrayList<>();
                Map<String, Integer> scoresWOobj = new HashMap<>();
                // TODO: check if this is correct
                for (String us : orderedObserversMap.keySet()) {
                    PlayerState ps = gameState.getPlayerState(us);
                    int preScore = ps.getScore();
                    System.out.println("Pre score: " + preScore);
                    Objective card = null;
                    for (int j = 0; j < 3; j++) {
                        if (j == 0) {
                            card = player.getSecretObj();
                        }
                        if (j == 1) {
                            card = gameState.getCommonObjective(0);
                        }
                        if (j == 2) {
                            card = gameState.getCommonObjective(1);
                        }
                        String type = card.getType();
                        System.out.println("Objective: " + card);
                        System.out.println("Type objective: " + type);
                        switch (type) {
                            case "L" -> {
                                LObjective c = (LObjective) card;
                                c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                            }
                            case "ReverseL" -> {
                                ReverseLObjective c = (ReverseLObjective) card;
                                c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                            }
                            case "DownL" -> {
                                DownLObjective c = (DownLObjective) card;
                                c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                            }
                            case "DownReverseL" -> {
                                DownReverseLObjective c = (DownReverseLObjective) card;
                                c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                            }
                            case "SxDiagonal" -> {
                                SxDiagonalObjective c = (SxDiagonalObjective) card;
                                c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                            }
                            case "DxDiagonal" -> {
                                DxDiagonalObjective c = (DxDiagonalObjective) card;
                                c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                            }
                            case "Resource" -> {
                                ResourceObjective c = (ResourceObjective) card;
                                c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                            }
                            default -> {
                                System.out.println("Error in type of objective");
                            }
                        }
                    }
                    int scoreObj = gameState.getPlayerState(us).getScore() - preScore;
                    scoresWOobj.put(us, scoreObj);
                    rank.add(new Pair<>(us, gameState.getPlayerState(us).getScore()));
                }
                Collections.sort(rank, new Comparator<Pair<String, Integer>>() {
                    @Override
                    public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                        if(o1.getValue().equals(o2.getValue()))
                            return scoresWOobj.get(o2.getKey()).compareTo(scoresWOobj.get(o1.getKey()));
                        else{
                            return o2.getValue().compareTo(o1.getValue());
                        }
                    }
                });
                for (String us : orderedObserversMap.keySet()) {
                    orderedObserversMap.get(us).update(new EndGameMessage("endGame", rank));
                    notYourTurn(us);
                }
            } else {
                currentPlayerIndex = (currentPlayerIndex + 1) % orderedObserversMap.size();
                String us = gameState.getPlayerOrder().get(currentPlayerIndex);
                orderedObserversMap.get(us).update(new LastTurnMessage("lastTurn", null));
                notifyCurrentPlayerTurn();
            }
        }
        }catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
        }
    }

    /**
     * Ends the game and notifies all observers.
     *
     * <p>This method sends an end game message to all registered observers to notify them
     * that the game has ended. It iterates through the list of observers and updates each one
     * with an EndGameMessage. If a RemoteException occurs during the update of any observer,
     * the exception is caught and an error message is printed to the console.</p>
     *
     * @param username The username of the player who initiated the game exit.f
     */
    public void exitGame(String username) {
        for(String us : orderedObserversMap.keySet()) {
            try {
                orderedObserversMap.get(us).update(new EndGameMessage("endGame", null));
            } catch (RemoteException e) {
                System.out.println("RemoteException caught: " + e.getMessage());
            }
        }
    }

    /**
     * Checks the connection with all observers.
     *
     * <p>This method sends a connection check message to all registered observers to verify
     * their connectivity status. It iterates through the list of observers and updates each one
     * with a checkConnection message. If a RemoteException occurs during the update of any observer,
     * the exception is caught, an error message is printed to the console, and the game exits.</p>
    */
    public void checkConnection() {
        try {
            for (String us : orderedObserversMap.keySet()) {
                orderedObserversMap.get(us).update(new checkConnection("checkConnection", null));
            }
        }catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            exitGame("error");
        }
    }

    public BlockingQueue<PlayerMove> getMoveQueue(){
        return moveQueue;
    }

    public void addObserverToMap(String username, GameObserver observer) {
        orderedObserversMap.put(username, observer);
    }

    public void setCardToPlace(Card cardToPlace) {
        this.cardToPlace = cardToPlace;
    }

    public void setTest(Map<Integer, Map<Integer, List<Coordinate>>> test) {
        this.test = test;
    }
}