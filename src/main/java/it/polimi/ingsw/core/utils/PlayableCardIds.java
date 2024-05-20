package it.polimi.ingsw.core.utils;

import java.util.List;

public class PlayableCardIds implements java.io.Serializable {
    private List<Integer> playingHandIds;
    private List<Integer> playingHandIdsBack;

    public PlayableCardIds(List<Integer> playingHandIds, List<Integer> playingHandIdsBack) {
        this.playingHandIds = playingHandIds;
        this.playingHandIdsBack = playingHandIdsBack;
    }

    public List<Integer> getPlayingHandIds() {
        return playingHandIds;
    }

    public List<Integer> getPlayingHandIdsBack() {
        return playingHandIdsBack;
    }
}
