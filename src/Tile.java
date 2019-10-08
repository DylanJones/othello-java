import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.Point;

public class Tile {
    private Rectangle rectangle;
    private Circle circle;

    public Point getPosition() {
        return position;
    }

    private Point position;

    public Tile(Rectangle rectangle, Point position) {
        this.rectangle = rectangle;
        this.position = position;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
