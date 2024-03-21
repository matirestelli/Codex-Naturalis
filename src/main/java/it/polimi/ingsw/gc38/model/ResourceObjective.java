package it.polimi.ingsw.gc38.model;

import java.util.Map;
public class ResourceObjective extends Objective{
    private int numResources;
    private Resource resource;
    private Map<Resource, Integer> playerResources;

    public void setNumResources(int numResources) {
        this.numResources = numResources;
    }
    public int getNumResources() {
        return numResources;
    }
    public Resource getResource() {
        return resource;
    }
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    //ridefinizione del metodo CalculatePoints(Player p), fare un algo che calcoli i punti
    public void CalculatePoints(Player p) {
        playerResources = p.getPersonalResources();
        p.addScore((playerResources.get(resource) / numResources) * getPoints());
    }
}

