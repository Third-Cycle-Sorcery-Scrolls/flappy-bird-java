import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {

    private GamePanel gamePanel;

    public InputHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (gamePanel.getGameState()) {
            case MENU:
                if (key == KeyEvent.VK_SPACE) gamePanel.startGame();
                break;

            case RUNNING:
                if (key == KeyEvent.VK_SPACE) gamePanel.flapBird();
                else if (key == KeyEvent.VK_ESCAPE) gamePanel.pauseGame();
                break;

            case GAME_OVER:
                if (key == KeyEvent.VK_SPACE) gamePanel.resetGame();
                break;

            case PAUSED:
                if (key == KeyEvent.VK_ESCAPE) gamePanel.resumeGame();
                break;
        }
    }
}
