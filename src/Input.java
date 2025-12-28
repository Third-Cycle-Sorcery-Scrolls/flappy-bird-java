package src;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Input class manages keyboard input (e.g., space bar to flap).
 * It listens for key presses and triggers actions on the game.
 */
public class Input extends KeyAdapter {
    private GamePanel gamePanel;

    /**
     * Constructor that takes the GamePanel to control.
     *
     * @param gamePanel reference to the main game panel
     */
    public Input(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        boolean isGameOver = gamePanel.isGameOver();
        // Space bar pressed: make bird jump or reset if game over
        if (key == KeyEvent.VK_SPACE) {
            if (isGameOver) {
                gamePanel.resetGame();
            } else {
                gamePanel.getBird().jump();
            }
        }else if(key == KeyEvent.VK_ENTER){
            if(isGameOver){
                gamePanel.resetGame();
            }
        }else if((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !isGameOver){
            gamePanel.getBird().jump();
        }
    }
}
