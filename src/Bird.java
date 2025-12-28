package src;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * Bird class manages the bird's position, movement, and drawing.
 */
public class Bird {
    private float x;
    private float y;
    private float width;
    private float height;
    private BufferedImage img = Assets.BIRD;
    private float velocityY;
    private float gravity;
    private float jumpForce;

    public Bird(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityY = 0;
        this.gravity = 1.0f;
        this.jumpForce = -9.0f;
    }

    /**
     * Updates the bird's position by applying gravity.
     */
    public void move() {
        velocityY += gravity;
        y += velocityY;
        // Prevent the bird from moving above the top of the panel
        if (y < 0) {
            y = 0;
            velocityY = 0;
        }
    }
    /**
     * Causes the bird to jump by setting its vertical velocity.
     */
    public void jump() {
        velocityY = jumpForce;
    }

    // Draws the bird on the screen
    public void draw(Graphics g) {
        g.drawImage(img, Math.round(x), Math.round(y), Math.round(width), Math.round(height), null);

    }

      //Resets the bird to the given position and stops vertical movement.
    public void reset(float newX, float newY) {
        this.x = newX;
        this.y = newY;
        this.velocityY = 0;
    }
    
     //Gets the bounding rectangle of the bird for collision detection.
    public Rectangle getBounds() {
        return new Rectangle(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
    }

    public int getX() {
        return Math.round(x);
    }

    public int getY() {
        return Math.round(y);
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
    public void setJumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }
}
