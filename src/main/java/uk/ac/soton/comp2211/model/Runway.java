package uk.ac.soton.comp2211.model;

import java.util.ArrayList;
import java.util.List;

public class Runway {
    private String runwayDesignator;
    
    private RunwayValues originalValues;
    private RunwayValues currentValues;

    private List<Obstacle> obstacles;
    
    public Runway(String _runwayDesignator, RunwayValues _runwayValues) {
        runwayDesignator = _runwayDesignator;

        originalValues = _runwayValues;
        currentValues = _runwayValues;

        obstacles = new ArrayList<Obstacle>();
    }

    public RunwayValues getOriginalValues() { return originalValues; }
    public RunwayValues getCRunwayValues() { return currentValues; }

    public void addObstacle(Obstacle _newObstacle) { obstacles.add(_newObstacle); }

    public void removeObstacle(Obstacle _existingObstacle) { obstacles.remove(_existingObstacle); }
    public void removeObstacle(int _index) { obstacles.remove(_index); }

    public List<Obstacle> getObstacles() { return obstacles; }

    public void calculateValues() { System.out.println("Caculating Values!"); }
}