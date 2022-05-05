package uk.ac.soton.comp2211.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Airport {
    private String name;
    private List<Tarmac> tarmacs;
    private File dataFile;

    public Airport(String _name, Tarmac[] _tarmacs, File _dataFile) {
        name = _name;

        tarmacs = new ArrayList<Tarmac>();
        for (Tarmac tarmac : _tarmacs) tarmacs.add(tarmac);

        dataFile = _dataFile;
    }

    /**
     * Returns the name of the airport.
     */
    public String getName() { return name; }

    /**
     * Returns a list of all tarmacs on the airport.
     */
    public List<Tarmac> getTarmacs() { return tarmacs; }

    /**
     * Returns the airport data file.
     */
    public File getDataFile() { return dataFile; }

    /**
     * Collects all the runways from each tarmacs and returns the list.
     */
    public Runway[] getRunways() {
        List<Runway> runways = new ArrayList<Runway>();
        for (Tarmac tarmac : tarmacs) { runways.addAll(Arrays.asList(tarmac.getRunways())); }
        return runways.toArray(new Runway[0]);
    }
}
