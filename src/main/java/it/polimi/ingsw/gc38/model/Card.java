package it.polimi.ingsw.gc38.model;

import java.util.List;
import java.util.Map;

public class Card {
    private int id;
    private boolean frontSide;
    private String frontCover;
    private String backCover;
    private Map<Integer, Corner> frontCorners;
    private Map<Integer, Corner> backCorners;
    private List<Resource> backResources;
    private Coordinate centre;
    private int xMatrixCord;
    private int yMatrixCord;

    public Map<Integer, Corner> getActualCorners() {
        if (frontSide) {
            return frontCorners;
        } else {
            return backCorners;
        }
    }

    public Map<Integer, Corner> getBackCorners() {
        return backCorners;
    }

    public void setXYCord(int yMatrixCord, int xMatrixCord) {
        this.xMatrixCord = xMatrixCord;
        this.yMatrixCord = yMatrixCord;
    }

    public int getxMatrixCord() {
        return xMatrixCord;
    }

    public void setxMatrixCord(int xMatrixCord) {
        this.xMatrixCord = xMatrixCord;
    }

    public int getyMatrixCord() {
        return yMatrixCord;
    }

    public void setyMatrixCord(int yMatrixCord) {
        this.yMatrixCord = yMatrixCord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFrontSide() {
        return frontSide;
    }

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

    public void setBackCover(String backCover) {
        this.backCover = backCover;
    }

    public Map<Integer, Corner> getFrontCorners() {
        return frontCorners;
    }

    public void setFrontCorners(Map<Integer, Corner> frontCorners) {
        this.frontCorners = frontCorners;
    }

    public List<Resource> getBackResources() {
        return backResources;
    }

    public void setBackResources(List<Resource> backResources) {
        this.backResources = backResources;
    }

    public Coordinate getCentre() {
        return centre;
    }

    public void setCentre(Coordinate centre) {
        this.centre = centre;
    }

}
