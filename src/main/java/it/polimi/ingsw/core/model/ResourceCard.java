package it.polimi.ingsw.core.model;

import java.io.Serializable;

public class ResourceCard extends Card implements Serializable {
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