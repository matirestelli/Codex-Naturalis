package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.core.model.message.response.messageBroadcast;
import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.GameServer;
import it.polimi.ingsw.network.socket.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameControllerExceptionTest {

    private GameController gameController;
    static private GameSessionManager sessionManager;
    private HandlerTest observer;
    private GameState gameState;

    @BeforeEach
    void setUp() throws RemoteException {
        gameState = new GameState();
        observer = new HandlerTest();
        gameController = new GameController(gameState);
        sessionManager = GameSessionManager.getInstance();
        Player player = new Player("us1");
        gameState.addPlayer(player);
        gameController.addObserver("us1", observer);
        gameController.addObserver("us1", observer);
        gameController.startGame();
    }

    @Test
    void testHandleMove() throws RemoteException {
        Message m = new Message("messageToAll", "message");
        MessageClient2Server message = new messageBroadcast("messageToAll", m);
        gameController.handleMove("us1", message);
        assertEquals(1, gameController.getMoveQueue().size());
    }

    @Test
    void testDrawCard() throws RemoteException {
        ResourceCard card = new ResourceCard();
        card.setId(2);
        gameController.setCardToPlace(card);
        Deck deck = new Deck("deck", ResourceCard.class);
        List<Card> cards = new ArrayList<Card>();
        ResourceCard card1 = new ResourceCard();
        ResourceCard card2 = new ResourceCard();
        ResourceCard card3 = new ResourceCard();
        ResourceCard card4 = new ResourceCard();
        card4.setId(4);
        card3.setId(3);
        card2.setId(45);
        card1.setId(12);
        card1.setSide(true);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        deck.setCards(cards);
        gameState.setResourceDeck(deck);
        gameState.getResourceCardsVisible().add(card1);
        gameState.getGoldCardsVisible().add(card2);
        gameController.drawCard("us1", "A");
        gameController.drawCard("us1", "B");
        gameController.drawCard("us1", "12");
        gameController.drawCard("us1", "45");
        GoldCard goldCard = new GoldCard();
        Deck deck2 = new Deck("deck", GoldCard.class);
        goldCard.setId(1);
        gameState.getGoldCardsVisible().add(goldCard);
        deck2.getCards().add(goldCard);
        gameState.setGoldDeck(deck2);
        gameController.setCardToPlace(goldCard);
        gameController.drawCard("us1", "A");
        goldCard.setSide(true);
        Point point = new Point();
        point.setQta(5);
        point.setResource(Resource.ANIMAL);
        goldCard.setPoint(point);
        cards.add(card1);
        cards.add(card2);
        deck.setCards(cards);
        gameState.setResourceDeck(deck);
        gameController.drawCard("us1", "A");
    }

    @Test
    void testHandleChoseObjective() throws RemoteException {
        Objective SecretObj = new Objective();
        gameController.handleChoseObjective("us1", SecretObj);
        assertEquals(SecretObj, gameState.getPlayerState("us1").getSecretObj());
    }

    @Test
    void testHandleStarterSideSelected() throws RemoteException {
        ResourceCard starterCard = new ResourceCard();
        gameState.getPlayerState("us1").setStarterCard(starterCard);
        gameState.getPlayerState("us1").initializeMatrix(81);
        gameController.handleStarterSideSelected("us1", true);
        assertEquals(true, gameState.getPlayerState("us1").getStarterCard().isFrontSide());
        gameController.handleStarterSideSelected("us1", true);

    }

    @Test
    void testHandleCardSelected() throws RemoteException {
        gameState.getPlayerState("us1").initializeMatrix(81);
        //setup
        ResourceCard cardtoplay = new ResourceCard();
        cardtoplay.setId(5);
        Map<Integer, Corner> corners = new HashMap<Integer, Corner>();
        Corner corner0 = new Corner();
        corner0.setHidden(true);
        corners.put(0, corner0);
        cardtoplay.setBackCorners(corners);
        CardSelection cardSelection = new CardSelection(5, false);

        gameState.getPlayerState("us1").initializeMatrix(81);
        gameState.getPlayerState("us1").addCardToHand(cardtoplay);
        ResourceCard card1 = new ResourceCard();
        card1.setId(1);
        card1.setSide(true);
        card1.setFrontCorners(corners);
        gameState.getPlayerState("us1").addCardToCodex(card1);
        gameState.getPlayerState("us1").getMatrix()[40][40] = 1;
        gameController.handleCardSelected("us1", cardSelection);


        card1.getFrontCorners().get(0).setHidden(false);
        gameState.getPlayerState("us1").addCardToHand(cardtoplay);
        gameState.getPlayerState("us1").addCardToCodex(cardtoplay);
        gameController.handleCardSelected("us1", cardSelection);
    }

    @Test
    void testRemoveObserver() throws RemoteException {
        gameController.removeObserver(observer);
    }

    @Test
    void testnotifyCurrentPlayerTurn() throws RemoteException {
        gameController.addObserverToMap("us2", observer);
        gameController.notifyCurrentPlayerTurn();
    }

    @Test
    void testAdvanceTurn() throws RemoteException {
        gameController.advanceTurn();
        gameState.getPlayerState("us1").setScore(25);
        List<Objective> objectives = new ArrayList<Objective>();
        Card objective1 = new LObjective();
        Card objective2 = new ReverseLObjective();
        Objective secretobj = new DownLObjective();
        ((Objective) objective1).setType("L");
        ((Objective) objective2).setType("ReverseL");
        ((Objective) secretobj).setType("DownL");
        objectives.add((Objective) objective1);
        objectives.add((Objective) objective2);
        gameState.setCommonObjective(objectives);
        gameState.getPlayerState("us1").setSecretObj((Objective) secretobj);
        gameController.advanceTurn();
        gameState.getPlayerState("us1").setScore(15);
        gameController.advanceTurn();

        Card objective3 = new ResourceObjective();
        Card objective4 = new Objective();
        ((Objective) objective3).setType("Resource");
        ((ResourceObjective) objective3).getRequirements().add(new Requirement(Resource.ANIMAL, 1));
        ((Objective) objective4).setType("def");
        List<Objective> objectives2 = new ArrayList<Objective>();
        objectives2.add((Objective) objective3);
        objectives2.add((Objective) objective4);
        gameState.setCommonObjective(objectives2);
        gameController.advanceTurn();
    }

    @Test
    void testUpdateCodex() throws RemoteException {
        gameState.getPlayerState("us1").getCodex().clear();
        List<Card> cards = new ArrayList<Card>();
        cards.add(new ResourceCard());
        ResourceCard card = new ResourceCard();
        card.setId(1);
        cards.add(card);
        gameState.getPlayerState("us1").initializeMatrix(81);
        gameState.getPlayerState("us1").addCardToCodex(card);
        assertEquals(1, gameState.getPlayerState("us1").getCodex().size());
        gameController.updateCodex("us1", cards);
        assertEquals(2, gameState.getPlayerState("us1").getCodex().size());
    }

    @Test
    void testDisplayMenu() throws RemoteException {
        gameController.displayMenu("us1");
    }

    @Test
    void testScoreboard() throws RemoteException {
        gameController.scoreboard("us1");
    }

    @Test
    void testUpdateBoards() throws RemoteException {
        List<String> data = new ArrayList<String>();
        data.add("us1");
        gameController.updateBoards(data);
    }

    @Test
    void testLastTurn() throws RemoteException {
        gameState.getPlayerState("us1").setScore(15);
        gameController.lastTurn("us1");
        gameState.getPlayerState("us1").setScore(25);
        List<Objective> objectives = new ArrayList<Objective>();
        Card objective1 = new SxDiagonalObjective();
        Card objective2 = new DownReverseLObjective();
        Objective secretobj = new DxDiagonalObjective();
        Objective secretobj2 = new DxDiagonalObjective();
        ((Objective) secretobj2).setType("Resource");
        ((Objective) objective1).setType("SxDiagonal");
        ((Objective) objective2).setType("DownReverseL");
        ((Objective) secretobj).setType("DxDiagonal");
        objectives.add((Objective) objective1);
        objectives.add((Objective) objective2);
        gameState.setCommonObjective(objectives);
        gameState.getPlayerState("us1").setSecretObj(secretobj);
        gameController.lastTurn("us1");


        gameState.getPlayerOrder().add("us2");
        gameState.addPlayer(new Player("us2"));
        gameState.getPlayerState("us2").setSecretObj(secretobj2);
        gameState.getPlayerState("us2").setScore(25);
        gameState.getPlayerState("us2").initializeMatrix(81);
        gameController.lastTurn("us1");
        gameController.lastTurn("us2");
        gameState.getPlayerState("us2").setScore(24);
        gameController.lastTurn("us2");
    }

    @Test
    void testExitGame() throws RemoteException {
        gameController.exitGame("us1");
    }

    @Test
    void testCheckConnection() throws RemoteException {
        gameController.checkConnection();
    }

    @Test
    void testReceivedMessagePrivate() throws RemoteException {
        MessagePrivate m = new MessagePrivate("Message", "us1", "us1");
        gameController.receivedMessagePrivate(m);
    }

    @Test
    void testHandleCardSelected1() throws RemoteException {
        gameState.getPlayerState("us1").getCodex().clear();
        //setup
        ResourceCard cardtoplay = new ResourceCard();
        cardtoplay.setId(5);
        Map<Integer, Corner> corners = new HashMap<Integer, Corner>();
        Corner corner0 = new Corner();
        corner0.setResource(Resource.ANIMAL);
        corner0.setHidden(true);
        corners.put(0, corner0);
        cardtoplay.setBackCorners(corners);
        CardSelection cardSelection = new CardSelection(5, false);

        gameState.getPlayerState("us1").initializeMatrix(81);
        gameState.getPlayerState("us1").addCardToHand(cardtoplay);
        ResourceCard card1 = new ResourceCard();
        card1.setId(1);
        card1.setSide(true);
        card1.setFrontCorners(corners);
        gameState.getPlayerState("us1").addCardToCodex(card1);
        gameState.getPlayerState("us1").getMatrix()[40][40] = 1;
        gameController.handleCardSelected("us1", cardSelection);


        card1.getFrontCorners().get(0).setHidden(false);
        gameState.getPlayerState("us1").addCardToHand(cardtoplay);
        gameController.handleCardSelected("us1", cardSelection);
    }

    @Test
    void testAngleChosen2() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(0, corner);
        card.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.2", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen1() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(0, corner);
        card.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.1", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen0() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(0, corner);
        card.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.0", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen3() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(0, corner);
        card.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.3", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen4() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(0, corner);
        card.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.4", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(0, corner);
        card.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.2", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        coordinates.add(new Coordinate(18, 40));
        cooMap.put(2, coordinates);
        test.put(16, cooMap);

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen5() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(0, corner);
        card.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.2", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        coordinates.add(new Coordinate(18, 40));
        cooMap.put(1, coordinates);
        test.put(16, cooMap);

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen6() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(1, corner);
        card2.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.2", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setSide(true);
        cardToAttachSelected.getCodex().get(2).setFrontCorners(corners);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        coordinates.add(new Coordinate(16, 2));
        cooMap.put(2, coordinates);
        test.put(16, cooMap);

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen7() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(1, corner);
        card2.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.2", cards);
        cardToAttachSelected.getCodex().get(1).setId(3);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setSide(true);
        cardToAttachSelected.getCodex().get(2).setFrontCorners(corners);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        coordinates.add(new Coordinate(16, 1));
        cooMap.put(2, coordinates);
        test.put(16, cooMap);

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testAngleChosen8() throws RemoteException {
        Card card = new ResourceCard();
        card.setSide(true);

        Card card2 = new ResourceCard();
        card.setSide(true);

        Card card3 = new ResourceCard();
        card.setId(18);
        card.setSide(true);

        Map<Integer, Corner> corners = new HashMap<Integer,Corner>();
        Corner corner = new Corner();
        corner.setResource(Resource.ANIMAL);
        corners.put(1, corner);
        card2.setFrontCorners(corners);

        List<Card> cards = new ArrayList<Card>();
        cards.add(card);
        cards.add(card2);
        cards.add(card3);


        CardToAttachSelected cardToAttachSelected = new CardToAttachSelected("16.2", cards);
        cardToAttachSelected.getCodex().get(1).setSide(true);
        cardToAttachSelected.getCodex().get(1).setFrontCorners(corners);
        cardToAttachSelected.getCodex().get(1).setId(17);
        cardToAttachSelected.getCodex().get(2).setId(16);
        cardToAttachSelected.getCodex().get(2).setSide(true);
        cardToAttachSelected.getCodex().get(2).setFrontCorners(corners);
        cardToAttachSelected.getCodex().get(2).setXYCord(40, 40);

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        Map<Integer, List<Coordinate>> cooMap = new HashMap<Integer, List<Coordinate>>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<Integer, Map<Integer, List<Coordinate>>>();

        coordinates.add(new Coordinate(17, 1));
        cooMap.put(2, coordinates);
        test.put(16, cooMap);

        gameController.setTest(test);
        gameController.angleChosen("us1", cardToAttachSelected);
    }

    @Test
    void testNotYourTurn() throws RemoteException {
        gameController.notYourTurn("us1");
    }
}


