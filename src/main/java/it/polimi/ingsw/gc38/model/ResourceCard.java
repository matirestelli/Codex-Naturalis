package it.polimi.ingsw.gc38.model;

public class ResourceCard extends Card implements ColoredCard {
    //private Color color;
    private int point;
    private String ANSI_COLOR;


    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getANSI_COLOR() {
        return ANSI_COLOR;
    }

    public void setANSI_COLOR(String ANSI_COLOR) {
        this.ANSI_COLOR = ANSI_COLOR;
    }

}

