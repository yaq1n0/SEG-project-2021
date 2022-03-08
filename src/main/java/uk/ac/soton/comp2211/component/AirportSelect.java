package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.event.PassAirportListener;

/**
 * Component that allows the user to select an airport to display.
 */
public class AirportSelect extends VBox {
    
    private PassAirportListener passAirportListener;
    
    public AirportSelect(Stage dialog, String[][] airports) {
        super(20);
        
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(new Text("Open Airport:"));

        this.getChildren().add(titleBox);
        
        for (String[] airport : airports) {
            HBox airportBox = new HBox();
            Button airportButton = new Button(airport[1]);
            airportButton.setOnAction((ActionEvent event) -> {
                this.passAirportListener.passAirport(airport[0]);
            });
            Label path = new Label(airport[0]);
            airportBox.getChildren().addAll(airportButton, path);
            this.getChildren().add(airportBox);
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
     * Set the pass airport listener which will accept airport choice.
     * @param listener listener
     */
    public void setPassAirportListener(PassAirportListener listener) {
        this.passAirportListener = listener;
    }
}
