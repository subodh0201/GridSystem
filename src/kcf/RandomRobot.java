package kcf;

import util.RandomUtil;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * class RandomRobot represents a robot that
 * selects a random point as destination
 */
public class RandomRobot extends Robot {

    public RandomRobot(Point position) {
        super(position);
    }

    @Override
    public Queue<Point> LCM(Point[] R, Point[] F) {
        int dx = RandomUtil.uniform(-10, 10);
        int dy = RandomUtil.uniform(-10, 10);
        return path(dx ,dy);
    }

    private Queue<Point> path(int dx, int dy) {
        Queue<Point> path = new LinkedList<>();
        Point cur = new Point(position);
        while (dx != 0 || dy != 0) {
            cur.x += direction(dx);
            cur.y += direction(dy);
            dx -= direction(dx);
            dy -= direction(dy);
            path.offer(new Point(cur));
        }
        return path;
    }

    private int direction(int d) {
        if (d == 0) return 0;
        return d < 0 ? -1 : 1;
    }
}
