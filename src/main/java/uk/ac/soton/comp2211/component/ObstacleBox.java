package uk.ac.soton.comp2211.component;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.model.Obstacle;

public class ObstacleBox extends VBox {
    
    public ObstacleBox(Obstacle[] obstacles) {
        super();

        Label label = new Label("Obstacles:");
        this.getChildren().add(label);
        for (Obstacle obs : obstacles) {
            Label obsLabel = new Label(obs.getName() + " at (x, y) add data beneath to input + customise");
            this.getChildren().add(obsLabel);
        }
    }
}
