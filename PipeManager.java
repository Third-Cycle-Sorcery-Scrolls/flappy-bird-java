import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PipeManager{
    private final int pipeSpawnInterval;
    private final int pipeGap;
    private static int screenHeight = 600;
    private static int groundHeight = 100;

    private List<Pipe> pipes;
    private long lastPipeTime;
    private int pipeSpawnX;

    public PipeManager(){
        this(1000, 200);
    }
    public PipeManager(int pipeSpawnInterval, int pipeGap){
        this.pipeSpawnInterval = pipeSpawnInterval;
        this.pipeGap = pipeGap;
        this.pipes = new ArrayList<>();
        this.lastPipeTime = System.currentTimeMillis();
        this.pipeSpawnX = 800;
    }
    public void update(){
        long now = System.currentTimeMillis();

        if(now - lastPipeTime >= pipeSpawnInterval){
            spawnPipe();
            lastPipeTime = now;
        } 

        for(Pipe pipe: pipes){
            pipe.update();
        }

        removeOffScreenPipes();
    }
    private void spawnPipe(){
        Pipe newPipe = new Pipe(pipeSpawnX, screenHeight, groundHeight);
        pipes.add(newPipe);
    }
    private void removeOffScreenPipes(){
        pipes.removeIf(Pipe::isOffScreen);
    }
    public void reset(){
        pipes.clear();
        lastPipeTime = System.currentTimeMillis();
    }
    public boolean checkPipePassed(int birdX){
        for(Pipe pipe: pipes){
            if(pipe.isPassed(birdX)){
                return true;
            }
        }
        return false;
    }
    public List<Pipe> getPipes(){
        return pipes;
    }
    public void draw(Graphics2D g2d){
        for(Pipe pipe: pipes){
            pipe.draw(g2d);
        }
    }
    public int getNextPipeGapY(){
        if(!pipes.isEmpty()){
            return pipes.get(0).getGapY();
        }
        return screenHeight / 2;
    }
}