package it.polimi.ingsw.gc38.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public abstract class Objective {
    private int id;
    private boolean frontSide;
    private String frontCover; // front side of the objective
    private String backCover; // back side of the objective
    private int points;
    private boolean isCompleted;
    private int completed; //numero di volte che l'obiettivo è stato completato
    private List<Integer> IDusati = new ArrayList<>();  //lista di carte già usate per completare un obiettivo

    public int getId() {
        return id;
    }

    public int setCompleted() { //incrementa il numero di volte che l'obiettivo è stato completato
        completed++;
        return completed;
    }

    protected List<Integer> getIDusati() {
        return IDusati;
    }

    protected void addIDusato(Integer id) {
        IDusati.add(id);
    }

    public int getCompleted() { //ritorna il numero di volte che l'obiettivo è stato completato
        return completed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFrontSide() {
        return frontSide;
    }

    //setSide è il metodo flip dell'uml
    public void setSide(boolean frontSide) {
        this.frontSide = frontSide;
    }

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

    public String getBackCover() {
        return backCover;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    // -- public void setCompleted(boolean isCompleted) {
    //    this.isCompleted = isCompleted;
    //}

    public void setBackCover(String backCover) {
        this.backCover = backCover;
    }
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    //metodo per verificare se l'obiettivo è completato, verrà poi implementato nelle classi figlie in modo diverso
    public void CalculatePoints(Player p){ // calcola e aggiunge punti a player passato

    }

}

