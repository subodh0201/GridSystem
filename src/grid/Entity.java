package grid;

import java.awt.*;

/**
 * abstract class Entity represents an entity in the
 * system that can be updated and rendered
 */
public abstract class Entity {
    public abstract void update();
    public abstract void render(Graphics2D g2D, Camera c);
}
