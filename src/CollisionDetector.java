package src;

import java.awt.Rectangle;

/**
 * CollisionDetector handles logic for detecting collisions 
 * between the bird and pipes (top and bottom).
 * later  will be implemented with cpp for maximum efficiency
 */
public class CollisionDetector {

     //Checks if the bird collides with the given pipe (either top or bottom)
    public static boolean isColliding(Bird bird, Pipe pipe) {
        // Bird's bounding box or (hurtbox)
        Rectangle birdBounds = bird.getBounds();
        // Check collision with top or bottom pipe(as pairs)
        if (birdBounds.intersects(pipe.getTopBounds()) || birdBounds.intersects(pipe.getBottomBounds())) {
            return true;
        }
        return false;
    }
}
