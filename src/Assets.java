package src;

import java.awt.image.BufferedImage;
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
            SPRITE_SHEET = ImageIO.read(
                //assets/sprites.png
                //OOP\Java Project backup\assets\sprites.png
                Assets.class.getResource("assets/sprites.png")
            );
            System.out.println("Sprite sheet loaded successfully: " + (SPRITE_SHEET != null));

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