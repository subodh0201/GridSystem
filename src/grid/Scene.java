package grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Scene extends JPanel {
    // size of the display canvas in pixels
    private final int canvasWidth;
    private final int canvasHeight;

    public final DrawLoop drawLoop;
    public final Camera camera;
    public final EventQueue eventQueue = new EventQueue();
    public final List<Entity> entities = Collections.synchronizedList(new ArrayList<>());


    public Scene(
            int canvasWidth, int canvasHeight,
            int tileSize,
            int topLeftX, int topLeftY,
            int offSetX, int offsetY
    ) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.camera = new Camera(canvasWidth, canvasHeight, tileSize, topLeftX, topLeftY, offSetX, offsetY);

        drawLoop = new DrawLoop() {
            @Override
            protected void update() { Scene.this.update(); }

            @Override
            protected void render() { Scene.this.repaint(); }
        };

        this.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        // Event listeners
        this.addMouseMotionListener(new ZoomPanHandler());
        this.addMouseWheelListener(new ZoomPanHandler());
    }

    // add an entity to the scene
    public void addEntity(Entity e) {
        entities.add(e);
    }

    // add multiple entities to the scene
    public void addAllEntity(Collection<Entity> entities) {
        this.entities.addAll(entities);
    }

    // the update phase of draw loop
    private void update() {
        // consume all pending events
        int sz = eventQueue.size();
        while (sz-- > 0)
            eventQueue.dequeue().consume();

        // update all entities
        for (Entity e : entities)
            e.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        for (Entity e : entities)
            e.render(g2D, camera);
        g2D.dispose();
    }


    // Allows zooming (scroll) and panning (drag) using the mouse
    private class ZoomPanHandler implements MouseMotionListener, MouseWheelListener {
        private Point cursor;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (cursor != null) {
                int diffX = e.getPoint().x - cursor.x, diffY = e.getPoint().y - cursor.y;
                eventQueue.enqueue(() -> camera.addOffset(diffX, diffY));
                cursor = e.getPoint();
            }
            cursor = e.getPoint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            cursor = null;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            eventQueue.enqueue(() -> camera.zoom(-e.getWheelRotation()));
        }
    }
}

