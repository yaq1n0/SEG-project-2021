package uk.ac.soton.comp2211.model;

public class RunwayValues {
    private int tora;
    private int toda;
    private int asda;
    private int lda;

    public RunwayValues(int _tora, int _toda, int _asda, int _lda) {
        tora = _tora;
        toda = _toda;
        asda = _asda;
        lda = _lda;
    }

    public void setTORA(int _tora) { toda = _tora; }
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

    public RunwayValues clone() { return new RunwayValues(toda, toda, asda, lda); }
    public void copy(RunwayValues _runway) {
        tora = _runway.getTORA();
        toda = _runway.getTODA();
        asda = _runway.getASDA();
        lda = _runway.getLDA();
    }
}
