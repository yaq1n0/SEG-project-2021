package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.event.InsertObstacleListener;
import uk.ac.soton.comp2211.event.ObstacleReturnListener;
import uk.ac.soton.comp2211.event.ObstacleClearListener;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.Position;

public class ObstacleBox extends VBox {

    private final Label obsLabel;
    private final Label lenLabel;
    private final Label widLabel;
    private final Label heightLabel;
    private final Label posLabel;
    private final Button obsButton;
    private InsertObstacleListener insertObstacleListener;
    private ObstacleClearListener obstacleClearListener;
    
    public ObstacleBox(Obstacle obstacle) {
        super();

        this.obsButton = new Button();
        
        if (obstacle != null) {
            this.obsButton.setText("Change Obstacle");
            this.obsLabel = new Label("Obstacle: " + obstacle.getName());
            this.lenLabel = new Label("Length: " + obstacle.getLength());
            this.widLabel = new Label("Width: " + obstacle.getWidth());
            this.heightLabel = new Label("Height: " + obstacle.getHeight());
            Position pos = obstacle.getPosition();
            this.posLabel = new Label("Position: (" + pos.getX() + ", " + pos.getY() + ")");
        } else {
            this.obsButton.setText("Add Obstacle");
            this.obsLabel = new Label("Obstacle: ");
            this.lenLabel = new Label("Length: ");
            this.widLabel = new Label("Width: ");
            this.heightLabel = new Label("Height: ");
            this.posLabel = new Label("Position: ");
        }

        this.getChildren().addAll(this.obsLabel, this.lenLabel, this.widLabel, this.heightLabel, this.posLabel);
        
        obsButton.setOnAction((ActionEvent event) -> {
            if (this.insertObstacleListener != null) {
                this.insertObstacleListener.openObstacleDialogue();
            }
        });
        Button clearObs = new Button("Remove Obstacle");
        clearObs.setOnAction(this::clearObstacle);
        this.getChildren().addAll(obsButton, clearObs);
        
    }

    private void clearObstacle(ActionEvent actionEvent) {
        this.obsButton.setText("Add Obstacle");
        this.obsLabel.setText("Obstacle: ");
        this.lenLabel.setText("Length: ");
        this.widLabel.setText("Width: ");
        this.heightLabel.setText("Height: ");
        this.posLabel.setText("Position: ");
        if (obstacleClearListener != null) {
            obstacleClearListener.reset();
        }
    }
    
    public void setInsertObstacleListener(InsertObstacleListener listener) {
        this.insertObstacleListener = listener;
    }
    
    public void setObstacleClearListener(ObstacleClearListener listener) {
        this.obstacleClearListener = listener;
    }
    
    public void update(Obstacle obstacle) {
        this.obsButton.setText("Change Obstacle");
        this.obsLabel.setText("Obstacle: " + obstacle.getName());
        this.lenLabel.setText("Length: " + obstacle.getLength());
        this.widLabel.setText("Width: " + obstacle.getWidth());
        this.heightLabel.setText("Height: " + obstacle.getHeight());
        Position pos = obstacle.getPosition();
        this.posLabel.setText("Position: (" + pos.getX() + ", " + pos.getY() + ")");
    }
}
