package uk.ac.soton.comp2211.model;

public class RunwayValues {
    private int tora;
    private int toda;
    private int asda;
    private int lsa;
    private int displacedThreshold;

    public RunwayValues(int _tora, int _toda, int _asda, int _lsa, int _displacedThreshold) {
        tora = _tora;
        toda = _toda;
        asda = _asda;
        lsa = _lsa;
        displacedThreshold = _displacedThreshold;
    }

    public void setTORA(int _tora) { toda = _tora; }
    public void setTODA(int _toda) { toda = _toda; }
    public void setASDA(int _asda) { asda = _asda; }
    public void setLSA(int _lsa) { lsa = _lsa; }
    public void setDisplacedThreshold(int _displacedThreshold) { displacedThreshold = _displacedThreshold; }

    public int getTORA() { return tora; }
    public int getTODA() { return toda; }
    public int getASDA() { return asda; }
    public int getLSA() { return lsa; }
    public int getDisplacedThreshold() { return displacedThreshold; }

    public RunwayValues clone() { return new RunwayValues(toda, toda, asda, lsa, displacedThreshold); }
}
