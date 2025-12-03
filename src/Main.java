
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        GamePanel panel = new GamePanel();

        frame.add(panel);
        frame.setSize(500, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        panel.startGame();
    }
}
