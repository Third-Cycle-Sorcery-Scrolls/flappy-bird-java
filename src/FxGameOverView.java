package src;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * JavaFX game over view that displays an overlay with game over information.
 * This view is embedded into Swing using JFXPanel.
 */
public class FxGameOverView {
    private Scene scene;
    private GameOverListener listener;
    private StackPane root;
    
    /**
     * Creates a new FxGameOverView with the specified dimensions, scores, and listener.
     * 
     * @param width the width of the overlay
     * @param height the height of the overlay
     * @param currentScore the current game score
     * @param highScore the high score
     * @param listener the listener to handle game over actions
     */
    public FxGameOverView(int width, int height, int currentScore, int highScore, GameOverListener listener) {
        this.listener = listener;
        createGameOverOverlay(width, height, currentScore, highScore);
    }
    
    /**
     * Creates the game over overlay UI.
     * 
     * @param width the width of the overlay
     * @param height the height of the overlay
     * @param currentScore the current game score
     * @param highScore the high score
     */
    private void createGameOverOverlay(int width, int height, int currentScore, int highScore) {
        root = new StackPane();
        
        // Semi-transparent dark background
        Rectangle darkOverlay = new Rectangle(width, height);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.7));
        darkOverlay.getStyleClass().add("dark-overlay");
        
        // Centered panel/card
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(300);
        card.setMaxHeight(400);
        card.getStyleClass().add("game-over-card");
        
        // Game Over title
        Label titleLabel = new Label("GAME OVER");
        titleLabel.getStyleClass().add("game-over-title");
        
        // Current score label
        Label currentScoreLabel = new Label("Score: " + currentScore);
        currentScoreLabel.getStyleClass().add("score-label");
        
        // High score label
        Label highScoreLabel = new Label("Best: " + highScore);
        highScoreLabel.getStyleClass().add("high-score-label");
        
        // Restart button
        Button restartButton = new Button("Restart");
        restartButton.getStyleClass().add("game-over-button");
        restartButton.setPrefWidth(200);
        restartButton.setPrefHeight(45);
        restartButton.setOnAction(e -> {
            if (listener != null) {
                listener.onRestart();
            }
        });
        addHoverAnimation(restartButton);
        
        // Exit to Menu button
        Button exitToMenuButton = new Button("Exit to Menu");
        exitToMenuButton.getStyleClass().add("game-over-button");
        exitToMenuButton.setPrefWidth(200);
        exitToMenuButton.setPrefHeight(45);
        exitToMenuButton.setOnAction(e -> {
            if (listener != null) {
                listener.onExitToMenu();
            }
        });
        addHoverAnimation(exitToMenuButton);
        
        // Add all elements to card
        card.getChildren().addAll(
            titleLabel,
            currentScoreLabel,
            highScoreLabel,
            restartButton,
            exitToMenuButton
        );
        
        // Add overlay and card to root
        root.getChildren().addAll(darkOverlay, card);
        
        // Create scene
        scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        
        // Load CSS
        try {
            String cssPath = getClass().getResource("gameover-style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Warning: Could not load CSS file: " + e.getMessage());
        }
        
        // Add fade-in animation
        addFadeInAnimation();
    }
    
    /**
     * Adds smooth fade-in animation to the game over overlay.
     */
    private void addFadeInAnimation() {
        root.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
    
    /**
     * Adds smooth hover animation to a button.
     * 
     * @param button the button to animate
     */
    private void addHoverAnimation(Button button) {
        javafx.animation.ScaleTransition scaleUp = new javafx.animation.ScaleTransition(
            javafx.util.Duration.millis(150), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        
        javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(
            javafx.util.Duration.millis(150), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        button.setOnMouseEntered(e -> scaleUp.play());
        button.setOnMouseExited(e -> scaleDown.play());
    }
    
    /**
     * Gets the JavaFX Scene for this game over overlay.
     * 
     * @return the Scene object
     */
    public Scene getScene() {
        return scene;
    }
}

