package src;

import java.util.Iterator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
// import javax.management.ConstructorParameters;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
// import java.io.IOException;
// import java.awt.*;
// import javax.imageio.ImageIO;
import src.FxGameOverView;
import src.GameOverListener;

// import src.SpriteSheet.Sprites;

/**
 * GamePanel is the main panel that manages game state, rendering, and updates.
 * It coordinates the bird, pipes, input, collision detection, and score.
 */
public class GamePanel extends JPanel implements ActionListener {
    private final int boardWidth = 360;
    private final int boardHeight = 640;
//As ratio is 360/640
    private BufferedImage backgroundImg;
    private BufferedImage gameOverImg;
    // private BufferedImage birdImg;
    // private BufferedImage topPipeImg;
    // private BufferedImage bottomPipeImg;

    private Bird bird;
    private ArrayList<Pipe> pipes;
    private ScoreManager scoreManager;
    private boolean gameOver;


    private Timer gameLoopTimer;
    private Timer pipeSpawnerTimer;
    private final PipePool pipePool = new PipePool();

    private GamePhase currentPhase;
    private Timer phaseTimer;
    private long phaseStartTime;
    private boolean isDarkMode = false;
    private float darknessAlpha = 0f;
    private Color darkOverlay = new Color(0, 0, 0, 0);
    
    // JavaFX Game Over overlay
    private JFXPanel gameOverPanel;
    private boolean gameOverScreenShown = false;
    private GameOverActionListener gameOverActionListener;


    /**
     * Constructor for the GamePanel.
     * It initializes images, game objects, and timers.
     * 
     * @param gameOverActionListener listener for game over actions (can be null)
     */
    public GamePanel(GameOverActionListener gameOverActionListener) {
        this.gameOverActionListener = gameOverActionListener;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);

        // Load images
        backgroundImg = Assets.BACKGROUND;
        gameOverImg = Assets.GAMEOVER;

        // Initialize game objects
        bird = new Bird(boardWidth / 8, boardHeight / 2, 51, 36);
        pipes = new ArrayList<>();
        scoreManager = new ScoreManager();
        gameOver = false;
        // Phasers
        currentPhase = GamePhase.PHASE_1;
        isDarkMode = currentPhase.isDark();
        bird.setJumpForce(currentPhase.getJumpForce());

        phaseTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                checkPhaseTransition();
            }
        });
        phaseTimer.start();
        phaseStartTime = System.currentTimeMillis();


        // Set up input listener
        addKeyListener(new Input(this));

        // Modify pipe spawner
        // Timer to spawn pipes every 1.5 seconds
        pipeSpawnerTimer = new Timer(currentPhase.getHGap(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipe();
            }
        });
        pipeSpawnerTimer.start();

        // Main game loop timer (60 FPS)
        gameLoopTimer = new Timer(1000 / 60, this);
        gameLoopTimer.start();
    }

    private void checkPhaseTransition(){
        long elapsedTime = System.currentTimeMillis() - phaseStartTime;

        if(elapsedTime >= currentPhase.getDuration()){
            advanceToNextPhase();
        }

        if(isDarkMode && darknessAlpha < 0.8f){
            darknessAlpha += 0.02f;
            if(darknessAlpha > 0.8f){
                darknessAlpha = 0.8f;
            }
            darkOverlay = new Color(0, 0, 0, (int)(darknessAlpha * 255));
        }else if(!isDarkMode && darknessAlpha > 0f){
            darknessAlpha -= 0.01f;
            if(darknessAlpha < 0f){
                darknessAlpha = 0f;
            }
            darkOverlay = new Color(0, 0, 0, (int)(darknessAlpha * 255));
        }
    }

    private void advanceToNextPhase(){
        int nextOrdinal = currentPhase.ordinal() + 1;
        if(nextOrdinal < GamePhase.values().length){
            currentPhase = GamePhase.values()[nextOrdinal];
            applyPhaseSettings();
            phaseStartTime = System.currentTimeMillis();

            showPhaseTransition();
        }
    }

    private void applyPhaseSettings(){
        pipeSpawnerTimer.setDelay(currentPhase.getHGap());
        pipeSpawnerTimer.restart();

        for(Pipe pipe : pipes){
            pipe.setSpeed(currentPhase.getPipeSpeed());
        }
        bird.setGravity(currentPhase.getGravity());
        bird.setJumpForce(currentPhase.getJumpForce());

        isDarkMode = currentPhase.isDark();
        if(!isDarkMode){
            darknessAlpha = 0f;
            darkOverlay = new Color(0, 0, 0, 0);
        }
    }

    private void showPhaseTransition(){
        System.out.println("Entering " + currentPhase.getPhaseName());
        // May be sound effects, popups, later...
    }

    //  Places a new pair of top and bottom pipes with a random vertical offset.
    private void placePipe() {
        float pipeWidth = 64;
        float pipeHeight = 512;
        float x = boardWidth; // start from right edge
        float randomTopY = -(float)(Math.random() * (pipeHeight / 2)) - pipeHeight / 4;

        // Use phase-specific vGap
        float vGap = currentPhase.getVGap();
                      
        // Acquire a pipe from the pipePool class
        Pipe pipePair = pipePool.acquire(x, randomTopY, pipeWidth, pipeHeight, vGap, currentPhase.getPipeSpeed());
        pipes.add(pipePair);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();

        if (gameOver && !gameOverScreenShown) {
            // Stop timers when game is over
            pipeSpawnerTimer.stop();
            gameLoopTimer.stop();
            phaseTimer.stop();
            
            // Update high score
            scoreManager.updateHighScore();
            
            // Show JavaFX game over screen
            showGameOverScreen();
        }
    }

    /**
     * Updates the game logic: moves bird and pipes, checks for collisions, updates score.
     */
    private void updateGame() {
        if (gameOver) {
            return;
        }

        // Move bird
        bird.move();

        // Move pipes and check for passing and collisions
        Iterator<Pipe> it = pipes.iterator();
        while (it.hasNext()) {
            Pipe pipe = it.next();
            pipe.move();

            // scoring
            if (!pipe.isPassed() && bird.getX() > pipe.getX() + pipe.getWidth()) {
                scoreManager.incrementScore(currentPhase);
                pipe.setPassed(true);
            }

            // collision
            if (CollisionDetector.isColliding(bird, pipe)) {
                gameOver = true;
                break;
            }

            // release when completely off left side of screen
            if (pipe.isOffScreen()) {
                it.remove();
                pipePool.release(pipe); // return to pool for reuse
            }
        }


        // Check if bird hit the ground
        if (bird.getY() + bird.getHeight() > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    /**
     * Renders the game objects: background, bird, pipes, and score.
     *
     * @param g the Graphics context
     */
    private void drawGame(Graphics g) {
        // Draw background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // Apply darkness overlay if in dark phase
        if (isDarkMode || darknessAlpha > 0) {
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, darknessAlpha));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, boardWidth, boardHeight);
            g2d.setComposite(oldComposite);
        }

        // Draw bird
        bird.draw(g);

        // Draw pipes
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }

        // Draw score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        
        // Draw phase info
        long elapsed = System.currentTimeMillis() - phaseStartTime;
        long remaining = Math.max(0, currentPhase.getDuration() - elapsed);
        int secondsRemaining = (int)(remaining / 1000);
        
        g.drawString("Phase " + currentPhase.getPhaseNumber() + ": " + 
                    currentPhase.getPhaseName(), 10, 25);
        g.drawString("Time: " + secondsRemaining + "s", 10, 50);
        
        // Draw score
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (!gameOverScreenShown) {
            g.drawString("Score: " + scoreManager.getScore(), 10, 80);
        }
    }


    /**
     * Shows the JavaFX game over overlay.
     * Must be called on Swing EDT.
     */
    private void showGameOverScreen() {
        if (gameOverScreenShown) {
            return;
        }
        gameOverScreenShown = true;
        
        // Create JFXPanel for game over overlay
        gameOverPanel = new JFXPanel();
        gameOverPanel.setPreferredSize(new Dimension(boardWidth, boardHeight));
        gameOverPanel.setSize(boardWidth, boardHeight);
        gameOverPanel.setBounds(0, 0, boardWidth, boardHeight);
        gameOverPanel.setOpaque(false);
        
        // Use null layout for absolute positioning overlay
        if (getLayout() == null) {
            setLayout(null);
        }
        add(gameOverPanel);
        gameOverPanel.setVisible(true);
        // Bring to front by setting z-order (lower index = higher z-order)
        setComponentZOrder(gameOverPanel, 0);
        
        // Initialize JavaFX scene on JavaFX Application Thread
        Platform.runLater(() -> {
            FxGameOverView gameOverView = new FxGameOverView(
                boardWidth, 
                boardHeight,
                scoreManager.getScore(),
                scoreManager.getHighScore(),
                new GameOverListener() {
                    @Override
                    public void onRestart() {
                        // Restart game on Swing EDT
                        SwingUtilities.invokeLater(() -> {
                            restartGame();
                        });
                    }
                    
                    @Override
                    public void onExitToMenu() {
                        // Exit to menu on Swing EDT
                        SwingUtilities.invokeLater(() -> {
                            exitToMenu();
                        });
                    }
                }
            );
            
            gameOverPanel.setScene(gameOverView.getScene());
        });
        
        revalidate();
        repaint();
    }
    
    /**
     * Restarts the game after game over.
     * Must be called on Swing EDT.
     */
    private void restartGame() {
        // Remove game over overlay
        if (gameOverPanel != null) {
            remove(gameOverPanel);
            gameOverPanel = null;
        }
        
        gameOverScreenShown = false;
        
        // Reset game state
        resetGame();
    }
    
    /**
     * Exits to main menu.
     * Must be called on Swing EDT.
     */
    private void exitToMenu() {
       scoreManager.updateHighScore();
       System.exit(0);

        if (gameOverActionListener != null) {
            gameOverActionListener.onExitToMenu();
        }
    }
    
    public void resetGame() {
        // release all current pipes back to pool
        for (Pipe p : pipes) {
            pipePool.release(p);
        }
        pipes.clear();

        bird.reset(boardWidth / 8, boardHeight / 2);
        bird.reset(boardWidth / 8.0f, boardHeight / 2.0f);
        scoreManager.reset();
        gameOver = false;
        currentPhase = GamePhase.PHASE_1;
        phaseStartTime = System.currentTimeMillis();
        isDarkMode = currentPhase.isDark();
        darknessAlpha = 0f;
        darkOverlay = new Color(0, 0, 0, 0);

        bird.setJumpForce(currentPhase.getJumpForce());
        
        applyPhaseSettings();

        // restart timers as before
        phaseTimer.restart();
        pipeSpawnerTimer.start();
        gameLoopTimer.start();
    }

    // Getter methods for bird and game state
    public Bird getBird() {
        return bird;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
