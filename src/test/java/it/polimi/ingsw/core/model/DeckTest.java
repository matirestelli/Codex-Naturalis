package it.polimi.ingsw.core.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class DeckTest {
    private Deck deck;
    private Gson gson;
    private Type typeOfCard;

    @BeforeEach
    public void setUp() throws Exception {
        typeOfCard = new TypeToken<List<Card>>(){}.getType();
        deck = new Deck("test", typeOfCard);
        gson = new Gson();
    }

    @Test
    public void testconstructor() {
        assertNotNull(deck.getCards());
        assertTrue(deck.isEmpty());
        assertTrue(deck.getCards().isEmpty());
    }

    @Test
    public void testSetCards() {
        List<Card> testCards = new ArrayList<>();
        testCards.add(new ResourceCard());
        deck.setCards(testCards);
        assertEquals(testCards, deck.getCards());
    }

    @Test
    public void testGetCards() {
        assertNotNull(deck.getCards());
    }

    @Test
    public void testisEmpty() {
        assertFalse(deck.isEmpty());
    }

    @Test
    public void testShuffle() {
        deck.getCards().add(new ResourceCard());
        deck.getCards().getFirst().setId(1);
        deck.getCards().add(new ResourceCard());
        deck.getCards().get(1).setId(2);
        List<Card> originalCards = new ArrayList<>(deck.getCards());
        assertEquals(originalCards.getFirst().getId(),1);
        assertEquals(originalCards.get(1).getId(),2);
        boolean orderChanged = false;
        while(!orderChanged){
            deck.shuffle();
            if(!originalCards.equals(deck.getCards())){
                orderChanged = true;
            }
        }
        assertTrue(orderChanged);
        assertTrue(deck.getCards().containsAll(originalCards));
    }

    @Test
    public void testLoadCardsFromJSON() {
        Type typeOfCard = new TypeToken<List<Card>>(){}.getType();
        deck.loadCardsFromJSON();
        assertNotNull(deck.getCards());
    }

    @Test
    public void testDrawCard() {
        deck.getCards().add(new ResourceCard());
        List<Card> originalCards = new ArrayList<>(deck.getCards());
        CardGame extractedCard = deck.drawCard();
        assertFalse(deck.getCards().contains(extractedCard));
        assertEquals(originalCards.size() - 1, deck.getCards().size());
    }








}
