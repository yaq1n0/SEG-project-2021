package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.event.InsertObstacleListener;
import uk.ac.soton.comp2211.event.ObstacleClearListener;
import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.Position;

public class ObstacleBox extends VBox {

    protected static final Logger logger = LogManager.getLogger(ObstacleBox.class);

    private final Label obsLabel;
    private final Label lenLabel;
    private final Label widLabel;
    private final Label heightLabel;
    private final Label centerLabel;
    private final Label eDistLabel;
    private final Label wDistLabel;
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
            this.centerLabel = new Label();
            this.eDistLabel = new Label();
            this.wDistLabel = new Label();
            
            try {
                Position pos = obstacle.getPosition();
                this.centerLabel.setText("Centre line Displacement: " + pos.getCentreLineDisplacement());
                this.eDistLabel.setText("Distance from East: " + pos.getDistanceFromEast());
                this.wDistLabel.setText("Distance from West: " + pos.getDistanceFromWest());
            } catch (PositionException pe) {
                logger.error(pe.getStackTrace());
                this.centerLabel.setText("Error");
                this.eDistLabel.setText("Error");
                this.wDistLabel.setText("Error");
            }
        } else {
            this.obsButton.setText("Add Obstacle");
            this.obsLabel = new Label("Obstacle: ");
            this.lenLabel = new Label("Length: ");
            this.widLabel = new Label("Width: ");
            this.heightLabel = new Label("Height: ");
            this.centerLabel = new Label("Centre line Displacement: ");
            this.eDistLabel = new Label("Distance from East: ");
            this.wDistLabel = new Label("Distance from West: ");
        }
        
        VBox leftPart = new VBox();
        VBox rightPart = new VBox();
        leftPart.getChildren().addAll(obsLabel, lenLabel, widLabel, heightLabel);
        rightPart.getChildren().addAll(centerLabel, eDistLabel, wDistLabel);
        
        obsButton.setOnAction((ActionEvent event) -> {
            if (this.insertObstacleListener != null) {
                this.insertObstacleListener.openObstacleDialogue();
            }
        });
        Button clearObs = new Button("Remove Obstacle");
        clearObs.setOnAction(this::clearObstacle);
        
        leftPart.getChildren().add(obsButton);
        rightPart.getChildren().add(clearObs);
        HBox.setHgrow(leftPart, Priority.ALWAYS);
        HBox.setHgrow(rightPart, Priority.ALWAYS);
        HBox partBox = new HBox();
        partBox.getChildren().addAll(leftPart, rightPart);
        this.getChildren().add(partBox);
    }

    private void clearObstacle(ActionEvent actionEvent) {
        this.obsButton.setText("Add Obstacle");
        this.obsLabel.setText("Obstacle: ");
        this.lenLabel.setText("Length: ");
        this.widLabel.setText("Width: ");
        this.heightLabel.setText("Height: ");
        this.centerLabel.setText("Centre line Displacement: ");
        this.eDistLabel.setText("Distance from East: ");
        this.wDistLabel.setText("Distance from West: ");
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
        try {
            Position pos = obstacle.getPosition();
            this.centerLabel.setText("Centre line Displacement: " + pos.getCentreLineDisplacement());
            this.eDistLabel.setText("Distance from East: " + pos.getDistanceFromEast());
            this.wDistLabel.setText("Distance from West: " + pos.getDistanceFromWest());
        } catch (PositionException pe) {
            logger.error(pe.getStackTrace());
            this.centerLabel.setText("Error");
            this.eDistLabel.setText("Error");
            this.wDistLabel.setText("Error");
        }
    }
}
