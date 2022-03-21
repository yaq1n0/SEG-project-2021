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
import uk.ac.soton.comp2211.event.PassAirportListener;
import uk.ac.soton.comp2211.model.SystemModel;

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
            if (!airport[1].equals("[ERROR] Broken file!")) {
                HBox airportBox = new HBox();
                Button airportButton = new Button(airport[1]);
                airportButton.setOnAction((ActionEvent event) -> {
                    System.out.println(airport[0]);
                    this.passAirportListener.passAirport(airport[0]);
                    dialog.close();
                });
                Label path = new Label(airport[0]);
                HBox.setHgrow(path, Priority.ALWAYS);
                path.setAlignment(Pos.CENTER);
                airportBox.getChildren().addAll(airportButton, path);
                this.getChildren().add(airportBox);
            }
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
