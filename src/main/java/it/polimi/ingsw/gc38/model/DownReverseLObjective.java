package it.polimi.ingsw.gc38.model;

import java.util.List;
import java.util.Map;
public class DownReverseLObjective extends PositionObjective{
    private Color color1;
    private Color color2;

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor1() {
        return color1;
    }
    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public Color getColor2() {
        return color2;
    }

    //ridefinizione del metodo verify()
}
