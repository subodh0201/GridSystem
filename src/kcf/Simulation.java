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

    // Robots in this simulation
    private final RobotInterface[] robotInterfaces;

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

    // paths[i] is a queue of Directions representing the path robot[i]
    // will take in the move phase
    private final Queue<Direction>[] paths;

    // position of robots and fixed points in global coordinate system
    private final Point[] R, F;

    // should the simulation go to error state on collision?
    private final boolean errorOnCollision;

    public Simulation(
            RobotInterface[] robotInterfaces,
            Point[] fixedPoints,
            int moveAfterFrames,
            boolean errorOnCollision
    ) {
        this.robotInterfaces = robotInterfaces;
        this.fixedPoints = fixedPoints;
        this.moveAfterFrames = moveAfterFrames;
        this.errorOnCollision = errorOnCollision;
        this.paths = (Queue<Direction>[]) new Queue[robotInterfaces.length];

        R = new Point[robotInterfaces.length];
        F = new Point[fixedPoints.length];

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
            case CYCLE: {
                round++;
                System.out.println("Round: " + round);
                state = SimulationStates.LOOK;
                System.out.println("Look");
                break;
            }
            case LOOK: {
                look();
                if (isComplete()) {
                    state = SimulationStates.DONE;
                    System.out.println("done");
                } else {
                    state = SimulationStates.COMPUTE;
                    System.out.println("compute");
                }
                break;
            }
            case COMPUTE: {
                compute();
                state = SimulationStates.MOVE;
                framesSinceLastMove = 0;
                System.out.println("move");
                break;
            }
            case MOVE: {
                if (framesSinceLastMove < moveAfterFrames) framesSinceLastMove++;
                else {
                    framesSinceLastMove = 0;
                    move();
                }
                break;
            }
            default:
                break;
        }
    }

    private void look() {
        for (int i = 0; i < robotInterfaces.length; i++) {
            R[i] = robotInterfaces[i].getPosition();
        }
        for (int i = 0; i < fixedPoints.length; i++) {
            F[i] = new Point(fixedPoints[i]);
        }
    }

    private void compute() {
        for (int i = 0; i < robotInterfaces.length; i++) {
            paths[i] = robotInterfaces[i].LCM(R, F);
        }
    }

    private void move() {
        boolean complete = true;
        for (int i = 0; i < robotInterfaces.length; i++) {
            if (!paths[i].isEmpty()) {
                robotInterfaces[i].move(paths[i].poll());
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
        for (RobotInterface robotInterface : robotInterfaces) {
            Point p = robotInterface.getPosition();
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

    @Override
    public void render(Graphics2D g2D, Camera c) {
        g2D.setColor(Color.GREEN);
        for (Point p : fixedPoints) {
            g2D.fillRect(c.gridToScreenX(p.x), c.gridToScreenY(p.y), c.tileSize(), c.tileSize());
        }
        g2D.setColor(Color.RED);
        for (RobotInterface robotInterface : robotInterfaces) {
            Point p = robotInterface.getPosition();
            g2D.fillOval(c.gridToScreenX(p.x), c.gridToScreenY(p.y), c.tileSize(), c.tileSize());
        }
    }
}
