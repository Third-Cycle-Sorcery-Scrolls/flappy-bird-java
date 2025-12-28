package src;

import java.util.Iterator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
// import javax.management.ConstructorParameters;
import javax.swing.*;
import java.awt.image.BufferedImage;
// import java.io.IOException;
// import java.awt.*;
// import javax.imageio.ImageIO;

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

//Another way to try sprite using spriteimage helper class

    // Image spriteSheetImage =
    // new ImageIcon(getClass().getResource("/assets/sprites.png")).getImage();
    // SpriteSheet spriteSheet = new SpriteSheet(spriteSheetImage);    
/*

Trial for sprite image implementation
    BufferedImage sheet;
    {
        java.io.InputStream is = null;
        try{
            is = GamePanel.class.getResourceAsStream("/assets/sprites.png");
            if(is == null){
                System.err.println("Sprite Resource is not found in specified directory");
                sheet = new BufferedImage(480,160, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = sheet.createGraphics();
                g.setColor(Color.MAGENTA);
                // g.fillArc(0, 0, sheet.getWidth(), sheet.getHeight(), 0, 360);
                g.fillRect(0, 0, sheet.getWidth(), sheet.getHeight());
                g.dispose();
            }
            else {
                sheet = ImageIO.read(is);
            }
        } catch(IOException e) {
            e.printStackTrace();
            if(sheet == null){
                sheet = new BufferedImage(80,100, BufferedImage.TYPE_INT_ARGB);
            }
        } finally {
            if(is != null){
                try {is.close();} catch (IOException ignored){}
            }
        }
    }
    protected int sheetScale = sheet.getWidth()/6;
    Image sprite;

    public void paint(Graphics2D g2d){
        // g2d.drawImage(sprite, x, y, null);
    }
        **/

    /**
     * Constructor for the GamePanel.
     * It initializes images, game objects, and timers.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);

        
        // Load images
        
        backgroundImg = Assets.BACKGROUND;
        gameOverImg = Assets.GAMEOVER;
        // birdImg = Assets.BIRD;
        // topPipeImg = Assets.PIPE_TOP;
        // bottomPipeImg = Assets.PIPE_BOTTOM;

        // Initialize game objects
        bird = new Bird(boardWidth / 8, boardHeight / 2, 51, 36);
        pipes = new ArrayList<>();
        scoreManager = new ScoreManager();
        gameOver = false;
        // Phasers
        currentPhase = GamePhase.PHASE_1;
        isDarkMode = currentPhase.isDark();

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

        if(isDarkMode && darknessAlpha < 0.7f){
            darknessAlpha += 0.01f;
            if(darknessAlpha > 0.7f){
                darknessAlpha = 0.7f;
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
        int pipeWidth = 64;
        int pipeHeight = 512;
        int x = boardWidth; // start from right edge
        int randomTopY = -(int)(Math.random() * (pipeHeight / 2)) - pipeHeight / 4;

        // Use phase-specific vGap
        int vGap = currentPhase.getVGap();
                      
        // Acquire a pipe from the pipePool class
        Pipe pipePair = pipePool.acquire(x, randomTopY, pipeWidth, pipeHeight, vGap, currentPhase.getPipeSpeed());
        pipes.add(pipePair);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();

        if (gameOver) {
            // Stop timers when game is over
            pipeSpawnerTimer.stop();
            gameLoopTimer.stop();
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
                scoreManager.incrementScore();
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
        if (gameOver) {
            g.drawImage(gameOverImg, 0, boardHeight/2 - 42, 180*2, 42*2, null);
            g.drawString("Final Score: " + scoreManager.getScore(), 10, 80);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Phase: " + currentPhase.getPhaseName(), 10, 110);
        } else {
            g.drawString("Score: " + scoreManager.getScore(), 10, 80);
        }
    }


    public void resetGame() {
    // release all current pipes back to pool
    for (Pipe p : pipes) {
        pipePool.release(p);
    }
    pipes.clear();

    bird.reset(boardWidth / 8, boardHeight / 2);
    scoreManager.reset();
    gameOver = false;
    currentPhase = GamePhase.PHASE_1;
    phaseStartTime = System.currentTimeMillis();
    isDarkMode = currentPhase.isDark();
    darknessAlpha = 0f;
    darkOverlay = new Color(0, 0, 0, 0);
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
