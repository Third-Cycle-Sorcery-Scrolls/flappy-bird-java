package src;

import java.util.ArrayDeque;


public class PipePool {
    private final ArrayDeque<Pipe> pool = new ArrayDeque<>();

    // public PipePool() {
    // }

    //We aquire a pipe or create new if empty
    public Pipe acquire(int startX, int topY, int width, int height, int vGap, int speed){
        if (pool.isEmpty()) {
            return new Pipe(startX, topY, width, height, vGap, speed);
        } else {
            Pipe p = pool.pop();
            p.reset(startX, topY, width, height, vGap, speed);
            return p;
        }
    }

    
    //Release a pipe back to the pool for reuse.
    public void release(Pipe p) {
        pool.push(p);
    }

    public int size() {
        return pool.size();
    }
}
