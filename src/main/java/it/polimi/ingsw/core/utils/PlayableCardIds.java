package it.polimi.ingsw.core.utils;

import java.util.List;

/**
 * This class represents a PlayableCardIds in the game.
 * It implements Serializable interface.
 * It maintains the playingHandIds and playingHandIdsBack of the player state.
 */
public class PlayableCardIds implements java.io.Serializable {
    private List<Integer> playingHandIds;
    private List<Integer> playingHandIdsBack;

    /**
     * Constructor for the PlayableCardIds class.
     * @param playingHandIds The ids of the playing hand.
     * @param playingHandIdsBack The ids of the playing hand back.
     */
    public PlayableCardIds(List<Integer> playingHandIds, List<Integer> playingHandIdsBack) {
        this.playingHandIds = playingHandIds;
        this.playingHandIdsBack = playingHandIdsBack;
    }

    /**
     * Returns the ids of the playing hand.
     * @return The ids of the playing hand.
     */
    public List<Integer> getPlayingHandIds() {
        return playingHandIds;
    }

    /**
     * Returns the ids of the playing hand back.
     * @return The ids of the playing hand back.
     */
    public List<Integer> getPlayingHandIdsBack() {
        return playingHandIdsBack;
    }
}