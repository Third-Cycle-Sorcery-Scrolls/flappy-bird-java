package src;

/**
 * Interface for handling menu actions from the JavaFX menu.
 * Allows the JavaFX menu to communicate with the Swing application.
 */
public interface MenuActionListener {
    /**
     * Called when the "Start Game" button is clicked.
     * This should trigger the transition from menu to game.
     */
    void onStartGame();
    
    /**
     * Called when the "Exit" button is clicked.
     * This should close the application.
     */
    void onExit();
}

