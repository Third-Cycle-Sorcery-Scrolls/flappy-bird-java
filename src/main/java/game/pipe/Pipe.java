package game.pipe;

import java.awt.*;
import java.util.Random;

public class Pipe {

    public static final int PIPE_WIDTH = 80;      
    public static final int GAP_SIZE = 180;     
    public static final int SPEED = 4;            
    public static final int SCREEN_HEIGHT = 600;  

    private int x;
    private int gapStartY;
    private boolean passed = false;

    private Random rand = new Random();

    public Pipe(int startX) {
        this.x = startX;
        gapStartY = rand.nextInt(SCREEN_HEIGHT - GAP_SIZE - 100) + 50;
    }

    public void update() {
        x -= SPEED;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);

        g.fillRect(x, 0, PIPE_WIDTH, gapStartY);
        g.fillRect(x, gapStartY + GAP_SIZE, PIPE_WIDTH, SCREEN_HEIGHT);
    }

    public Rectangle getTopRect() {
        return new Rectangle(x, 0, PIPE_WIDTH, gapStartY);
    }

    public Rectangle getBottomRect() {
        return new Rectangle(x, gapStartY + GAP_SIZE, PIPE_WIDTH, SCREEN_HEIGHT);
    }

    public boolean isOffScreen() {
        return x + PIPE_WIDTH < 0;
    }

    public boolean isPassedBy(int birdX) {
        if (!passed && birdX > x + PIPE_WIDTH) {
            passed = true;
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }
}


