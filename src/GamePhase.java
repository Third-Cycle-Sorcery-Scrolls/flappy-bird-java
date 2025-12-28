package src;

public enum GamePhase {
    // Parameters: phaseNumber, duration(ms), hGap, vGap, pipeSpeed, gravity, jumpForce, scoring, isDark, phaseName
    PHASE_1(1, 15000, 1200, 250, -4.0f, 1.0f, -9.0f, 1, false, "Beginner"),
    PHASE_2(2, 15000, 800, 350, -4.0f, 1.0f, -10.0f, 1, false, "Compression 1"),
    PHASE_3(3, 15000, 2000, 130, -4.0f, 1.0f, -9.0f, 1, false, "Needles 1"),
    PHASE_4(4, 10000, 1200, 250, -4.0f, 1.0f, -9.0f, 1,true, "Black Out 1!"),
    PHASE_5(5, 15000, 800, 300, -6.0f, 1.0f, -9.0f, 2,false, "Overdrive 1"),
    PHASE_6(6, 10000, 2000, 150, -4.0f, 1.5f, -9.0f, 4, false, "Heavy Wings 1"),
    PHASE_7(7, 15000, 1000, 250, -8.0f, 1.0f, -9.0f, 2, false, "Velocity 1"),
    PHASE_8(8, 10000, 1100, 230, -4.0f, 1.0f, -9.0f, 2, false, "Beginner 2"),
    PHASE_9(9, 15000, 770, 320, -4.0f, 1.0f, -9.0f, 2, false, "Compression 1");
   
    private final int phaseNumber;
    private final int duration; // milliseconds
    private final int hGap;     // horizontal gap between pipe pairs
    private final float vGap;     // vertical gap within a pipe pair
    private final float pipeSpeed;
    private final float gravity;
    private final float jumpForce;
    private final int baseScore;
    private final boolean isDark;
    private final String phaseName;
    
    GamePhase(int phaseNumber, int duration, int hGap, float vGap, float pipeSpeed, 
              float gravity, float jumpForce, int baseScore, boolean isDark, String phaseName) {
        this.phaseNumber = phaseNumber;
        this.duration = duration;
        this.hGap = hGap;
        this.vGap = vGap;
        this.pipeSpeed = pipeSpeed;
        this.gravity = gravity;
        this.baseScore = baseScore;
        this.jumpForce = jumpForce;
        this.isDark = isDark;
        this.phaseName = phaseName;
    }
    
    // Getters
    public int getPhaseNumber() { return phaseNumber; }
    public int getDuration() { return duration; }
    public int getHGap() { return hGap; }
    public float getVGap() { return vGap; }
    public float getPipeSpeed() { return pipeSpeed; }
    public float getGravity() { return gravity; }
    public float getJumpForce() {
        return jumpForce;
    }
    public int getBaseScore() {
        return baseScore;
    }
    public boolean isDark() { return isDark; }
    public String getPhaseName() { return phaseName; }
}