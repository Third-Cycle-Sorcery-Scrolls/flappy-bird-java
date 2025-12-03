import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private boolean running = false;

    public enum GameState { MENU, RUNNING, GAME_OVER, PAUSED }
    private GameState gameState = GameState.MENU;

    private int score = 0;

    // Bird visual
    private int birdY = 400;
    private int birdVelocity = 0;
    private final int GRAVITY = 1;

    // Background image
    private BufferedImage backgroundImage;

    public GamePanel() {
        setFocusable(true);

        try {
            backgroundImage = ImageIO.read(new File("./assets/clouds.jpg")); // your cloud background
        } catch (IOException e) {
            System.out.println("Background image not found!");
        }
    }

    public GameState getGameState() { return gameState; }

    public void startGame() {
        gameState = GameState.RUNNING;
        running = true;
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void resetGame() {
        score = 0;
        birdY = 400;
        birdVelocity = 0;
        gameState = GameState.RUNNING;
    }

    public void pauseGame() { gameState = GameState.PAUSED; }

    public void resumeGame() { gameState = GameState.RUNNING; }

    public void flapBird() {
        birdVelocity = -15; // bird moves up
    }

    @Override
    public void run() {
        while (true) {
            if (gameState == GameState.RUNNING) updateGame();
            repaint();
            try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void updateGame() {
        // Gravity
        birdVelocity += GRAVITY;
        birdY += birdVelocity;

        // Hit ground
        if (birdY > 700) {
            birdY = 700;
            birdVelocity = 0;
            gameState = GameState.GAME_OVER;
        }

        // Dummy score
        score++;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image if available
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        switch (gameState) {
            case MENU:
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                g.drawString("FLAPPY BIRD", 120, 200);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("Press SPACE to Start", 150, 300);
                break;

            case RUNNING:
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("Score: " + score, 10, 30);

                // Draw bird
                g.setColor(Color.YELLOW);
                g.fillRect(200, birdY, 30, 30);
                break;

            case GAME_OVER:
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                g.drawString("GAME OVER", 140, 200);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("Score: " + score, 180, 250);
                g.drawString("Press SPACE to Restart", 130, 300);
                break;

            case PAUSED:
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 32));
                g.drawString("PAUSED", 170, 250);
                break;
        }
    }
}
