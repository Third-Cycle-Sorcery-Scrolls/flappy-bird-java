package src;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Pipe class represents a pair of top and bottom pipes, 
 * including movement, drawing, and reset logic for reuse.
 */
public class Pipe {
    private int x;
    private int topY;
    private int width;
    private int height;

    // Shared images from Assets (do not create per-instance)
    private final BufferedImage topImage = Assets.PIPE_TOP;
    private final BufferedImage bottomImage = Assets.PIPE_BOTTOM;

    private boolean passed;

    private static final int VELOCITY_X = -4;
    private static final int GAP = 150; // gap between top and bottom pipe

    // Cached rectangles to avoid allocating new Rectangle objects every frame
    private final Rectangle topBounds = new Rectangle();
    private final Rectangle bottomBounds = new Rectangle();

    /**
     * Constructor for Pipe pair.
     * Note: prefer creating via PipePool or calling reset after construction.
     */
    public Pipe(int startX, int topY, int width, int height) {
        this.x = startX;
        this.topY = topY;
        this.width = width;
        this.height = height;
        this.passed = false;
        updateBounds();
    }

    /**
     * Reset this pipe so it can be reused.
     * This should be called when retrieving a pipe from a pool.
     */
    public void reset(int startX, int topY, int width, int height) {
        this.x = startX;
        this.topY = topY;
        this.width = width;
        this.height = height;
        this.passed = false;
        updateBounds();
    }

    /**
     * Moves the pipe horizontally based on velocity and updates bounds.
     */
    public void move() {
        x += VELOCITY_X;
        updateBounds();
    }

    /**
     * Returns true when the pipe is fully off the left side of the screen.
     */
    public boolean isOffScreen() {
        return x + width < 0;
    }

    /**
     * Draws both the top and bottom pipe on screen.
     *
     * @param g the Graphics context
     */
    public void draw(Graphics g) {
        // Draw top pipe
        g.drawImage(topImage, x, topY, width, height, null);
        // Draw bottom pipe (gap below top pipe)
        int bottomY = topY + height + GAP;
        g.drawImage(bottomImage, x, bottomY, width, height, null);
    }

    /**
     * Update cached bounds rectangles to current position/size.
     */
    private void updateBounds() {
        topBounds.setBounds(x, topY, width, height);
        int bottomY = topY + height + GAP;
        bottomBounds.setBounds(x, bottomY, width, height);
    }

    /**
     * Gets the bounding rectangle of the top pipe for collision detection.
     *
     * @return bounding Rectangle for top pipe (cached instance)
     */
    public Rectangle getTopBounds() {
        return topBounds;
    }

    /**
     * Gets the bounding rectangle of the bottom pipe for collision detection.
     *
     * @return bounding Rectangle for bottom pipe (cached instance)
     */
    public Rectangle getBottomBounds() {
        return bottomBounds;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
