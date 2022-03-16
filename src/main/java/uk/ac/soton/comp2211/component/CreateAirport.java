package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class CreateAirport extends VBox {

    public CreateAirport(Stage dialog) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(new Text("Create Airport:"));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        buttonBox.getChildren().add(cancel);
        buttonBox.setAlignment(Pos.CENTER);
        
        this.getChildren().addAll(titleBox, buttonBox);
    }

}
