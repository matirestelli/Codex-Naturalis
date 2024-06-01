package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelViewTest {
    private ModelView modelView;

    @BeforeEach
    void setUp() {
        modelView = new ModelView();
    }

    @Test
    void testSetAndGetMyPlayingCard() {
        Card card = new ResourceCard();
        modelView.setMyPlayingCard(card);
        assertEquals(card, modelView.getMyPlayingCard());
    }

    @Test
    void testSetAndGetChat() {
        Chat chat = new Chat();
        modelView.setChat(chat);
        assertEquals(chat, modelView.getChat());
    }

    @Test
    void testSetAndGetUnreadedMessages() {
        modelView.setMyUnreadedMessages(5);
        assertEquals(5, modelView.getMyUnreadedMessages());
    }

    @Test
    void testSetAndGetMyTurn() {
        modelView.setMyTurn(true);
        assertTrue(modelView.isMyTurn());
    }

    @Test
    void testSetAndGetMyMatrix() {
        int[][] matrix = new int[81][81];
        modelView.setMyMatrix(matrix);
        assertArrayEquals(matrix, modelView.getMyMatrix());
    }

    @Test
    void testSetAndGetSecretObj() {
        Objective objective = new Objective();
        modelView.setSecretObj(objective);
        assertEquals(objective, modelView.getSecretObj());
    }

    @Test
    void testSetAndGetMyStarterCard() {
        ResourceCard card = new ResourceCard();
        modelView.setMyStarterCard(card);
        assertEquals(card, modelView.getMyStarterCard());
    }

    @Test
    void testSetAndGetMyHand() {
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new ResourceCard());
        modelView.setMyHand(hand);
        assertEquals(hand, modelView.getMyHand());
    }

    @Test
    void testSetAndGetMyCodex() {
        List<Card> codex = new ArrayList<>();
        codex.add( new ResourceCard());
        modelView.setMyCodex(codex);
        assertEquals(codex, modelView.getMyCodex());
    }

    @Test
    void testSetAndGetMyUsername() {
        modelView.setMyUsername("username");
        assertEquals("username", modelView.getMyUsername());
    }

    @Test
    void testSetAndGetMyColor() {
        modelView.setMyColor(Color.BLUE);
        assertEquals(Color.BLUE, modelView.getMyColor());
    }

    @Test
    void testSetAndGetMyScore() {
        modelView.setMyScore(5);
        assertEquals(5, modelView.getMyScore());
    }

    @Test
    void testSetAndGetMyResources() {
        HashMap<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.ANIMAL, 5);
        modelView.setMyResources(resources);
        assertEquals(resources, modelView.getMyResources());
    }

    @Test
    void testAddCardToCodex() {
        List<Card> codex = new ArrayList<>();
        codex.add(new ResourceCard());
        modelView.setMyCodex(codex);
        Card card = new ResourceCard();
        modelView.addCardToCodex(card);
        assertTrue(modelView.getMyCodex().contains(card));
    }

    @Test
    void testSetAndGetPlayerStates() {
        HashMap<String, ViewModelPlayerState> playerStates = new HashMap<>();
        modelView.setPlayerStates(playerStates);
        assertEquals(playerStates, modelView.getPlayerStates());
    }

    @Test
    void testCreatePlayerStates() {
        List<String> usernames = new ArrayList<>();
        usernames.add("username");
        modelView.createPlayerStates(usernames);
        assertTrue(usernames.containsAll(modelView.getPlayerStates().keySet()));
        assertEquals(usernames.size(), modelView.getPlayerStates().size());
    }

    @Test
    void testSetStateOfPlayer() {
        ViewModelPlayerState state = new ViewModelPlayerState();
        modelView.setStateOfPlayer("username", state);
        assertEquals(state, modelView.getPlayerStates().get("username"));
    }

    @Test
    void testSetAndGetDeckGBack() {
        Card deckGBack = new GoldCard();
        modelView.setDeckGBack(deckGBack);
        assertEquals(deckGBack, modelView.getDeckGBack());
    }

    @Test
    void testSetAndGetDeckRBack() {
        Card deckRBack = new ResourceCard();
        modelView.setDeckRBack(deckRBack);
        assertEquals(deckRBack, modelView.getDeckRBack());
    }

    @Test
    void testSetAndGetReseourceCardVisible() {
        List<Card> resourceCardVisible = new ArrayList<>();
        resourceCardVisible.add(new ResourceCard());
        modelView.setResourceCardsVisible(resourceCardVisible);
        assertEquals(resourceCardVisible, modelView.getResourceCardsVisible());
    }

    @Test
    void testSetAndGetGoldCardVisible() {
        List<Card> goldCardVisible = new ArrayList<>();
        goldCardVisible.add(new GoldCard());
        modelView.setGoldCardsVisible(goldCardVisible);
        assertEquals(goldCardVisible, modelView.getGoldCardsVisible());
    }

    @Test
    void testSetAndGetCommonObj() {
        List<Objective> commonObj = new ArrayList<>();
        commonObj.add(new Objective());
        modelView.setCommonObj(commonObj);
        assertEquals(commonObj, modelView.getCommonObj());
    }

    @Test
    void testSetAndGetBroadcastChat() {
        List<String[]> broadcastChat = new ArrayList<>();
        broadcastChat.add(new String[]{"username", "message"});
        modelView.setBroadcastChat(broadcastChat);
        assertEquals(broadcastChat, modelView.getBroadcastChat());
    }

    @Test
    void testSetAndGetPlayers() {
        List<String> players = new ArrayList<>();
        players.add("username");
        modelView.setPlayers(players);
        assertEquals(players, modelView.getPlayers());
    }

    @Test
    void testGetPlayerOrder() {
        List<String> players = new ArrayList<>();
        players.add("username");
        modelView.setPlayers(players);
        assertTrue(players.containsAll(modelView.getPlayerOrder().keySet()));
    }

}