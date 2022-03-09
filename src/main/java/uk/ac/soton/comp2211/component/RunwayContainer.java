package uk.ac.soton.comp2211.component;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.model.Runway;

/**
 * Component to contain data on each runway for an airport.
 */
public class RunwayContainer extends VBox {
    
    private final RunwayView runwayView;
    private final ParameterBox parameterBox;
    
    public RunwayContainer(Runway runway) {
        super();
        //Label, RunwayView and ParameterBox
        
        Label runwayLabel = new Label(runway.getRunwayDesignator());
        this.runwayView = new RunwayView();
        this.parameterBox = new ParameterBox(runway.getCurrentValues());
        
        // add runway view later
        this.getChildren().addAll(runwayLabel, runwayView, this.parameterBox);
        this.setStyle("-fx-border-color: black;");
    }
    
}
