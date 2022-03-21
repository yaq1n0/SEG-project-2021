package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.event.InsertObstacleListener;
import uk.ac.soton.comp2211.event.ObstacleClearListener;
import uk.ac.soton.comp2211.event.VerifyObstacleListener;
import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.Position;

public class ObstacleBox extends VBox {

    protected static final Logger logger = LogManager.getLogger(ObstacleBox.class);

    private final Label obsLabel;
    private final Label lenLabel;
    private final Label widLabel;
    private final Label heightLabel;
    private final TextField centerField;
    private final TextField eDistField;
    private final TextField wDistField;
    private final Button obsButton;
    private InsertObstacleListener insertObstacleListener;
    private ObstacleClearListener obstacleClearListener;
    private VerifyObstacleListener verifyObstacleListener;
    private final int runwayLength;
    private Obstacle obstacle;

    public ObstacleBox(Obstacle obstacle, int runwayLength) {
        super();

        this.obstacle = obstacle;
        this.runwayLength = runwayLength;

        this.obsButton = new Button();
        Label centerLabel = new Label("Centre line Displacement: ");
        Label eDistLabel = new Label("Distance from East: ");
        Label wDistLabel = new Label("Distance from West: ");
        this.centerField = new TextField();
        this.wDistField = new TextField();
        this.eDistField = new TextField();

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
                this.eDistField.setText("" + pos.getDistanceFromEast());
                this.wDistField.setText("" + pos.getDistanceFromWest());
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
        HBox eDistPair = new HBox();
        eDistPair.getChildren().addAll(eDistLabel, eDistField);
        HBox wDistPair = new HBox();
        wDistPair.getChildren().addAll(wDistLabel, wDistField);
        leftPart.getChildren().addAll(obsLabel, lenLabel, widLabel, heightLabel);
        rightPart.getChildren().addAll(centerPair, eDistPair, wDistPair);

        obsButton.setOnAction((ActionEvent event) -> {
            if (this.insertObstacleListener != null) {
                this.insertObstacleListener.openObstacleDialogue();
            }
        });

        Button save = new Button("Save");
        save.setOnAction(this::checkParams);

        Button clearObs = new Button("Remove Obstacle");
        clearObs.setOnAction(this::clearObstacle);

        leftPart.getChildren().add(obsButton);
        rightPart.getChildren().addAll(save, clearObs);
        HBox.setHgrow(leftPart, Priority.ALWAYS);
        HBox.setHgrow(rightPart, Priority.ALWAYS);
        HBox partBox = new HBox();
        partBox.getChildren().addAll(leftPart, rightPart);
        this.getChildren().add(partBox);
    }

    private void checkParams(ActionEvent actionEvent) {
        int center, east, west = 0;
        try {
            center = Integer.parseInt(this.centerField.getText());
            east = Integer.parseInt(this.eDistField.getText());

            // Gives east precedence over west, following block avoids the number format exception.
            if (wDistField.getText().length() != 0) {
                west = Integer.parseInt(this.wDistField.getText());
            }

            if (east + west != this.runwayLength) {
                this.wDistField.setText("" + (this.runwayLength - east));
                west = this.runwayLength - east;
            }
            // Error check position values
            if (east < 0 || west < 0 || east > runwayLength || west > runwayLength) {
                throw new PositionException("Position out of bounds.");
            }

            if (this.obstacle == null) {
                throw new PositionException("Can't change position of null obstacle.");
            }
            this.obstacle.setPosition(new Position(west, east, center));
            this.verifyObstacleListener.confirm();
            logger.info("User set obstacle position.");

        } catch (NumberFormatException | PositionException nfe) {
            this.centerField.clear();
            this.eDistField.clear();
            this.wDistField.clear();
        }
    }

    private void clearObstacle(ActionEvent actionEvent) {
        this.obsButton.setText("Add Obstacle");
        this.obsLabel.setText("Obstacle: ");
        this.lenLabel.setText("Length: ");
        this.widLabel.setText("Width: ");
        this.heightLabel.setText("Height: ");
        this.centerField.clear();
        this.eDistField.clear();
        this.wDistField.clear();
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

    public void setVerifyObstacleListener(VerifyObstacleListener listener) {
        this.verifyObstacleListener = listener;
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
                this.eDistField.setText("" + pos.getDistanceFromEast());
                this.wDistField.setText("" + pos.getDistanceFromWest());
                this.verifyObstacleListener.confirm();
            } catch (PositionException pe) {
                logger.error(pe.getStackTrace());
                this.centerField.setText("");
                this.eDistField.setText("");
                this.wDistField.setText("");
            }
        }
    }

    /**
     * Prevent the user from entering obstacle position data.
     */
    public void disablePositionFields() {
        this.centerField.setDisable(true);
        this.eDistField.setDisable(true);
        this.wDistField.setDisable(true);
    }

    /**
     * Allow the user to enter obstacle position data.
     */
    public void enablePositionFields() {
        this.centerField.setDisable(false);
        this.eDistField.setDisable(false);
        this.wDistField.setDisable(false);
    }
}
