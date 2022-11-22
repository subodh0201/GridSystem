package grid;

/**
 * class Camera represents a visible window of the grid.
 * It keeps track of where the visible tiles should be drawn
 * on the canvas and the size of tiles (determines the zoom)
 * TODO:
 *  * add a method zoom() which changes tileSize and
 *    updates topLeft coordinates and offsets so that
 *    zoom in and out is with respect to the center
 */
public class Camera {
    // minimum size of a tile in pixels
    public static final int MIN_TILE_SIZE = 4;

    // size of the display canvas in pixels
    public final int canvasWidth;
    public final int canvasHeight;

    // size of a tile in pixels
    private int tileSize = 16;

    // x and y grid coordinates of top left tile
    // which is within the camera boundaries
    private int topLeftX = 0;
    private int topLeftY = 0;

    // x and y offset of the top left tile from the
    // top left corner of the screen in pixels.
    // offset is normalized to be in (-tileSize, 0]
    private int offsetX = 0;
    private int offsetY = 0;


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

    public int offsetX() { return offsetX; }

    public int offsetY() { return offsetY; }

    // returns the number of tiles visible along the width of the canvas
    public int tileCountX() { return canvasWidth / tileSize + 1; }

    // returns the number of tiles visible along the height of the canvas
    public int tileCountY() { return canvasHeight / tileSize + 1; }


    // normalizes offset to be in (-tileSize, 0]
    private void normalizeOffset() {
        if (offsetX > 0) {
            topLeftX -= offsetX / tileSize + 1;
            offsetX = offsetX % tileSize - tileSize;
        }
        if (offsetY > 0) {
            topLeftY -= offsetY / tileSize + 1;
            offsetY = offsetY % tileSize - tileSize;
        }
        if (offsetX <= -tileSize) {
            topLeftX += (-offsetX) / tileSize;
            offsetX = -(-offsetX % tileSize);
        }
        if (offsetY <= -tileSize) {
            topLeftY += (-offsetY) / tileSize;
            offsetY = -(-offsetY % tileSize);
        }
    }


    // adds offset in pixels (can be used to pan the camera)
    public void addOffset(int offsetX, int offsetY) {
        this.offsetX += offsetX;
        this.offsetY += offsetY;
        normalizeOffset();
    }


    // sets the tile size in pixels
    public void setTileSize(int pixels) {
        pixels = Math.max(MIN_TILE_SIZE, pixels);
        offsetX = offsetX * pixels / tileSize;
        offsetY = offsetY * pixels / tileSize;
        tileSize = pixels;
    }


    // returns true if the x grid coordinate is visible
    public boolean isVisibleX(int gridX) {
        return (gridX - topLeftX >= 0 && gridX - topLeftX <= tileCountX());
    }

    // returns true if the y grid coordinate is visible
    public boolean isVisibleY(int gridY) {
        return (gridY - topLeftY >= 0 && gridY - topLeftY <= tileCountY());
    }

    // returns true if the grid point is visible
    public boolean isVisible(int gridX, int gridY) {
        return isVisibleX(gridX) && isVisibleY(gridY);
    }


    // returns the x coordinate of the top left pixel
    // of tiles which have gridX as x grid coordinate
    public int screenX(int gridX) {
        return (gridX - topLeftX) * tileSize + offsetX;
    }

    // returns the y coordinate of the top left pixel
    // of tiles which have gridY as y grid coordinate
    public int screenY(int gridY) {
        return (gridY - topLeftY) * tileSize + offsetY;
    }
}
