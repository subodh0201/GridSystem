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

        Scene scene = new Scene(600, 600, 16, -600 / 32, -600 / 32, +4, +4);
        scene.addEntity(new Background());

        window.add(scene);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        scene.drawLoop.start();
    }
}