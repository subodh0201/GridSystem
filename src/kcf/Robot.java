package kcf;

import java.awt.*;
import java.util.Queue;

/**
 * abstract class Robot represents a robot on the grid.
 */
public abstract class Robot {
    protected Point position;

    public Robot(Point position) {
        this.position = new Point(position);
    }

    public Point getPosition() {
        return new Point(position);
    }

    public void move(Direction d) {
        switch (d) {
            case UP: position.y += 1; break;
            case DOWN: position.y -= 1; break;
            case RIGHT: position.x += 1; break;
            case LEFT: position.x -= 1; break;
        }
    }

    /**
     * Look-Compute-Move cycle. The parameters R and F
     * should be provided in the robots local coordinate
     * system and it returns a path leading to the destination
     * in its local coordinate system
     * @param R location of robots in local coordinate system
     * @param F location of fixed points in local coordinate system
     * @return a path to destination in local coordinate system
     */
    public abstract Queue<Direction> LCM(Point[] R, Point[] F);
}
