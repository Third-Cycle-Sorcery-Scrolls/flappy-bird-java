package src;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javax.swing.*;
import java.awt.*;

public class HybridTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swing + JavaFX App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());
            JButton jbutton = new JButton("This is swing");


            // JavaFX bridge
            JFXPanel fxPanel = new JFXPanel();
            frame.add(fxPanel, BorderLayout.CENTER);

            frame.setVisible(true);

            initFX(fxPanel);
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        Platform.runLater(() -> {
            fxPanel.setScene(FxView.createScene());
        });
    }
}

class FxView {

    public static Scene createScene() {
        Label label = new Label("JavaFX inside Swing");
        label.setStyle("-fx-font-size: 20px;");

        Button button = new Button("Click Me");
        button.setOnAction(e -> label.setText("Hello from JavaFX"));

        VBox root = new VBox(15, label, button);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        return new Scene(root);
    }
}