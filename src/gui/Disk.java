package gui;

import javafx.scene.shape.Circle;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Disk {
    private Circle circle;
    private int index;

    public Circle getCircle() {
        return circle;
    }

    public int getIndex() {
        return index;
    }

    public Disk(Circle circle, int index) {
        this.circle = circle;
        this.index = index;
    }
}
