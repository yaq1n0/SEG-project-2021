package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Component that shows the user the recalculation steps
 */
public class ShowSteps extends VBox {
    public ShowSteps(Stage dialog, ArrayList<String> steps) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(new Text("recalculation steps "));

        this.getChildren().add(titleBox);

        for (String step : steps) {
            this.getChildren().add(new Text(step));
        }

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        Button exit = new Button("exit");
        exit.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        buttonBox.getChildren().add(exit);
        buttonBox.setAlignment(Pos.CENTER); // dunno why this is done twice, but it's done this way in ObstacleSelect
        this.getChildren().add(buttonBox);
    }
}
