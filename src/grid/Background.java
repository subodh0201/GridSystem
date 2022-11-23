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
        for (int tileY = 0; tileY < c.tileCountY(); tileY++) {
            for (int tileX = 0; tileX < c.tileCountX(); tileX++) {
                boolean even = (c.tileToGridX(tileX) + c.tileToGridY(tileY)) % 2 == 0;
                g2D.setColor(even ? evenTiles : oddTiles);
                g2D.fillRect(c.tileToScreenX(tileX), c.tileToScreenY(tileY), c.tileSize(), c.tileSize());
            }
        }

        g2D.setColor(axisBorders);
        // y-axis
        if (c.isVisibleX(0)) {
            for (int tileY = 0; tileY < c.tileCountY(); tileY++) {
                g2D.drawRect(c.gridToScreenX(0), c.tileToScreenY(tileY), c.tileSize(), c.tileSize());
                util.GraphicsUtil.drawCenteredString(
                        g2D,
                        c.tileToGridY(tileY) + "",
                        new Rectangle(c.gridToScreenX(0), c.tileToScreenY(tileY), c.tileSize(), c.tileSize()),
                        new Font(Font.SERIF, Font.PLAIN, c.tileSize() / 2)
                );
            }
        }
        // x-axis
        if (c.isVisibleY(0)) {
            for (int tileX = 0; tileX < c.tileCountX(); tileX++) {
                g2D.drawRect(c.tileToScreenX(tileX), c.gridToScreenY(0), c.tileSize(), c.tileSize());
                util.GraphicsUtil.drawCenteredString(
                        g2D,
                        c.tileToGridX(tileX) + "",
                        new Rectangle(c.tileToScreenX(tileX), c.gridToScreenY(0), c.tileSize(), c.tileSize()),
                        new Font(Font.SERIF, Font.PLAIN, c.tileSize() / 2)
                );
            }
        }
    }
}