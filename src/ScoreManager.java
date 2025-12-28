package src;

/**
 * ScoreManager keeps track of the player score and manages scoring updates.
 * Also tracks the high score across game sessions.
 */
public class ScoreManager {
    private int score;
    private int highScore;
    
    /**
     * Constructor initializes score to zero and loads high score.
     */
    public ScoreManager() {
        score = 0;
        highScore = 0; // Could load from file in the future
    }

    /**
     * Increments the score by one.
     */
    public void incrementScore(GamePhase currentPhase) {
        score += currentPhase.getBaseScore();
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score = 0;
    }

    /**
     * Gets the current score.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Updates the high score if the current score is higher.
     * Should be called when the game ends.
     */
    public void updateHighScore() {
        if (score > highScore) {
            highScore = score;
        }
    }
    
    /**
     * Gets the high score.
     *
     * @return the high score
     */
    public int getHighScore() {
        return highScore;
    }
}
