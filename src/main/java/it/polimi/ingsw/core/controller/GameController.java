package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.request.*;
import it.polimi.ingsw.core.model.message.response.DisplayMenu;
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
            //send to all the players the players pawn
            System.out.println("Turn order: " + gameState.getPlayerOrder());
            notifyCurrentPlayerTurn();
        }
    }

    public void handleCardSelected(String username, CardSelection cardSelection) throws RemoteException {
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
    }

    @Override
    public void advanceTurn() throws RemoteException {
        if (gameState.getPlayerState(gameState.getPlayerOrder().get(currentPlayerIndex)).getScore() >= 20 || last == true) {
            lastTurn(gameState.getPlayerOrder().get(currentPlayerIndex));
        } else {
            System.out.println("User: " + gameState.getPlayerOrder().get(currentPlayerIndex) + " Score: " + gameState.getPlayerState(gameState.getPlayerOrder().get(currentPlayerIndex)).getScore());
            currentPlayerIndex = (currentPlayerIndex + 1) % orderedObserversMap.size();
            notifyCurrentPlayerTurn();
        }
    }

    public void updateCodex(String username, List<Card> codex) throws RemoteException {
        gameState.getPlayerState(username).setCodex(codex);
    }

    public void angleChosen(String username, CardToAttachSelected cardToAttach) throws RemoteException {
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

        //todo vedere se ha senso
        //player.getCodex().getLast().setXYCord(cardToPlace.getyMatrixCord(), cardToPlace.getxMatrixCord());

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
        //todo capire perchè la prima del mazzo non corrisponde alla prima del mazzo il cli
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
    }

    public void receivedMessageBroadcast(Message message){
        for (String us : orderedObserversMap.keySet()) {
            try {
                if(!us.equals(message.getSender()))
                    orderedObserversMap.get(us).update(new newChatMessage("newMessage", message));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void receivedMessagePrivate(MessagePrivate message) {
        try {
            orderedObserversMap.get(message.whoIsReceiver()).update(new newChatMessage("newMessage", message));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void notYourTurn(String username) throws RemoteException {
        orderedObserversMap.get(username).update(new NotYourTurnMessage("notYourTurn", username));
    }

    public void displayMenu(String username) throws RemoteException {
        orderedObserversMap.get(username).update(new displayMenu("displayMenu", username));
    }

    public void scoreboard(String username) throws RemoteException {
        Map<String, Integer> scoreboard = new HashMap<>();
        for (String player : orderedObserversMap.keySet()) {
            int newScore = gameState.getPlayerState(player).getScore();
            scoreboard.put(player, newScore);
        }
        orderedObserversMap.get(username).update(new DisplayScoreboard("displayScoreboard", scoreboard));
    }



    public void updateBoards(List<String> data){
        for (String us : orderedObserversMap.keySet()) {
            try {
                orderedObserversMap.get(us).update(new updateBoards("Boards", data));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void lastTurn(String username) throws RemoteException {
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
    }
}