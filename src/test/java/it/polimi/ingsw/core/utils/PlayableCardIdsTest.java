package it.polimi.ingsw.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayableCardIdsTest {
    private List<Integer> playingHandIds;
    private List<Integer> playingHandIdsBack;

    @BeforeEach
    void setUp() {
        playingHandIds = List.of(1, 2, 3);
        playingHandIdsBack = List.of(4, 5, 6);
    }

    @Test
    void getPlayingHandIds() {
        PlayableCardIds playableCardIds = new PlayableCardIds(playingHandIds, playingHandIdsBack);
        assertEquals(playingHandIds, playableCardIds.getPlayingHandIds());
    }

    @Test
    void getPlayingHandIdsBack() {
        PlayableCardIds playableCardIds = new PlayableCardIds(playingHandIds, playingHandIdsBack);
        assertEquals(playingHandIdsBack, playableCardIds.getPlayingHandIdsBack());
    }
}
