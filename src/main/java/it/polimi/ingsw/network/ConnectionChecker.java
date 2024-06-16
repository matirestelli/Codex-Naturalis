package it.polimi.ingsw.network;

import it.polimi.ingsw.core.model.GameState;
import it.polimi.ingsw.core.model.PlayerState;

public class ConnectionChecker extends Thread {
    private GameState game;
    private int interval;
    private long timeout;

    public ConnectionChecker(GameState game, int interval, long timeout) {
        this.game = game;
        this.interval = interval; // intervallo in millisecondi
        this.timeout = timeout; // timeout in millisecondi
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            game.checkPlayersConnection(timeout);

            if (!game.allPlayersConnected()) {
                System.out.println("Not all players are connected!");
                for (PlayerState player : game.getPlayerStates().values()) {
                    if (!player.isConnected()) {
                        System.out.println("Player " + player.getUsername() + " is not connected.");
                    }
                }
            } else {
                System.out.println("All players are connected.");
            }
        }
    }
}

