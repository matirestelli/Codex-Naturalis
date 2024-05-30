package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.request.*;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.core.model.message.response.StarterSideSelectedMessage;
import it.polimi.ingsw.core.utils.PlayerMove;
import it.polimi.ingsw.observers.GameObserver;

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


    public GameController(GameState gameState) throws RemoteException {
        super();
        this.gameState = gameState;
        this.observers = new LinkedHashMap<>();
        this.moveQueue = new LinkedBlockingQueue<>();
        this.moveProcessor = new Thread(this::processMoves);
        this.moveProcessor.start();
        this.currentPlayerIndex = 0;

        this.matrixDimension = 10;
    }

    @Override
    public void startGame() throws RemoteException {
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
        List<Card> updatedDecks = new ArrayList<>();
        updatedDecks.addAll(gameState.getResourceCardsVisible());
        updatedDecks.addAll(gameState.getGoldCardsVisible());
        updatedDecks.add(gameState.getResourceDeck().getCards().getFirst());
        updatedDecks.add(gameState.getGoldDeck().getCards().getFirst());
        //notify observers of the updated decks
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("updateDecks", updatedDecks)) ;


        gameState.intializePawn();

        // assign the starter card to each player
        gameState.assignStarterCardToPlayers();

        //todo then cancel it, now I need it to put username in viewModel beacause now it's a parameter of the main
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("loadedUsername", us));

        //notify observers of the list of players with their usernames (using playerorder)
        //TODO: assign a color to each player and send it also to the observers, maybe a map also for colors and username, only one event
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("loadedPlayers", gameState.getPlayerOrder()));

        // notify observers of the starter card assigned to each player
        for (String us : orderedObserversMap.keySet()) {
            orderedObserversMap.get(us).update(new StarterCardLoadedMessage("starterCardLoaded", gameState.getPlayerState(us).getStarterCard()));
        }

        // TODO: fix
        /* for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("loadedPawn", gameState.getPlayerState(us).getPawn())); */

        // assign the first hand of cards to each player
        gameState.assignFirstHandToPlayers();
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new UpdatedHandMessage("updateHand", gameState.getPlayerState(us).getHand()));


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
    }

    // TODO: Fix this method, launching exception in case of false offer
    @Override
    public synchronized void handleMove(String username, MessageClient2Server event) throws RemoteException {
        // add the move to the queue
        boolean offer = moveQueue.offer(new PlayerMove(username, event));
        if (!offer) {
            System.out.println("Move not added to the queue");
        }
    }

    public void handleChoseObjective(String username, Objective cardSelected) throws RemoteException {
        System.out.println("Chose objective: " + cardSelected);
        // set secret objective of player given by username
        gameState.setSecretObjective(username, cardSelected);
    }

    public void handleStarterSideSelected(String username, boolean side) throws RemoteException {
        System.out.println("Starter side selected: " + side);
        // assign side of starter card to player given by username
        gameState.assignStarterSide(username, side);
        // place starter card in middle of matrix
        gameState.placeStarterInMatrix(username, matrixDimension);

        playersReadyToPlayer.add(username);

        if (playersReadyToPlayer.size() == gameState.getPlayerOrder().size()) {
            System.out.println("Turn order: " + gameState.getPlayerOrder());
            notifyCurrentPlayerTurn();
        }
    }

    public void handleCardSelected(String username, CardSelection cardSelection) throws RemoteException {
        // get card from player hand by id
        Card cardToPlay = gameState.getPlayerState(username).getCardFromHand(cardSelection.getId());
        // remove selected card from player hand
        gameState.getPlayerState(username).removeCardFromHand(cardToPlay);
        // set side of selected card
        cardToPlay.setSide(cardSelection.getSide());

        // get list of possible angles to place the card
        List<Coordinate> angoliDisponibili = new ArrayList<>();
        // TODO: make test as attribute of GameState class
        test = new HashMap<>();
        PlayerState ps = gameState.getPlayerState(username);
        for (Card c : ps.getCodex())
            angoliDisponibili.addAll(c.findFreeAngles(ps.getMatrix(), ps.getCodex(), cardToPlay.getId(), test));

        // add card to player codex
        gameState.getPlayerState(username).addCardToCodex(cardToPlay);

        // TODO: set card to place as attribute of PlayerState class
        cardToPlace = cardToPlay;

        // notify player of free angles
        orderedObserversMap.get(username).update(new AvailableAnglesMessage("askAngle", angoliDisponibili));
    }

    public void processMove(String username, MessageClient2Server message) throws RemoteException {
        System.out.println("Processing move: " + message.getType());
        message.doAction(username, this);

            /* case "newMessage" -> {
                Message message = (Message) event.getData();
                System.out.println("New message: " + message.getText());
                System.out.println("Sender: " + message.getSender());
                System.out.println("Receiver: " + message.whoIsReceiver());

                if (message.whoIsReceiver().equals("all")) {
                    gameState.getPlayerState(username).getChat().addMsg(message);
                    for (String us : orderedObserversMap.keySet()) {
                        if (!us.equals(username)) {
                            gameState.getPlayerState(us).getChat().addMsg(message);
                            orderedObserversMap.get(us).update(new GameEvent("mexIncoming", message));
                        }
                    }
                } else {
                    gameState.getPlayerState(username).getChat().addMsg(message);
                    gameState.getPlayerState(message.whoIsReceiver()).getChat().addMsg(message);
                    orderedObserversMap.get(message.whoIsReceiver()).update(new GameEvent("mexIncoming", message));
                }
            } */
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
                e.printStackTrace();
            }
        }
    }

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

    @Override
    public void notifyCurrentPlayerTurn() throws RemoteException {
        String us = gameState.getPlayerOrder().get(currentPlayerIndex);
        for (String username : orderedObserversMap.keySet()) {
            if (!username.equals(us))
                orderedObserversMap.get(username).update(new NotYourTurnMessage("notYourTurn", us));
        }

        orderedObserversMap.get(us).update(new BeforeTurnMessage("beforeTurnEvent", gameState.calculateResource(us)));

        // send list of ids of playable cards from playing hand
        /* if (last) {
            orderedObserversMap.get(us).update(new GameEvent("lastTurn", gameState.getPlayerState(currentPlayerIndex)));
        } */

        orderedObserversMap.get(us).update(new YourTurnMessage("currentPlayerTurn", gameState.getPlayableCardIdsFromHand(us)));
    }

    @Override
    public void advanceTurn() throws RemoteException {
        currentPlayerIndex = (currentPlayerIndex + 1) % orderedObserversMap.size();
        notifyCurrentPlayerTurn();
    }

    public void angleChosen(String username, CardToAttachSelected cardToAttach) throws RemoteException {
        PlayerState player = gameState.getPlayerState(username);
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

        // TODO: fix this update method
        orderedObserversMap.get(username).update(new DrawNewCardMessage("askWhereToDraw", visibileCards));
    }

    public void drawCard(String username, String choose) throws RemoteException {
        PlayerState player = gameState.getPlayerState(username);

        Card extractedCard;
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
        updatedDecks.add(gameState.getResourceDeck().getCards().getFirst());
        updatedDecks.add(gameState.getGoldDeck().getCards().getFirst());
        //notify observers of the updated decks
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("updateDecks", updatedDecks)) ;

        // check if last turn
        // TODO: fix
        // lastTurn(username);


        //TODO ask if it's okay
        //update all players of the player's resources count, score and codex, also the player that played
        ViewModelPlayerstate updatedPlayerstate = new ViewModelPlayerstate();
        updatedPlayerstate.setScore(player.getScore());
        updatedPlayerstate.setCodex(player.getCodex());
        updatedPlayerstate.setPersonalResources(player.calculateResources());
        Map<String, ViewModelPlayerstate> updatedPlayer = new HashMap<>();
        updatedPlayer.put(username, updatedPlayerstate);
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("updatePlayerstate", updatedPlayer ));

        // advance turn
        advanceTurn();
    }

    /* public void lastTurn(String username) throws RemoteException {
        PlayerState player = gameState.getPlayerState(username);
        // System.out.println("User: " + username + " Score: " + player.getScore());
        if (player.getScore() >= 2 || last == true) {
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
                    // orderedObserversMap.get(username).update(new GameEvent("notYourTurn", us));

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
                        if (card instanceof SxDiagonalObjective) {
                            SxDiagonalObjective c = (SxDiagonalObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof DxDiagonalObjective) {
                            DxDiagonalObjective c = (DxDiagonalObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof LObjective) {
                            LObjective c = (LObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof ReverseLObjective) {
                            ReverseLObjective c = (ReverseLObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof DownLObjective) {
                            DownLObjective c = (DownLObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof DownReverseLObjective) {
                            DownReverseLObjective c = (DownReverseLObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof ResourceObjective) {
                            ResourceObjective c = (ResourceObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else {
                            System.out.println("INVALID CARD TYPE");
                            //throw new IllegalArgumentException("Invalid card type");
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
                    orderedObserversMap.get(us).update(new GameEvent("endGame", rank));
                }
            } else {
                orderedObserversMap.get(username).update(new GameEvent("reachedPoints", gameState.getPlayerState(currentPlayerIndex)));
            }
        }
    } */
}