package src;

import javax.swing.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import java.util.concurrent.CountDownLatch;

/**
 * Main class that integrates Swing and JavaFX.
 * The Swing JFrame is the main window, and JavaFX menu is embedded using JFXPanel.
 */
public class Main {

    private static JFrame frame;
    private static JFXPanel jfxPanel;
    private static int boardWidth = 360;
    private static int boardHeight = 640;

    public static void main(String[] args) throws Exception {
        // Load game assets
        Assets.load();

        // Initialize JavaFX platform
        initializeJavaFX();

        // Create Swing frame on EDT
        SwingUtilities.invokeLater(() -> {
            createAndShowFrame();
        });
    }

    /**
     * Initializes the JavaFX platform.
     * This must be called before creating any JavaFX components.
     */
    private static void initializeJavaFX() {
        // JavaFX platform needs to be initialized before creating JFXPanel
        // The simplest way is to create a dummy JFXPanel which initializes the toolkit
        final CountDownLatch latch = new CountDownLatch(1);
        
        // Create a dummy JFXPanel on Swing EDT to initialize JavaFX toolkit
        SwingUtilities.invokeLater(() -> {
            try {
                // Create a temporary JFXPanel to initialize the JavaFX toolkit
                JFXPanel dummyPanel = new JFXPanel();
                // Now we can safely use Platform.runLater
                Platform.runLater(() -> {
                    Platform.setImplicitExit(false); // Don't exit when last window closes
                    latch.countDown();
                });
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and displays the main JFrame with the JavaFX menu.
     * This method must be called on the Swing EDT.
     */
    private static void createAndShowFrame() {
        frame = new JFrame("Flappy Bird");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and show JavaFX menu
        showMenu();

        frame.setVisible(true);
    }

    /**
     * Shows the JavaFX menu in a JFXPanel.
     * This method must be called on the Swing EDT.
     */
    private static void showMenu() {
        jfxPanel = new JFXPanel();
        frame.setContentPane(jfxPanel);
        frame.revalidate();
        frame.repaint();

        // Initialize JavaFX scene on JavaFX Application Thread
        Platform.runLater(() -> {
            FxMenuView menuView = new FxMenuView(boardWidth, boardHeight, new MenuActionListener() {
                @Override
                public void onStartGame() {
                    // Switch to game on Swing EDT
                    SwingUtilities.invokeLater(() -> {
                        startGame();
                    });
                }

                @Override
                public void onExit() {
                    // Exit on Swing EDT
                    SwingUtilities.invokeLater(() -> {
                        System.exit(0);
                    });
                }
            });

            jfxPanel.setScene(menuView.getScene());
        });
    }

    /**
     * Removes the JavaFX menu and shows the Swing GamePanel.
     * This method must be called on the Swing EDT.
     */
    private static void startGame() {
        // Remove JFXPanel
        if (jfxPanel != null) {
            frame.remove(jfxPanel);
            jfxPanel = null;
        }

        // Create and add GamePanel with callback for exiting to menu
        GamePanel gamePanel = new GamePanel(new GameOverActionListener() {
            @Override
            public void onExitToMenu() {
                // Return to menu on Swing EDT
                SwingUtilities.invokeLater(() -> {
                    returnToMenu();
                });
            }
        });
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocus();
    }
    
    /**
     * Returns to the main menu from the game.
     * This method must be called on the Swing EDT.
     */
    private static void returnToMenu() {
        // Remove current content (GamePanel)
        frame.getContentPane().removeAll();
        
        // Show menu again
        showMenu();
        
        frame.revalidate();
        frame.repaint();
    }
}