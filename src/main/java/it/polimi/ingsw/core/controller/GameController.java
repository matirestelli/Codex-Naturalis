package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Resource;
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
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("loadedStarter", gameState.getPlayerState(us).getStarterCard()));

        // assign the first hand of cards to each player
        gameState.assignFirstHandToPlayers();
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("updateHand", gameState.getPlayerState(us).getHand()));

        // assign common objectives of the game
        // TODO: implement for loop to draw n cards. Define n as class variable
        gameState.addCommonObjective((Objective) gameState.getObjectiveDeck().drawCard());
        gameState.addCommonObjective((Objective) gameState.getObjectiveDeck().drawCard());

        // notify observers of the common objectives
        for (String us : orderedObserversMap.keySet())
            orderedObserversMap.get(us).update(new GameEvent("loadedCommonObjective", gameState.getCommonObjectives()));

        // assign secret objectives to each player
        for (String us : orderedObserversMap.keySet()) {
            // TODO: implement for loop to draw n cards. Define n as class variable
            List<CardGame> secretChoose = new ArrayList();
            secretChoose.add(gameState.getObjectiveDeck().drawCard());
            secretChoose.add(gameState.getObjectiveDeck().drawCard());
            // notify observers of the secret objectives
            orderedObserversMap.get(us).update(new GameEvent("chooseObjective", secretChoose));
        }

        // set side of starter card for each player
        for (String us : orderedObserversMap.keySet()) {
            // notify observers of the side of the starter card
            orderedObserversMap.get(us).update(new GameEvent("starterSide", gameState.getPlayerState(us).getStarterCard()));
        }
    }

    // TODO: Fix this method, launching exception in case of false offer
    @Override
    public synchronized void handleMove(String username, GameEvent event) throws RemoteException {
        // add the move to the queue
        boolean offer = moveQueue.offer(new PlayerMove(username, event));
        if (!offer) {
            System.out.println("Move not added to the queue");
        }
    }

    public void processMove(String username, GameEvent event) throws RemoteException {
        System.out.println("Processing move: " + event.getType());
        String type = event.getType();
        switch (type) {
            case "cardSelection" -> {
                CardSelection cardSelection = (CardSelection) event.getData();
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
                orderedObserversMap.get(username).update(new GameEvent("askAngle", angoliDisponibili));

                // advanceTurn();
            }
            case "secretObjectiveSelection" -> {
                Objective cardSelected = (Objective) event.getData();
                // set secret objective of player given by username
                gameState.setSecretObjective(username, cardSelected);
                // chooseObjective(username, card);
            }
            case "starterSideSelection" -> {
                boolean side = (boolean) event.getData();
                // assign side of starter card to player given by username
                gameState.assignStarterSide(username, side);
                // place starter card in middle of matrix
                gameState.placeStarterInMatrix(username, matrixDimension);

                playersReadyToPlayer.add(username);

                if (playersReadyToPlayer.size() == gameState.getPlayerOrder().size()) {
                    System.out.println("Iniziano i turni");
                    notifyCurrentPlayerTurn();
                }
            }
            case "angleSelection" -> {
                angleChosen(username, (CardToAttachSelected) event.getData());
            }
            case "drawCard" -> {
                drawCard(username, (String) event.getData());
            }
            default -> {
                System.out.println("Unknown move type: " + type);
            }
        }
    }

    public void processMoves() {
        while (true) {
            try {
                PlayerMove playerMove = moveQueue.take(); // Blocca fino a quando un elemento è disponibile
                processMove(playerMove.getUsername(), playerMove.getEvent());
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
                orderedObserversMap.get(username).update(new GameEvent("notYourTurn", us));
        }
        GameEvent beforeTurnEvent = new GameEvent("beforeTurnEvent", gameState.calculateResource(us));
        orderedObserversMap.get(us).update(beforeTurnEvent);
        // send list of ids of playable cards from playing hand
        GameEvent turnEvent = new GameEvent("currentPlayerTurn", gameState.getPlayableCardIdsFromHand(us));
        orderedObserversMap.get(us).update(turnEvent);
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
        orderedObserversMap.get(username).update(new GameEvent("updateCodex", new ArrayList<>(player.getCodex())));

        player.getMatrix()[cardToPlace.getyMatrixCord()][cardToPlace.getxMatrixCord()] = cardToPlace.getId();

        List<Card> visibileCards = new ArrayList<>();
        visibileCards.addAll(gameState.getResourceCardsVisible());
        visibileCards.addAll(gameState.getGoldCardsVisible());

        // TODO: fix this update method
        orderedObserversMap.get(username).update(new GameEvent("askWhereToDraw", visibileCards));
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
        orderedObserversMap.get(username).update(new GameEvent("updateHand", new ArrayList<>(player.getHand())));

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

    public void lastTurn(String username) throws RemoteException {
        PlayerState player = gameState.getPlayerState(username);
        Boolean last = false;
        if (player.getScore() >= 20 || last == true) {
            last = true;
            if (currentPlayerIndex == observers.size() - 1) {
                // calculate points
                List<Integer> rank = null;
                Map<Integer, Integer> scores = null;
                // TODO: check if this is correct
                for (int i = 0; i < observers.size(); i++) {

                    // PlayerState player = gameState.getPlayerState(currentPlayerIndex);
                    int preScore = player.getScore();
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
                            throw new IllegalArgumentException("Invalid card type");
                        }
                    }

                    int scoreObj = player.getScore() - preScore;
                    scores.put(currentPlayerIndex, scoreObj);
                    rank.add(currentPlayerIndex);
                }
                Collections.sort(rank, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer index1, Integer index2) {
                        PlayerState player1 = gameState.getPlayerState(index1);
                        PlayerState player2 = gameState.getPlayerState(index2);
                        if (player1.getScore() == player2.getScore()) {
                            return Integer.compare(scores.get(index2), scores.get(index1)); // Ordine decrescente
                        } else {
                            return Integer.compare(player2.getScore(), player1.getScore()); // Ordine decrescente
                        }
                    }
                });

                orderedObserversMap.get(username).update(new GameEvent("endGame", gameState.getPlayerState(currentPlayerIndex)));
            } else {
                orderedObserversMap.get(username).update(new GameEvent("lastTurn", gameState.getPlayerState(currentPlayerIndex)));
            }
        }
    }
}