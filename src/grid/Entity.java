package grid;

import java.awt.*;

/**
 * interface Entity represents an entity in the
 * system that can be updated and rendered
 */
public interface Entity {
    void update();
    void render(Graphics2D g2D, Camera c);
}
