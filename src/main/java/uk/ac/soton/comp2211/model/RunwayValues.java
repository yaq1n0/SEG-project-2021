package uk.ac.soton.comp2211.model;

public class RunwayValues {
    private int tora;
    private int toda;
    private int asda;
    private int lda;
    private int stopway;
    private int clearway;

    public RunwayValues(int _tora, int _toda, int _asda, int _lda) {
        tora = _tora;
        toda = _toda;
        asda = _asda;
        lda = _lda;
        updateClearwayStopway();
    }

    private void updateClearwayStopway() {
        if (asda - tora > 0) {
            stopway = asda - tora;
        }
        if (toda - tora > 0) {
            clearway = toda - tora;
        }
    }

    public void setTORA(int _tora) { tora = _tora; }
    public void setTODA(int _toda) { toda = _toda; }
    public void setASDA(int _asda) { asda = _asda; }
    public void setLDA(int _lda) { lda = _lda; }
    public void setValues(int _tora, int _toda, int _asda, int _lda) {
        tora = _tora;
        toda = _toda;
        asda = _asda;
        lda = _lda;
    }

    public int getTORA() { return tora; }
    public int getTODA() { return toda; }
    public int getASDA() { return asda; }
    public int getLDA() { return lda; }

    public int getClearway() {
        updateClearwayStopway();
        return clearway;
    }
    public int getStopway() {
        updateClearwayStopway();
        return stopway;
    }


    public RunwayValues clone() { return new RunwayValues(tora, toda, asda, lda); }
    public void copy(RunwayValues _runway) {
        tora = _runway.getTORA();
        toda = _runway.getTODA();
        asda = _runway.getASDA();
        lda = _runway.getLDA();
    }
}
