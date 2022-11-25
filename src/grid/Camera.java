package grid;

/**
 * class Camera represents the visible part of the grid.
 * Each point on the grid is displayed as a tile on the canvas.
 * There are 3 coordinate systems in use:
 *  * (gridX, gridY): "grid coordinate", Actual grid points
 *      * Positive direction of x-axis: to the right
 *      * Positive direction of y-axis: upwards
 *  * (tileX, tileY): "tile coordinate", Visible tiles with
 *      the top-left visible tile have coordinates (0,0)
 *      * Positive direction of x-axis: to the right
 *      * Positive direction of y-axis: downwards
 *  * (screenX, screenY): "pixel coordinate" the coordinate
 *      of the top left point of the tile on the canvas.
 *      * Positive direction of x-axis: to the right
 *      * Positive direction of y-axis: downwards
 */
public class Camera {
    // minimum size of a tile in pixels
    public static final int MIN_TILE_SIZE = 4;

    // size of the display canvas in pixels
    public final int canvasWidth;
    public final int canvasHeight;

    // size of a tile in pixels
    private int tileSize = 16;

    // x and y grid coordinates of tile (0,0)
    private int topLeftX = 0;
    private int topLeftY = 0;

    // x and y offset of the (0,0) tile from the
    // top left corner of the screen in fraction
    // of the tile size normalized to be in (-1, 0]
    private double offsetX = 0;
    private double offsetY = 0;


    /**
     * @param canvasWidth width of the canvas in pixels
     * @param canvasHeight height of the canvas in pixels
     * @param tileSize size of a tile in pixels
     * @param topLeftX gridX coordinate of top-left point
     * @param topLeftY gridY coordinate of top-left point
     * @param offsetX x offset in pixels of the left-most tiles
     * @param offsetY y offset in pixels of the top-most tiles
     */
    public Camera(
            int canvasWidth, int canvasHeight,
            int tileSize,
            int topLeftX, int topLeftY,
            int offsetX, int offsetY
    ) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        setTileSize(tileSize);
        addOffset(offsetX, offsetY);
        normalizeOffset();
    }

    public int tileSize() { return tileSize; }

    public int topLeftX() { return topLeftX; }

    public int topLeftY() { return topLeftY; }

    // returns the x offset on pixels
    public int offsetX() { return (int)(offsetX * tileSize); }

    // returns the y offset in pixels
    public int offsetY() { return (int)(offsetY * tileSize); }


    // returns the number of tiles visible along the width of the canvas
    public int tileCountX() { return canvasWidth / tileSize + 2; }

    // returns the number of tiles visible along the height of the canvas
    public int tileCountY() { return canvasHeight / tileSize + 2; }


    public boolean isVisibleX(int gridX) {
        int tileX = gridToTileX(gridX);
        return tileX >= 0 && tileX < tileCountX();
    }

    public boolean isVisibleY(int gridY) {
        int tileY = gridToTileY(gridY);
        return tileY >= 0 && tileY < tileCountY();
    }

    private void setTopLeftToTileX(int tileX) {
        topLeftX += tileX;
    }

    private void setTopLeftToTileY(int tileY) {
        topLeftY -= tileY;
    }


    // normalizes offset to be in (-1, 0]
    private void normalizeOffset() {
        if (offsetX > 0 || offsetX <= -1) {
            int w = (int) offsetX;
            double f = offsetX % 1;
            if (f > 0) {
                w += 1;
                f -= 1;
            }
            setTopLeftToTileX(-w);
            offsetX = f;
        }
        if (offsetY > 0 || offsetY <= -1) {
            int w = (int) offsetY;
            double f = offsetY % 1;
            if (f > 0) {
                w += 1;
                f -= 1;
            }
            setTopLeftToTileY(-w);
            offsetY = f;
        }
    }

    public void addOffset(double offsetX, double offsetY) {
        this.offsetX += offsetX;
        this.offsetY += offsetY;
        normalizeOffset();
    }

    // adds offset in pixels (can be used to pan the camera)
    public void addOffset(int offsetX, int offsetY) {
        addOffset((double)offsetX / tileSize, (double)offsetY / tileSize);
    }


    // sets the tile size in pixels
    public void setTileSize(int pixels) {
        pixels = Math.max(MIN_TILE_SIZE, pixels);
        tileSize = pixels;
    }


    public void zoom(int pixels) {
        double oldWidthX = (double)canvasWidth / tileSize;
        double oldWidthY = (double)canvasHeight / tileSize;
        setTileSize(tileSize + pixels);
        double diffX = (double)canvasWidth / tileSize - oldWidthX;
        double diffY = (double)canvasHeight / tileSize - oldWidthY;
        addOffset(diffX / 2, diffY / 2);

    }


    // ***** Conversion Functions *****

    public int tileToScreenX(int tileX) {
        return tileX * tileSize + offsetX();
    }

    public int tileToScreenY(int tileY) {
        return tileY * tileSize + offsetY();
    }

    public int screenToTileX(int screenX) {
        return (screenX - offsetX()) / tileSize;
    }

    public int screenToTileY(int screenY) {
        return (screenY - offsetY()) / tileSize;
    }


    private int gridToTileX(int gridX) {
        return gridX - topLeftX;
    }

    private int gridToTileY(int gridY) {
        return topLeftY - gridY;
    }

    public int tileToGridX(int tileX) {
        return topLeftX + tileX;
    }

    public int tileToGridY(int tileY) {
        return topLeftY - tileY;
    }

    public int gridToScreenX(int gridX) {
        return tileToScreenX(gridToTileX(gridX));
    }

    public int gridToScreenY(int gridY) {
        return tileToScreenY(gridToTileY(gridY));
    }

    public int screenToGridX(int screenX) {
        return tileToGridX(screenToTileX(screenX));
    }

    public int screenToGridY(int screenY) {
        return tileToGridY(screenToTileY(screenY));
    }
}
