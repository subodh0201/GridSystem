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

    public void setPosition(Point position) {
        this.position = new Point(position);
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
    public abstract Queue<Point> LCM(Point[] R, Point[] F);
}
