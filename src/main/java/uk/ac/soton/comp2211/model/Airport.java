package uk.ac.soton.comp2211.model;

public class Airport {
    private String name;
    private Runway[] runways;

    public Airport(String _name, Runway[] _runways) {
        name = _name;
        runways = _runways;
    }

    public String getName() { return name; }

    public Runway getRunway(int _index) { return runways[_index]; }
    public Runway[] getRunways() { return runways; }
}
