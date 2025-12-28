package src;

/**
 * Interface for handling game over actions that require Main class intervention.
 * Used when the game needs to exit to the main menu.
 */
public interface GameOverActionListener {
    /**
     * Called when the user wants to exit to the main menu.
     * This should switch from GamePanel back to the JavaFX menu.
     */
    void onExitToMenu();
}

