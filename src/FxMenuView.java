package src;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

/**
 * JavaFX menu view that displays a pre-game menu with title and buttons.
 * This view is embedded into Swing using JFXPanel.
 */
public class FxMenuView {
    private Scene scene;
    private MenuActionListener listener;
    
    /**
     * Creates a new FxMenuView with the specified dimensions and listener.
     * 
     * @param width the width of the menu
     * @param height the height of the menu
     * @param listener the listener to handle menu actions
     */
    public FxMenuView(int width, int height, MenuActionListener listener) {
        this.listener = listener;
        createMenu(width, height);
    }
    
    /**
     * Creates the menu UI with title and buttons.
     * 
     * @param width the width of the menu
     * @param height the height of the menu
     */
    private void createMenu(int width, int height) {
        // Create title
        Text title = new Text("Flappy Bird");
        title.setFont(new Font("Arial", 48));
        title.getStyleClass().add("title");
        
        // Create Start Game button
        Button startButton = new Button("Start Game");
        startButton.getStyleClass().add("menu-button");
        startButton.setPrefWidth(200);
        startButton.setPrefHeight(50);
        startButton.setOnAction(e -> {
            if (listener != null) {
                listener.onStartGame();
            }
        });
        addHoverAnimation(startButton);
        
        // Create Exit button
        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("menu-button");
        exitButton.setPrefWidth(200);
        exitButton.setPrefHeight(50);
        exitButton.setOnAction(e -> {
            if (listener != null) {
                listener.onExit();
            }
        });
        addHoverAnimation(exitButton);
        
        // Create layout
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("menu-root");
        root.getChildren().addAll(title, startButton, exitButton);
        
        // Create scene
        scene = new Scene(root, width, height);
        
        // Load CSS - using same pattern as Assets class
        try {
            String cssPath = getClass().getResource("menu-style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Warning: Could not load CSS file: " + e.getMessage());
            // Continue without CSS if file not found
        }
    }
    
    /**
     * Adds smooth hover animation to a button.
     * 
     * @param button the button to animate
     */
    private void addHoverAnimation(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        button.setOnMouseEntered(e -> scaleUp.play());
        button.setOnMouseExited(e -> scaleDown.play());
    }
    
    /**
     * Gets the JavaFX Scene for this menu.
     * 
     * @return the Scene object
     */
    public Scene getScene() {
        return scene;
    }
}

