package kcf;

import grid.Camera;
import grid.Entity;

import java.awt.*;
import java.util.HashSet;
import java.util.Queue;

/**
 * class Simulation represents a synchronous simulation.
 * The robots execute a look-compute-move cycle synchronously.
 */
public class Simulation implements Entity {
    // Robots in the simulation
    private final Robot[] robots;

    // if xAxisDirectionAligned[i] is true the x-axis direction of
    // robot[i] is aligned with the global coordinate system else
    // it is the opposite direction.
    private final boolean[] xAxisDirectionAligned;

    // fixed points in the simulation
    private final Point[] fixedPoints;

    // number of rounds executed
    private int round = 0;

    // state of the simulation
    private SimulationStates state;

    // during move phase moving to next point on path on every frame is
    // too fast so we move only after every 'moveAfterFrames' frames
    private final int moveAfterFrames;

    // number of frames passed since last move
    private int framesSinceLastMove = 0;

    // paths[i] is a queue of points representing the path robot[i]
    // will take in the move phase
    private final Queue<Point>[] paths;

    // position of robots and fixed points in global coordinate system
    // and with the x-axis directions reversed.
    private final Point[] R, R_, F, F_;

    // should the simulation go to error state on collision?
    private final boolean errorOnCollision;

    public Simulation(
            Robot[] robots,
            boolean[] xAxisDirectionAligned,
            Point[] fixedPoints,
            int moveAfterFrames,
            boolean errorOnCollision
    ) {
        this.robots = robots;
        this.xAxisDirectionAligned = xAxisDirectionAligned;
        this.fixedPoints = fixedPoints;
        this.moveAfterFrames = moveAfterFrames;
        this.errorOnCollision = errorOnCollision;
        this.paths = (Queue<Point>[]) new Queue[robots.length];

        R = new Point[robots.length];
        R_ = new Point[robots.length];
        F = new Point[fixedPoints.length];
        F_ = new Point[fixedPoints.length];

        this.state = SimulationStates.READY;

        if (collision() && errorOnCollision)
            state = SimulationStates.ERROR;
    }

    // start the simulation
    public synchronized void start() {
        if (state == SimulationStates.READY)
            state = SimulationStates.CYCLE;
    }

    @Override
    public void update() {
        switch (state) {
            case READY, DONE, ERROR, PAUSED -> {}
            case CYCLE -> {
                round++;
                System.out.println("Round: " + round);
                state = SimulationStates.LOOK;
                System.out.println("Look");
            }
            case LOOK -> {
                look();
                if (isComplete()) {
                    state = SimulationStates.DONE;
                    System.out.println("done");
                } else {
                    state = SimulationStates.COMPUTE;
                    System.out.println("compute");
                }

            }
            case COMPUTE -> {
                compute();
                state = SimulationStates.MOVE;
                framesSinceLastMove = 0;
                System.out.println("move");
            }
            case MOVE -> {
                if (framesSinceLastMove < moveAfterFrames) framesSinceLastMove++;
                else {
                    framesSinceLastMove = 0;
                    move();
                }
            }
        }
    }

    private void look() {
        for (int i = 0; i < robots.length; i++) {
            R[i] = new Point(robots[i].getPosition());
            R_[i] = new Point(robots[i].getPosition());
            R_[i].x = -R_[i].x;
        }
        for (int i = 0; i < fixedPoints.length; i++) {
            F[i] = new Point(fixedPoints[i]);
            F_[i] = new Point(fixedPoints[i]);
            F_[i].x = -F_[i].x;
        }
    }

    private void compute() {
        for (int i = 0; i < robots.length; i++) {
            if (xAxisDirectionAligned[i]) {
                paths[i] = robots[i].LCM(R, F);
            } else {
                paths[i] = robots[i].LCM(R_, F_);
            }
        }
    }

    private void move() {
        boolean complete = true;
        for (int i = 0; i < robots.length; i++) {
            if (!paths[i].isEmpty()) {
                robots[i].setPosition(paths[i].poll());
                complete = false;
            }
        }
        if (collision() && errorOnCollision)
            state = SimulationStates.ERROR;
        else if (complete)
            state = SimulationStates.CYCLE;
    }

    private boolean collision() {
        boolean collision = false;
        HashSet<Point> points = new HashSet<>();
        StringBuilder sb = new StringBuilder("Collision at:");
        for (int i = 0; i < robots.length; i++) {
            Point p = globalCoordinate(i);
            if (!points.add(p)) {
                sb.append(" (").append(p.x).append(", ").append(p.y).append(')');
                collision = true;
            }
        }
        if (collision)
            System.out.println(sb);
        return collision;
    }

    public boolean isComplete() {
        return false;
    }

    private Point globalCoordinate(int i) {
        Point p = new Point(robots[i].getPosition());
        if (!xAxisDirectionAligned[i]) p.x = -p.x;
        return p;
    }

    @Override
    public void render(Graphics2D g2D, Camera c) {
        g2D.setColor(Color.GREEN);
        for (Point p : fixedPoints) {
            g2D.fillRect(c.gridToScreenX(p.x), c.gridToScreenY(p.y), c.tileSize(), c.tileSize());
        }
        g2D.setColor(Color.RED);
        for (int i = 0; i < robots.length; i++) {
            Point p = globalCoordinate(i);
            g2D.fillOval(c.gridToScreenX(p.x), c.gridToScreenY(p.y), c.tileSize(), c.tileSize());
        }
    }
}
