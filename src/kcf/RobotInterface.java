package kcf;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class RobotInterface {
    private final Robot r;
    private final boolean xAxisAlignment;

    public RobotInterface(Robot r, boolean xAxisAlignment) {
        this.r = r;
        this.xAxisAlignment = xAxisAlignment;
    }

    public Point getPosition() {
        return inverse(r.getPosition());
    }

    public void move(Direction d) {
        r.move(transform(d));
    }

    public Queue<Direction> LCM(Point[] R, Point[] F) {
        Point[] _R = new Point[R.length];
        Point[] _F = new Point[F.length];

        for (int i = 0; i < R.length; i++) {
            _R[i] = transform(R[i]);
        }
        for (int i = 0; i < F.length; i++) {
            _F[i] = transform(F[i]);
        }

        Queue<Direction> path = r.LCM(_R, _F);
        Queue<Direction> _path = new LinkedList<>();
        while (!path.isEmpty()) {
            _path.offer(inverse(path.poll()));
        }
        return _path;
    }

    private Point transform(Point p) {
        return new Point(p.x * ((xAxisAlignment) ? +1 : -1), p.y);
    }

    private Point inverse(Point p) {
        return transform(p);
    }

    private Direction transform(Direction d) {
        if (!xAxisAlignment) {
            if (d.equals(Direction.LEFT)) d = Direction.RIGHT;
            if (d.equals(Direction.RIGHT)) d = Direction.LEFT;
        }
        return d;
    }

    private Direction inverse(Direction d) {
        return transform(d);
    }
}
