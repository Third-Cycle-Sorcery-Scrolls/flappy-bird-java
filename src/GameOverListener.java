package src;

/**
 * Interface for handling game over screen actions.
 * Allows the JavaFX game over screen to communicate with the Swing game.
 */
public interface GameOverListener {
    /**
     * Called when the "Restart" button is clicked.
     * This should reset and restart the game.
     */
    void onRestart();
    
    /**
     * Called when the "Exit to Menu" button is clicked.
     * This should return to the main menu.
     */
    void onExitToMenu();
}

