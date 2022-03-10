package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.SystemModel;

/**
 * Component to contain data on each runway for an airport.
 */
public class RunwayContainer extends VBox {
    
    private final RunwayView runwayView;
    private final ParameterBox parameterBox;
    private ObstacleBox obstacleBox;
    
    private final Runway runway;
    
    public RunwayContainer(Runway runway, Stage stage) {
        super();
        
        this.runway = runway;
        
        this.runwayView = new RunwayView();
        
        HBox dataBox = new HBox();
        this.parameterBox = new ParameterBox(runway.getOriginalValues());
        this.parameterBox.setPadding(new Insets(0, 10, 10, 10));
        
        // get obstacles needs to change for multiple obstacles
        Obstacle obs = runway.getTarmac().getObstacle();
        this.obstacleBox = new ObstacleBox(obs);

        this.obstacleBox.setObstacleReturnListener(this::setObstacle);
        this.obstacleBox.setInsertObstacleListener(() -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);

            ObstacleSelect obstacleSelect = new ObstacleSelect(dialog, SystemModel.getObstacles());
            obstacleSelect.setPassObstacleListener(this::setObstacle);

            Scene dialogScene = new Scene(obstacleSelect, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        });
        this.obstacleBox.setObstacleClearListener(this::clearObstacle);
        
        VBox recalculateBox = new VBox();
        Button recalculate = new Button("Run Calculation");
        recalculate.setOnAction(this::recalculate);
        recalculateBox.getChildren().add(recalculate);

        HBox.setHgrow(this.parameterBox, Priority.ALWAYS);
        this.parameterBox.setStyle("-fx-border-color: black");
        HBox.setHgrow(obstacleBox, Priority.ALWAYS);
        this.obstacleBox.setStyle("-fx-border-color: black");
        HBox.setHgrow(recalculateBox, Priority.ALWAYS);
        recalculateBox.setStyle("-fx-border-color: black");


        this.obstacleBox.setPadding(new Insets(10, 10, 10, 10));
        recalculateBox.setPadding(new Insets(10, 10, 10, 10));
        
        dataBox.getChildren().addAll(this.obstacleBox, recalculateBox);
        
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
        this.parameterBox.resetValues();
        this.runway.recalculate(300);
        this.parameterBox.updateValues(this.runway.getCurrentValues());
    }

    /**
     * Set the tarmac obstacle.
     * @param obstacle obstacle
     */
    public void setObstacle(Obstacle obstacle) {
        this.parameterBox.resetValues();
        this.runway.getTarmac().setObstacle(obstacle);
        this.obstacleBox.update(obstacle);
    }

    /**
     * Reset the parameters of the parameter box.
     */
    public void clearObstacle() {
        this.runway.getTarmac().removeObstacle();
        this.parameterBox.resetValues();
    }
}
