package gui;

import javafx.scene.shape.Circle;

/**
 * Represents a disk on the othello game board
 * Includes a circle object for drawing and index for position
 */
public class Disk {
    private Circle circle;
    private int index;
    private boolean flipping;

    /**
     * @param circle used for drawing
     * @param index used for positioning
     */
    public Disk(Circle circle, int index) {
        this.circle = circle;
        this.index = index;
        flipping = false;
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

    public void setFlipping(boolean flipping) {
        this.flipping = flipping;
    }

    public boolean isFlipping() {
        return flipping;
    }
}
