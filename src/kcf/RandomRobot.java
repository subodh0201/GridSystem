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
    public Queue<Direction> LCM(Point[] R, Point[] F) {
        int dx = RandomUtil.uniform(-10, 10);
        int dy = RandomUtil.uniform(-10, 10);
        return path(dx ,dy);
    }

    private Queue<Direction> path(int dx, int dy) {
        Queue<Direction> path = new LinkedList<>();
        Direction d = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        for (int i = 0; i < Math.abs(dx); i++) {
            path.add(d);
        }
        d = (dy > 0) ? Direction.UP : Direction.DOWN;
        for (int i = 0; i < Math.abs(dy); i++) {
            path.add(d);
        }
        return path;
    }
}
