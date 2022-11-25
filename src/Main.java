import javax.swing.*;

import grid.Background;
import grid.Scene;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame window = new JFrame("Main Frame");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        int canvasWidth = 800, canvasHeight = 600;
        int tileSize = 20;


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

        scene.drawLoop.start();
    }
}