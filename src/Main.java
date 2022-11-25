import javax.swing.*;

import grid.Background;
import grid.Scene;
import kcf.RandomRobot;
import kcf.Simulation;
import util.RandomUtil;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame window = new JFrame("Main Frame");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        int canvasWidth = 800, canvasHeight = 600;
        int tileSize = 8;


        Scene scene = new Scene(
                canvasWidth, canvasHeight,
                tileSize,
                - canvasWidth / (2 * tileSize) + 2,  canvasHeight / (2 * tileSize) - 2,
                2 * tileSize, 2 * tileSize
        );

        scene.addEntity(new Background());

        window.add(scene);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // creating robots
        RandomRobot[] robots = new RandomRobot[50];
        for (int i = 0; i < robots.length; i++)
            robots[i] = new RandomRobot(RandomUtil.uniformPoint(-20, 20));

        // random direction of x axis for each robot
        boolean[] xAxisDirectionAligned = new boolean[robots.length];
        for (int i = 0; i < xAxisDirectionAligned.length; i++)
            xAxisDirectionAligned[i] = Math.random() < 0.5;

        // creating fixed points
        Point[] fixedPoints = new Point[10];
        for (int i = 0; i < fixedPoints.length; i++)
            fixedPoints[i] = RandomUtil.uniformPoint(-20, 20);

        // creating the simulation
        Simulation s = new Simulation(robots, xAxisDirectionAligned, fixedPoints, 60, false);
        scene.addEntity(s);

        scene.drawLoop.start();

        scene.eventQueue.enqueue(s::start);
    }
}