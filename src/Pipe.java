package src;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Pipe class represents a pair of top and bottom pipes, 
 * including movement, drawing, and reset logic for reuse.
 */
public class Pipe {
    private float x;
    private float topY;
    private float width;
    private float height;
    private final BufferedImage topImage = Assets.PIPE_TOP;
    private final BufferedImage bottomImage = Assets.PIPE_BOTTOM;
    private boolean passed;
    private float vGap;
    private float speed;

    // Cached rectangles to avoid allocating new Rectangle objects every frame
    private final Rectangle topBounds = new Rectangle();
    private final Rectangle bottomBounds = new Rectangle();

    /**
     * Constructor for Pipe pair.
     * Note: prefer creating via PipePool or calling reset after construction.
     */
    public Pipe(float startX, float topY, float width, float height, float vGap, float speed) {
        this.x = startX;
        this.topY = topY;
        this.width = width;
        this.height = height;
        this.vGap = vGap;
        this.speed = speed;
        this.passed = false;
        updateBounds();
    }

    /**
     * Reset this pipe so it can be reused.
     * This should be called when retrieving a pipe from a pool.
     */
    public void reset(float startX, float topY, float width, float height, float vGap, float speed){
        this.x = startX;
        this.topY = topY;
        this.width = width;
        this.height = height;
        this.vGap = vGap;
        this.speed = speed;
        this.passed = false;
        updateBounds();
    }

    /**
     * Moves the pipe horizontally based on velocity and updates bounds.
     */
    public void move() {
        x += speed;
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
        g.drawImage(topImage, Math.round(x), Math.round(topY), Math.round(width), Math.round(height), null);
        // Draw bottom pipe (gap below top pipe)
        float bottomY = topY + height + vGap;
        g.drawImage(bottomImage, Math.round(x), Math.round(bottomY), Math.round(width), Math.round(height), null);
    }

    /**
     * Update cached bounds rectangles to current position/size.
     */
    private void updateBounds() {
        int intX = Math.round(x);
        int intTopY = Math.round(topY);
        int intWidth = Math.round(width);
        int intHeight = Math.round(height);
        
        topBounds.setBounds(intX, intTopY, intWidth, intHeight);
        
        float bottomY = topY + height + vGap;
        int intBottomY = Math.round(bottomY);
        bottomBounds.setBounds(intX, intBottomY, intWidth, intHeight);
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

    public float getX() {
        return x;
    }

    public float getWidth() {
        return width;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setVGap(float vGap) {
        this.vGap = vGap;
    }
}
