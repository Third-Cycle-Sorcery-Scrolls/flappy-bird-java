
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private boolean running = false;

    public GamePanel() {
        setFocusable(true);
        setBackground(Color.BLACK);
    }

    public void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            updateGame();
            repaint();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        // Will call updates later
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Will draw later
    }
}
