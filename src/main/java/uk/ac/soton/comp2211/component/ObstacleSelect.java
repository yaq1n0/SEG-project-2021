package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.event.PassObstacleListener;
import uk.ac.soton.comp2211.model.Obstacle;

/**
 * Component that allows the user to select an airport to display.
 */
public class ObstacleSelect extends VBox {

    private PassObstacleListener passObstacleListener;

    public ObstacleSelect(Stage dialog, Obstacle[] obstacles) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(new Text("Obstacle Presets:"));

        this.getChildren().add(titleBox);
    
        for (Obstacle obs : obstacles) {
            HBox obsBox = new HBox();
            Button obsButton = new Button(obs.getName());
            obsButton.setOnAction((ActionEvent event) -> {
                this.passObstacleListener.passAirport(obs);
            });
            obsBox.getChildren().addAll(obsButton);
            this.getChildren().add(obsBox);
        }

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        buttonBox.getChildren().add(cancel);
        buttonBox.setAlignment(Pos.CENTER);
        this.getChildren().add(buttonBox);
    }

    /**
     * Set the pass obstacle listener which will accept obstacle choice to insert.
     * @param listener listener
     */
    public void setPassObstacleListener(PassObstacleListener listener) {
        this.passObstacleListener = listener;
    }
}
