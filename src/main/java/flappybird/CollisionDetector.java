package main.java.flappybird;




import java.awt.Rectangle;
import java.util.List;
import java.util.Objects;
import java.util.Optional;




public final class CollisionDetector {

    //private CollisionDetector(){}
//Not used yet...
    

public static boolean birdHitsPipe(Bird bird, Pipe pipe) {



        if (bird == null || pipe == null) return false;

        Rectangle birdRect = safeBounds(bird.getBounds());
        Rectangle topRect = safeBounds(pipe.getTopBounds());
        Rectangle bottomRect = safeBounds(pipe.getBottomBounds());

        return birdRect.intersects(topRect) || birdRect.intersects(bottomRect);
    }



    public static Optional<Pipe> birdHitsAnyPipe(Bird bird, List<Pipe> pipes) {
        if (bird == null || pipes == null || pipes.isEmpty()) return Optional.empty();




        for (Pipe pipe : pipes) {
            if (pipe == null) continue;
            if (birdHitsPipe(bird, pipe)) return Optional.of(pipe);
        }



        return Optional.empty();



    }
    private static Rectangle safeBounds(Rectangle r) {
        if (r == null) return new Rectangle(0, 0, 0, 0);



        return r;
    }
}