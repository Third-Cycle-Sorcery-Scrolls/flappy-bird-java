package src;

import java.io.*;

/**
 * Manages current score and persistent high score.
 */
public class ScoreManager {

    private int score;
    private int highScore;

    private static final String FILE_NAME = "highscore.txt";

    public ScoreManager() {
        score = 0;
        highScore = loadHighScore();
    }

    public void incrementScore() {
        score++;
    }

    public void reset() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    /**
     * Call when game ends.
     */
    public void updateHighScore() {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }

    // ---------------- FILE I/O ----------------

    private int loadHighScore() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            return Integer.parseInt(br.readLine());
        } catch (Exception e) {
            return 0; // first run or file missing
        }
    }

    private void saveHighScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.err.println("Failed to save high score");
        }
    }
}

