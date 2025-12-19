package src;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;

        // Load game assets
        Assets.load();




        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible(true);
        
		frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        // gamePanel.add(null);
        frame.pack();
        // gamePanel.add();
        // frame.pack();
        gamePanel.requestFocus();
        frame.setVisible(true);
    }
    
}