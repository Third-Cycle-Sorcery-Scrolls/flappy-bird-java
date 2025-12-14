package src;
public class ScoreManager {
    private int score;
 /**
 * ScoreManager keeps track of the player score and manages scoring updates.
 */
    /**
     * Constructor initializes score to zero.
     */
    public ScoreManager() {
        score = 0;
    }

    /**
     * Increments the score by one.
     */
    public void incrementScore() {
        score++;
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
}
