package uk.ac.soton.comp2211.model;

public class RunwayValues {
    private int tora;
    private int toda;
    private int asda;
    private int lda;
    private int dt;
    private int stopway;
    private int clearway;

    public RunwayValues(int _tora, int _toda, int _asda, int _lda) {
        // TODO: validate that these values are positive and make sense
        // toda and asda must be >= tora
        // tora must be >= lda
        tora = _tora;
        toda = _toda; // tora + clearway
        asda = _asda; // tora + stopway
        lda = _lda;
        updateDerivedValues();
    }

    private void updateDerivedValues() {
        setStopway(getASDA() - getTORA());
        setClearway(getTODA() - getTORA());
        setDT(getTORA() - getLDA());
    }

    public int getTORA() { return tora; }
    public int getTODA() { return toda; }
    public int getASDA() { return asda; }
    public int getLDA() { return lda; }

    public int getDT() { return dt; }
    public int getStopway() { return stopway; }
    public int getClearway() { return clearway; }


    public void setTORA(int _tora) { tora = _tora; }
    public void setTODA(int _toda) { toda = _toda; }
    public void setASDA(int _asda) { asda = _asda; }
    public void setLDA(int _lda) { lda = _lda; }

    public void setDT(int _dt) { dt = _dt; }
    public void setStopway(int _stopway) { stopway = _stopway; }
    public void setClearway(int _clearway) { clearway = _clearway; }

    public void setValues(int _tora, int _toda, int _asda, int _lda) {
        setTORA(_tora);
        setTODA(_toda);
        setASDA(_asda);
        setLDA(_lda);
        updateDerivedValues();
    }

    public RunwayValues clone() { return new RunwayValues(tora, toda, asda, lda); }

    public void copy(RunwayValues _runway) {
        tora = _runway.getTORA();
        toda = _runway.getTODA();
        asda = _runway.getASDA();
        lda = _runway.getLDA();
        _runway.updateDerivedValues();
    }
}
