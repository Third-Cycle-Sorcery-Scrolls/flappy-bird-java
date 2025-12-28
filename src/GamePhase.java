package src;

public enum GamePhase {
    // Parameters: phaseNumber, duration(ms), hGap, vGap, pipeSpeed, gravity, isDark, phaseName
    PHASE_1(1, 20000, 1500, 250, -4, 1, false, "Normal"),
    PHASE_2(2, 20000, 800, 400, -4, 1, false, "Low H-Gap, High V-Gap"),
    PHASE_3(3, 20000, 2500, 120, -4, 1, false, "High H-Gap, Low V-Gap"),
    PHASE_4(4, 20000, 1500, 250, -4, 1, true, "Sudden Darkness"),
    PHASE_5(5, 20000, 800, 400, -8, 1, false, "Low H-Gap, High V-Gap, High Speed"),
    PHASE_6(6, 20000, 2500, 120, -4, 2, false, "High H-Gap, Low V-Gap, High Gravity"),
    PHASE_7(7, 20000, 1500, 250, -8, 1, false, "Normal High Speed");
    
    private final int phaseNumber;
    private final int duration; // milliseconds
    private final int hGap;     // horizontal gap between pipe pairs
    private final int vGap;     // vertical gap within a pipe pair
    private final int pipeSpeed;
    private final int gravity;
    private final boolean isDark;
    private final String phaseName;
    
    GamePhase(int phaseNumber, int duration, int hGap, int vGap, int pipeSpeed, 
              int gravity, boolean isDark, String phaseName) {
        this.phaseNumber = phaseNumber;
        this.duration = duration;
        this.hGap = hGap;
        this.vGap = vGap;
        this.pipeSpeed = pipeSpeed;
        this.gravity = gravity;
        this.isDark = isDark;
        this.phaseName = phaseName;
    }
    
    // Getters
    public int getPhaseNumber() { return phaseNumber; }
    public int getDuration() { return duration; }
    public int getHGap() { return hGap; }
    public int getVGap() { return vGap; }
    public int getPipeSpeed() { return pipeSpeed; }
    public int getGravity() { return gravity; }
    public boolean isDark() { return isDark; }
    public String getPhaseName() { return phaseName; }
}