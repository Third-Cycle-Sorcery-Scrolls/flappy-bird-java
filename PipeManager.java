
import java.util.ArrayList;

public class PipeManager{
    private static int pipeSpawnInterval;
    private static int pipeGap;
    private ArrayList<Pipe> pipes;

    public PipeManager(){
        pipeSpawnInterval = 1000;
        pipeGap = 200;
        pipes = new ArrayList<>();
    }
    public PipeManager(int pipeSpawnInterval, int pipeGap){
        PipeManager.pipeSpawnInterval = pipeSpawnInterval;
        PipeManager.pipeGap = pipeGap;
    }
    private void spawnPipe(){
        Pipe newPipe = new Pipe(/* arguments */);
        pipes.add(newPipe);
    }
    private void removePipe(){
        for(Pipe pipe : pipes){
            // Delete if offscreen
            if(/* */){
                pipes.remove(pipe);
            }
        }
    }
    public void reset(){
        pipes.clear();
    }
}