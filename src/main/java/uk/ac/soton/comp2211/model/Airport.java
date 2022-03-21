package uk.ac.soton.comp2211.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Airport {
    private String name;
    private Tarmac[] tarmacs;

    public Airport(String _name, Tarmac[] _tarmacs) {
        name = _name;
        tarmacs = _tarmacs;
    }

    public String getName() { return name; }

    public Tarmac[] getTarmacs() { return tarmacs; }
    public Runway[] getRunways() {
        List<Runway> runways = new ArrayList<Runway>();

        for (Tarmac tarmac : tarmacs) {
            runways.addAll(Arrays.asList(tarmac.getRunways()));
        }   
        return runways.toArray(new Runway[0]);
    }
}
