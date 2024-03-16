package it.polimi.ingsw.gc38;

import it.polimi.ingsw.gc38.controller.GameController;
import it.polimi.ingsw.gc38.model.Player;
import it.polimi.ingsw.gc38.view.CliView;

public class App {

    public static void main(String[] args) {
        Player player = new Player();
        CliView view = new CliView();
        GameController controller = new GameController(player, view);

        controller.startGame();
    }

}