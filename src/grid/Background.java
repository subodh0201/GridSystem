package grid;

import java.awt.*;

/**
 * Background of grid.Scene
 */
public class Background extends Entity {
    public final Color oddTiles;
    public final Color evenTiles;
    public final Color axisBorders;

    public Background() {
        this(Color.WHITE, new Color(245, 245, 245), Color.BLUE);
    }

    public Background(Color oddTiles, Color evenTiles, Color axisBorders) {
        this.oddTiles = oddTiles;
        this.evenTiles = evenTiles;
        this.axisBorders = axisBorders;
    }

    @Override
    public void update() {
        // nothing to update
    }

    @Override
    public void render(Graphics2D g2D, Camera c) {
        for (int i = 0; i <= c.tileCountX(); i++) {
            for (int j = 0; j <= c.tileCountY(); j++) {
                boolean even = (i + c.topLeftX() + j + c.topLeftY()) % 2 == 0;
                g2D.setColor(even ? evenTiles : oddTiles);
                g2D.fillRect(c.screenX(c.topLeftX() + i), c.screenY(c.topLeftY() + j), c.tileSize(), c.tileSize());
            }
        }
        g2D.setColor(axisBorders);
        // y-axis
        if (c.isVisibleX(0)) {
            for (int j = 0; j <= c.tileCountY(); j++) {
                g2D.drawRect(c.screenX(0), c.screenY(c.topLeftY() + j), c.tileSize(), c.tileSize());
            }
        }
        // x-axis
        if (c.isVisibleY(0)) {
            for (int i = 0; i <= c.tileCountX(); i++) {
                g2D.drawRect(c.screenX(c.topLeftX() + i), c.screenY(0), c.tileSize(), c.tileSize());
            }
        }
    }
}