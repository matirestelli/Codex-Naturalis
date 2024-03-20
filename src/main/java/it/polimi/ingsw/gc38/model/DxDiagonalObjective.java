package it.polimi.ingsw.gc38.model;

//import java.util.List;
//import java.util.Map;

public class DxDiagonalObjective extends PositionObjective{
    private Color color;

    //List<Integer> IDusati = new ArrayList<>();

    //private Integer completati;

    //private Integer Points

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    //ridefinizione del metodo complete()

    //patter che l'algoritmo ricorsivo controllerà per ogni carta del colore color e che non sia contenuta nella lista di carte già usate per completare un obiettivo

    /*
    // se è in posizione 1:
    if (matrix[x][y].getCard().getColor() == color && !IDusati.contains(matrix[x][y].getCard().getId())) {
        if (matrix[x+1][y+1].getCard().getColor() == color && !IDusati.contains(matrix[x+1][y+1].getCard().getId())){
           if (matrix[x+2][y+2].getCard().getColor() == color && !IDusati.contains(matrix[x+2][y+2].getCard().getId())){
                IDusati.add(matrix[x][y].getCard().getId());
                IDusati.add(matrix[x+1][y+1].getCard().getId());
                IDusati.add(matrix[x+2][y+2].getCard().getId());
                completati++;
           }
        }
     }
     // se è in posizione 2:
    if (matrix[x][y].getCard().getColor() == color && !IDusati.contains(matrix[x][y].getCard().getId())){
        if (matrix[x+1][y-1].getCard().getColor() == color && !IDusati.contains(matrix[x+1][y-1].getCard().getId())) {
           if (matrix[x-1][y+1].getCard().getColor() == color && !IDusati.contains(matrix[x-1][y+1].getCard().getId())) {
                IDusati.add(matrix[x][y].getCard().getId());
                IDusati.add(matrix[x+1][y-1].getCard().getId());
                IDusati.add(matrix[x-1][y+1].getCard().getId());
                completati++;
           }
        }
     }
     // se è in posizione 3:
    if (matrix[x][y].getCard().getColor() == color && !IDusati.contains(matrix[x][y].getCard().getId())){
        if (matrix[x-1][y-1].getCard().getColor() == color && !IDusati.contains(matrix[x-1][y-1].getCard().getId())) {
           if (matrix[x-2][y-2].getCard().getColor() == color && !IDusati.contains(matrix[x-2][y-2].getCard().getId())) {
                IDusati.add(matrix[x][y].getCard().getId());
                IDusati.add(matrix[x-1][y-1].getCard().getId());
                IDusati.add(matrix[x-2][y-2].getCard().getId());
                completati++;
           }
        }
     }
     Points= completati * 3;
     return Points;
     */

}