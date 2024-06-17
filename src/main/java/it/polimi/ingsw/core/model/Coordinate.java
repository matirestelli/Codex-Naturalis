package it.polimi.ingsw.core.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a coordinate in the game.
 * It implements Serializable interface.
 * It maintains the x and y coordinates.
 */
public class Coordinate implements Serializable {
    private int x;
    private int y;

    /**
     * Constructs a Coordinate with the given x and y coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate.
     * @return The x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate.
     * @param x The x coordinate to set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the y coordinate.
     * @return The y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate.
     * @param y The y coordinate to set.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Checks if the given object is equal to this coordinate.
     * @param obj The object to compare with.
     * @return True if the given object is equal to this coordinate, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinate that = (Coordinate) obj;
        return x == that.x && y == that.y;
    }

    /**
     * Returns the hash code of this coordinate.
     * @return The hash code of this coordinate.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}