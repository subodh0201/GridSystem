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

        Scene scene = new Scene(800, 600, 20, -800 / 40, 600 / 40, 0, 0);
        scene.addEntity(new Background());

        window.add(scene);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        scene.drawLoop.start();
    }
}