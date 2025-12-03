import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        GamePanel panel = new GamePanel();

        frame.add(panel);
        frame.setSize(500, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Attach InputHandler
        InputHandler input = new InputHandler(panel);
        panel.addKeyListener(input);

        // Show start menu
        panel.repaint();
    }
}
