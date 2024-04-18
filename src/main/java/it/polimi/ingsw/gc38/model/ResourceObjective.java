package it.polimi.ingsw.gc38.model;

import it.polimi.ingsw.gc38.view.CliView;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ResourceObjective extends Objective {
    List<Resource> resources;

    public int calculatePoints(Player p) {
        Map<Resource, Integer> playerResources = p.calculateResources();
        List<Integer> cardinality = new ArrayList<>();

        List<Requirement> requirements = new ArrayList<>();

        for (Resource res : resources) {
            // check if in requirements there is already a requirement for the same resource
            // if so, increment its cardinality
            // otherwise, add a new requirement
            if (requirements.stream().anyMatch(r -> r.getResource() == res))
                requirements.stream().filter(r -> r.getResource() == res).forEach(Requirement::incrementQta);
            else
                requirements.add(new Requirement(res, 1));
        }

        for (Requirement req : requirements) {
            // if player has the required resource, add its cardinality to the list
            // otherwise, return 0 points
            if (playerResources.get(req.getResource()) == 0)
                return 0;
            cardinality.add(playerResources.get(req.getResource()) / req.getQta());
        }

        // to calculate point, use minimum cardinality of resources
        return cardinality.stream().min(Integer::compare).get() * getPoints();
    }

    public void displayCard(CliView view) {
        final String ANSI_RED_BACKGROUND = "\u001B[41m";
        final String ANSI_COLOR_RESET = "\u001B[0m";
        final String ANSI_BOLD = "\u001B[1m";
        final String ANSI_COLOR_GOLD = "\u001B[33m";
        final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
        final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        final String ANSI_YELLOW_BACKGROUND = "\u001B[48;5;220m";

        String ANSIColor = "";

        if (getColor() == Color.RED) {
            ANSIColor = ANSI_RED_BACKGROUND;
        } else if (getColor() == Color.BLUE) {
            ANSIColor = ANSI_BLUE_BACKGROUND;
        } else if (getColor() == Color.PURPLE) {
            ANSIColor = ANSI_PURPLE_BACKGROUND;
        } else if (getColor() == Color.GREEN) {
            ANSIColor = ANSI_GREEN_BACKGROUND;
        } else
            ANSIColor = ANSI_YELLOW_BACKGROUND;

        view.displayMessage(ANSIColor + "  " + ANSI_BOLD + this.getPoints() + ANSIColor + "    " + ANSI_COLOR_RESET);
        if (resources.size() == 3) {
            view.displayMessage(ANSIColor + "    " + resources.get(0).toString().charAt(0) + "  " + ANSI_COLOR_RESET);
            view.displayMessage(ANSIColor + "   " + resources.get(1).toString().charAt(0) + " " + resources.get(2).toString().charAt(0) + " " + ANSI_COLOR_RESET);
        } else {
            view.displayMessage(ANSIColor + "   " + resources.get(0).toString().charAt(0) + " " + resources.get(1).toString().charAt(0) + " " + ANSI_COLOR_RESET);
            view.displayMessage(ANSIColor + "       " + ANSI_COLOR_RESET);

        }
    }
}

