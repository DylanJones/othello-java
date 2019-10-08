
import javafx.scene.shape.Circle;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Disk {
    private Circle circle;
    private Point position;

    public Circle getCircle() {
        return circle;
    }

    public Point getPosition() {
        return position;
    }

    public Disk(Circle circle, Point position) {
        this.circle = circle;
        this.position = position;
    }
}
