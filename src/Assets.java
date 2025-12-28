package src;

import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.imageio.ImageIO;

public final class Assets {

    public static BufferedImage SPRITE_SHEET;

    public static BufferedImage BIRD;
    public static BufferedImage PIPE_TOP;
    public static BufferedImage PIPE_BOTTOM;
    public static BufferedImage BACKGROUND;
    public static BufferedImage GAMEOVER;

    private Assets() {}

    public static void load() {
        try {
            java.net.URL resourceUrl = null;
            
            // Try multiple resource paths
            // 1. Relative to package (src/assets/sprites.png)
            resourceUrl = Assets.class.getResource("assets/sprites.png");
            
            // 2. Absolute from classpath root (/src/assets/sprites.png)
            if (resourceUrl == null) {
                resourceUrl = Assets.class.getResource("/src/assets/sprites.png");
            }
            
            // 3. Using classloader from classpath root
            if (resourceUrl == null) {
                resourceUrl = Assets.class.getClassLoader().getResource("src/assets/sprites.png");
            }
            
            // 4. Try without src prefix
            if (resourceUrl == null) {
                resourceUrl = Assets.class.getClassLoader().getResource("assets/sprites.png");
            }
            
            // 5. Fallback: try as file path (for development/IDE)
            if (resourceUrl == null) {
                try {
                    java.io.File file = new java.io.File("src/assets/sprites.png");
                    if (file.exists()) {
                        resourceUrl = file.toURI().toURL();
                    }
                } catch (Exception e) {
                    // Ignore file path attempt
                }
            }
            
            if (resourceUrl == null) {
                throw new RuntimeException("Could not find sprites.png resource. " +
                    "Tried: assets/sprites.png, /src/assets/sprites.png, " +
                    "src/assets/sprites.png, and file path.");
            }
            
            SPRITE_SHEET = ImageIO.read(resourceUrl);
            System.out.println("Sprite sheet loaded successfully from: " + resourceUrl);

            BIRD = SPRITE_SHEET.getSubimage(528,   128, 34, 25);
            PIPE_TOP = SPRITE_SHEET.getSubimage(604,  0, 52, 270);
            PIPE_BOTTOM = SPRITE_SHEET.getSubimage(660, 0, 52, 270);
            BACKGROUND = SPRITE_SHEET.getSubimage(0, 0, 288, 512);
            GAMEOVER = SPRITE_SHEET.getSubimage(291, 396, 191, 42);

        } catch (Exception e) {
            System.out.println("Cannot get Sprite sheet res");
            throw new RuntimeException("Failed to load assets", e);
            
        }
    }
}