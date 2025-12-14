package src;

import java.awt.Graphics;
// import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
// import src.SpriteSheet;
// import src.SpriteSheet.Sprites;

/**
 * Pipe class represents a pair of top and bottom pipes, 
 * including movement and drawing logic.
 */
public class Pipe {
    private int x;
    private int topY;
    private int width;
    private int height;
    private BufferedImage topImage;
    private BufferedImage bottomImage;
    private boolean passed;

    private static final int VELOCITY_X = -4;
    private static final int GAP = 150; // gap between top and bottom pipe. (Grandpa mode for slow players lol)

    public Pipe(int startX, int topY, int width, int height) {
        this.x = startX;
        this.topY = topY;
        this.width = width;
        this.height = height;
        this.topImage = Assets.PIPE_TOP;
        this.bottomImage = Assets.PIPE_BOTTOM;
        this.passed = false;
    }

    /**
     * Moves the pipe horizontally based on velocity.
     */
    public void move() {
        x += VELOCITY_X;
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
     * Gets the bounding rectangle of the top pipe for collision detection.
     *
     * @return bounding Rectangle for top pipe
     */
    public Rectangle getTopBounds() {
        return new Rectangle(x, topY, width, height);
    }

    /**
     * Gets the bounding rectangle of the bottom pipe for collision detection.
     *
     * @return bounding Rectangle for bottom pipe
     */
    public Rectangle getBottomBounds() {
        int bottomY = topY + height + GAP;
        return new Rectangle(x, bottomY, width, height);
    }

    // Getter methods
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
