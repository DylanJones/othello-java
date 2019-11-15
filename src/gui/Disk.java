package gui;

import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a disk on the othello game board
 * Includes a circle object for drawing and index for position
 */
public class Disk {
    private Circle circle;
    private int index;

    /**
     * @param circle used for drawing
     * @param index used for positioning
     */
    public Disk(Circle circle, int index) {
        this.circle = circle;
        this.index = index;
    }

    /**
     * @return the circle
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * @return the position
     */
    public int getIndex() {
        return index;
    }
}
