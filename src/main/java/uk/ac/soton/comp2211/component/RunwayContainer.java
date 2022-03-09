package uk.ac.soton.comp2211.component;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.model.Obstacle;
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
        
        this.runwayView = new RunwayView();
        
        HBox dataBox = new HBox();
        this.parameterBox = new ParameterBox(runway.getCurrentValues());
        
        // get obstacles needs to change for multiple obstacles
        ObstacleBox obstacleBox;
        try {
            Obstacle obs = runway.getTarmac().getObstacle();
            obstacleBox = new ObstacleBox(new Obstacle[] {obs});
        } catch (NullPointerException npe) {
            obstacleBox = new ObstacleBox(new Obstacle[] {});
        }
        
        VBox recalculateBox = new VBox();
        Button recalculate = new Button("Run Calculation");
        recalculateBox.getChildren().add(recalculate);

        HBox.setHgrow(this.parameterBox, Priority.ALWAYS);
        this.parameterBox.setStyle("-fx-border-color: black");
        HBox.setHgrow(obstacleBox, Priority.ALWAYS);
        obstacleBox.setStyle("-fx-border-color: black");
        HBox.setHgrow(recalculateBox, Priority.ALWAYS);
        recalculateBox.setStyle("-fx-border-color: black");
        
        dataBox.getChildren().addAll(obstacleBox, recalculateBox);
        
        // add runway view later
        this.getChildren().addAll(this.runwayView, this.parameterBox, dataBox);
        this.setStyle("-fx-border-color: black;");
        this.setPadding(new Insets(10, 10, 10, 10));
    }
    
}
