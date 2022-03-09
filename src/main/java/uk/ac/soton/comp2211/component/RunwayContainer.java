package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
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
    
    private final Runway runway;
    
    public RunwayContainer(Runway runway) {
        super();
        
        this.runway = runway;
        
        this.runwayView = new RunwayView();
        
        HBox dataBox = new HBox();
        this.parameterBox = new ParameterBox(runway.getOriginalValues());
        this.parameterBox.setPadding(new Insets(0, 10, 10, 10));
        
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
        recalculate.setOnAction(this::recalculate);
        recalculateBox.getChildren().add(recalculate);

        HBox.setHgrow(this.parameterBox, Priority.ALWAYS);
        this.parameterBox.setStyle("-fx-border-color: black");
        HBox.setHgrow(obstacleBox, Priority.ALWAYS);
        obstacleBox.setStyle("-fx-border-color: black");
        HBox.setHgrow(recalculateBox, Priority.ALWAYS);
        recalculateBox.setStyle("-fx-border-color: black");


        obstacleBox.setPadding(new Insets(10, 10, 10, 10));
        recalculateBox.setPadding(new Insets(10, 10, 10, 10));
        
        dataBox.getChildren().addAll(obstacleBox, recalculateBox);
        
        // add runway view later
        this.getChildren().addAll(new Label(this.runway.getRunwayDesignator()), this.runwayView, this.parameterBox, dataBox);
        this.setStyle("-fx-border-color: black;");
        this.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Perform the backend calculation and update this container.
     * @param event event
     */
    public void recalculate(ActionEvent event) {
        this.runway.recalculate(300);
        this.parameterBox.updateValues(this.runway.getCurrentValues());
    }
    
}
