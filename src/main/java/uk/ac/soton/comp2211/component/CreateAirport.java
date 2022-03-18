package uk.ac.soton.comp2211.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.model.Airport;
import uk.ac.soton.comp2211.model.SystemModel;
import uk.ac.soton.comp2211.model.Tarmac;


public class CreateAirport extends VBox {

    public CreateAirport(Stage dialog) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(new Text("Create Airport:"));

        VBox vboxTarmacs = new VBox();
        vboxTarmacs.getChildren().add(new TarmacVBox(1, 1));

        HBox airportParameters = new HBox();
        airportParameters.setAlignment(Pos.CENTER);
        TextField inputAirportName = new TextField();
        inputAirportName.setPromptText("airport name");
        var tarmacCountOptions = FXCollections.observableArrayList("1 tarmac", "2 tarmacs", "3 tarmacs");
        ComboBox dropdownTarmacCount = new ComboBox(tarmacCountOptions);
        dropdownTarmacCount.setValue(tarmacCountOptions.get(0));
        dropdownTarmacCount.setOnAction(e -> {
            int tarmacCount = 1;
            if (dropdownTarmacCount.getValue() == tarmacCountOptions.get(1)) tarmacCount = 2;
            else if (dropdownTarmacCount.getValue() == tarmacCountOptions.get(2)) tarmacCount = 3;

            if (tarmacCount == 1) {
                if (vboxTarmacs.getChildren().size() == 2)
                    vboxTarmacs.getChildren().remove(1);
                else {
                    vboxTarmacs.getChildren().remove(2);
                    vboxTarmacs.getChildren().remove(1);
                }
            } else if (tarmacCount == 2) {
                if (vboxTarmacs.getChildren().size() == 1)
                    vboxTarmacs.getChildren().add(new TarmacVBox(2, tarmacCount));
                else vboxTarmacs.getChildren().remove(2);
            } else {
                if (vboxTarmacs.getChildren().size() == 1) {
                    vboxTarmacs.getChildren().add(new TarmacVBox(2, tarmacCount));
                    vboxTarmacs.getChildren().add(new TarmacVBox(3, tarmacCount));
                } else vboxTarmacs.getChildren().add(new TarmacVBox(3, tarmacCount));
            }
        });
        airportParameters.getChildren().addAll(inputAirportName, dropdownTarmacCount);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        Button create = new Button("Create");
        create.setOnAction((ActionEvent event) -> {
            String airportName = inputAirportName.getText();
            Tarmac[] tarmacs = new Tarmac[1];
            Airport airport = new Airport(airportName, tarmacs);

        });
        buttonBox.getChildren().add(cancel);
        buttonBox.setAlignment(Pos.CENTER);
        
        this.getChildren().addAll(titleBox, airportParameters, vboxTarmacs, buttonBox);
    }

    private class TarmacVBox extends VBox {
        public TarmacVBox(int tarmacID, int tarmacCount) {
            super();

            VBox runwaysVBox = new VBox();
            runwaysVBox.getChildren().add(new RunwayVBox("09"));

            HBox tarmacParameters = new HBox();
            Text textTarmacName = new Text("Tarmac " + tarmacID);
            TextField inputTarmacLength = new TextField();
            inputTarmacLength.setPromptText("length");
            ObservableList<String> directionOptions = FXCollections.observableArrayList("unidirectional", "bidirectional");
            ComboBox direction = new ComboBox(directionOptions);
            direction.setValue(directionOptions.get(0));
            direction.setOnAction(e -> {
                if (direction.getValue() == directionOptions.get(1))
                    runwaysVBox.getChildren().add(new RunwayVBox("27"));
                else runwaysVBox.getChildren().remove(1);
            });
            tarmacParameters.getChildren().setAll(textTarmacName, inputTarmacLength, direction);

            this.getChildren().addAll(tarmacParameters, runwaysVBox);
        }
    }

    private class RunwayVBox extends VBox {
        public RunwayVBox(String runwayDesignator) {
            HBox runwayParameters = new HBox();
            Text textDesignator = new Text(runwayDesignator);
            TextField inputThreshold = new TextField();
            inputThreshold.setPromptText("displacement threshold");
            TextField inputStopway = new TextField();
            inputStopway.setPromptText("stopway");
            TextField inputClearway = new TextField();
            inputClearway.setPromptText("clearway");
            runwayParameters.getChildren().addAll(textDesignator, inputStopway, inputClearway);

            HBox runwayValues = new HBox();
            TextField inputTORA = new TextField();
            inputTORA.setPromptText("tora");
            TextField inputTODA = new TextField();
            inputTODA.setPromptText("toda");
            TextField inputLDA = new TextField();
            inputLDA.setPromptText("lda");
            TextField inputASDA = new TextField();
            inputASDA.setPromptText("asda");
            runwayValues.getChildren().setAll(inputTORA, inputTODA, inputLDA, inputASDA);

            this.getChildren().setAll(runwayParameters, runwayValues);
        }
    }
}
