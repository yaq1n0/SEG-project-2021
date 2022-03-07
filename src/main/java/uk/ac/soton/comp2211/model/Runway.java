package uk.ac.soton.comp2211.model;

public class Runway {
    private String runwayDesignator;
    private Tarmac tarmac; // Corresponding physical runway.
    private RunwayValues originalValues;
    private RunwayValues currentValues;
    private int displacedThreshold;

    private int RESA = 240; // runway end safety area (meters)
    private int SEV = 60; // strip end value (meters)
    private int ALS = 50; // approach landing surface slope
    private int TOCS = 50; // take-off climb surface slope

    public Runway(String _runwayDesignator, Tarmac _tarmac, RunwayValues _originalValues, int _displacedThreshold) {
        runwayDesignator = _runwayDesignator;
        tarmac = _tarmac;
        originalValues = _originalValues;
        currentValues = _originalValues.clone();
        displacedThreshold = _displacedThreshold;
    }

    public String getRunwayDesignator() { return runwayDesignator; }
    public Tarmac getTarmac() { return tarmac; }
    public RunwayValues getOriginalValues() { return originalValues; }
    public RunwayValues getCurrentValues() { return currentValues; }
    public int getDisplacedThreshold() { return displacedThreshold; }

    public void recalculate(int blastAllowance) {
        Obstacle obs = tarmac.getObstacle();
        int obsLength = obs.getLength();
        int obsHeight = obs.getHeight();
        int obsPos = obs.getPosition().getX();

        if (obs != null) {
            if (obsPos <= tarmac.getLength() / 2) {
                //closer to start of runway, can replace tarmac.getLength() with originalValues.getTORA()

                // landing over obstacle
                int newLDA;
                int d1 = obsPos + (obsHeight * ALS) + SEV;
                int d2 = obsPos + RESA + SEV; // including obsLength?
                int d3 = obsPos + blastAllowance;
                newLDA = Math.max(d1, Math.max(d2, d3));
                currentValues.setLDA(currentValues.getLDA() - newLDA);

                // take off away from obstacle
                int d4 = obsPos + blastAllowance; // include obsLength?
                currentValues.setTORA(currentValues.getTORA() - d4);
                currentValues.setTODA(currentValues.getTODA() - d4);
                currentValues.setASDA(currentValues.getASDA() - d4);

            } else {
                //closer to end of runway

                //land towards obstacles
                currentValues.setLDA(currentValues.getLDA() - RESA - SEV);

                // take off towards it
                int d1 = obsPos - RESA - SEV;
                int d2 = obsPos - (obsHeight * TOCS);
                currentValues.setTORA(Math.min(d1, d2));
                // TODA = ASDA = TORA
                currentValues.setTODA(currentValues.getTORA());
                currentValues.setASDA(currentValues.getTORA());
            }
        }
    }
}