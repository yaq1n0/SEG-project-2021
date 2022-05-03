package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.event.*;
import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.exceptions.SizeException;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.Position;

/**
 * Component that obstacle controls (add, remove, set size, set pos, recalculate, show steps, save steps)
 */
public class ObstacleBox extends VBox{

    protected static final Logger logger = LogManager.getLogger(ObstacleBox.class);

    private final Label obsLabel;
    private final Label lenLabel;
    private final Label widLabel;
    private final Label heightLabel;
    private final NumberField centerField;
    private final NumberField tDistField;
    private final Button obsButton;
    private InsertObstacleListener insertObstacleListener;
    private ObstacleClearListener obstacleClearListener;

    private RecalculateListener recalculateListener;
    private ShowStepsListener showStepsListener;
    private LogStepsListener logStepsListener;
    private Obstacle obstacle;

    public ObstacleBox(Obstacle obstacle) {
        super();

        this.obstacle = obstacle;

        this.obsButton = new Button();
        Label centerLabel = new Label("Centre line Displacement: ");
        Label eDistLabel = new Label("Distance from Threshold: ");
        this.centerField = new NumberField(-5000, 5000);
        this.tDistField = new NumberField(-5000, 5000);

        if (obstacle != null) {
            enablePositionFields();
            this.obsButton.setText("Change Obstacle");
            this.obsLabel = new Label("Obstacle: " + obstacle.getName());
            this.lenLabel = new Label("Length: " + obstacle.getLength());
            this.widLabel = new Label("Width: " + obstacle.getWidth());
            this.heightLabel = new Label("Height: " + obstacle.getHeight());

            try {
                Position pos = obstacle.getPosition();
                this.centerField.setText("" + pos.getCentreLineDisplacement());
                this.tDistField.setText("" + pos.getDistance());
            } catch (PositionException pe) {
                logger.error(pe.getStackTrace());
            }
        } else {
            disablePositionFields();
            this.obsButton.setText("Add Obstacle");
            this.obsLabel = new Label("Obstacle: ");
            this.lenLabel = new Label("Length: ");
            this.widLabel = new Label("Width: ");
            this.heightLabel = new Label("Height: ");
        }

        VBox leftPart = new VBox();
        VBox rightPart = new VBox();
        HBox centerPair = new HBox();
        centerPair.getChildren().addAll(centerLabel, centerField);
        HBox tDistPair = new HBox();
        tDistPair.getChildren().addAll(eDistLabel, tDistField);
        leftPart.getChildren().addAll(obsLabel, lenLabel, widLabel, heightLabel);
        rightPart.getChildren().addAll(centerPair, tDistPair);

        obsButton.setOnAction((ActionEvent event) -> {
            if (this.insertObstacleListener != null) {
                this.insertObstacleListener.openObstacleDialogue();
            }
        });

        Button recalculate = new Button("Recalculate");
        recalculate.setOnAction(this::recalculate);

        Button showSteps = new Button("Show Steps");
        showSteps.setOnAction(this::recalculateSteps);

        //TODO: link this to the SystemModel.recordCalculation method
        Button logSteps = new Button("Log Steps");
        logSteps.setOnAction(this::recalculateLog);

        Button clearObs = new Button("Remove Obstacle");
        clearObs.setOnAction(this::clearObstacle);

        leftPart.getChildren().add(obsButton);
        rightPart.getChildren().addAll(recalculate, showSteps, logSteps, clearObs);
        HBox.setHgrow(leftPart, Priority.ALWAYS);
        HBox.setHgrow(rightPart, Priority.ALWAYS);
        HBox partBox = new HBox();
        partBox.getChildren().addAll(leftPart, rightPart);
        this.getChildren().add(partBox);
    }

    private void recalculate(ActionEvent actionEvent) {
        if (validateParams()) {
            recalculateListener.recalculate();
        }
    }

    private void recalculateSteps(ActionEvent actionEvent) {
        if (validateParams()) {
            showStepsListener.showSteps();
        }
    }

    private void recalculateLog(ActionEvent actionEvent) {
        if (validateParams()) {
            logStepsListener.logSteps();
        }
    }

    private boolean validateParams() {
        int center, thresh;
        boolean valid = false;

        try {
            center = Integer.parseInt(this.centerField.getText());
            thresh = Integer.parseInt(this.tDistField.getText());
            
            if (this.obstacle == null) {
                throw new PositionException(logger, "Can't change position of null obstacle.");
            }
            
            try {
                this.obstacle.setPosition(new Position(thresh, center));
                valid = true;
                logger.info("User set obstacle position.");
            } catch (PositionException | SizeException pe) {
                logger.error("Invalid parameters to Position constructor.");
            }

        } catch (NumberFormatException | PositionException nfe) {
            this.centerField.clear();
            this.tDistField.clear();
        }

        return valid;
    }

    private void clearObstacle(ActionEvent actionEvent) {
        this.obsButton.setText("Add Obstacle");
        this.obsLabel.setText("Obstacle: ");
        this.lenLabel.setText("Length: ");
        this.widLabel.setText("Width: ");
        this.heightLabel.setText("Height: ");
        this.centerField.clear();
        this.tDistField.clear();
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

    public void setRecalculateListener(RecalculateListener recalculateListener) {
        this.recalculateListener = recalculateListener;
    }

    public void setShowStepsListener(ShowStepsListener showStepsListener) {
        this.showStepsListener = showStepsListener;
    }

    public void setLogStepsListener(LogStepsListener logStepsListener) {
        this.logStepsListener = logStepsListener;
    }

    public void update(Obstacle obstacle) {
        if (obstacle != null) {
            enablePositionFields();
            this.obstacle = obstacle;
            this.obsButton.setText("Change Obstacle");
            this.obsLabel.setText("Obstacle: " + obstacle.getName());
            this.lenLabel.setText("Length: " + obstacle.getLength());
            this.widLabel.setText("Width: " + obstacle.getWidth());
            this.heightLabel.setText("Height: " + obstacle.getHeight());
            try {
                Position pos = obstacle.getPosition();
                this.centerField.setText("" + pos.getCentreLineDisplacement());
                this.tDistField.setText("" + pos.getDistance());
            } catch (PositionException pe) {
                logger.error(pe.getStackTrace());
                this.centerField.setText("");
                this.tDistField.setText("");
            }
        }
    }

    /**
     * Prevent the user from entering obstacle position data.
     */
    public void disablePositionFields() {
        this.centerField.setDisable(true);
        this.tDistField.setDisable(true);
    }

    /**
     * Allow the user to enter obstacle position data.
     */
    public void enablePositionFields() {
        this.centerField.setDisable(false);
        this.tDistField.setDisable(false);
    }
}
