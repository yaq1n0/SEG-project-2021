package uk.ac.soton.comp2211.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.exceptions.RunwayException;
import uk.ac.soton.comp2211.model.Airport;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.RunwayValues;
import uk.ac.soton.comp2211.model.SystemModel;
import uk.ac.soton.comp2211.model.Tarmac;


public class CreateAirport extends VBox {

    private TextField inputAirportName;
    private VBox vboxTarmacs;

    public CreateAirport(Stage dialog) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        Text text = new Text ("Create An Airport");
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));
        titleBox.getChildren().add(text);
        titleBox.setPadding(new Insets(10,0,0,0));

        vboxTarmacs = new VBox();
        vboxTarmacs.getChildren().add(new TarmacVBox(1, 1));
        HBox airportParameters = new HBox();
        airportParameters.setAlignment(Pos.CENTER);
        inputAirportName = new TextField();
        inputAirportName.setPromptText("airport name");
        var tarmacCountOptions = FXCollections.observableArrayList("1 tarmac", "2 tarmacs", "3 tarmacs");
        ComboBox<String> dropdownTarmacCount = new ComboBox<String>(tarmacCountOptions);
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
            try {
                generateAirport();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        cancel.setStyle("-fx-background-color: #D3CECF; ");
        buttonBox.getChildren().add(cancel);
        buttonBox.setAlignment(Pos.CENTER);
        
        this.getChildren().addAll(titleBox, airportParameters, vboxTarmacs, buttonBox);

    }

    private void generateAirport() throws Exception {
        String airportName = inputAirportName.getText();

        int tarmacCount = vboxTarmacs.getChildren().size();
        Tarmac[] tarmacs = new Tarmac[tarmacCount];
        for (int i = 0; i < tarmacCount; i++) {
            TarmacVBox vboxTarmac = (TarmacVBox)vboxTarmacs.getChildren().get(i);

            tarmacs[i] = vboxTarmac.getTarmac();
        }

        Airport airport = new Airport(airportName, tarmacs);
        SystemModel.addAirport(airport, airportName + ".xml");
    }

    private class TarmacVBox extends VBox {
        public VBox vboxRunways;

        private int tarmacID;

        public TarmacVBox(int _tarmacID, int _tarmacCount) {
            super();

            tarmacID = _tarmacID;
            VBox runwaysVBox = new VBox();
            runwaysVBox.getChildren().add(new RunwayVBox("09     "));

            HBox tarmacParameters = new HBox();
            Text textTarmacName = new Text("Tarmac " + tarmacID + "   ");
            textTarmacName.setFont(Font.font("Verdana"));
            NumberField inputTarmacLength = new NumberField(Tarmac.MIN_LENGTH, Tarmac.MAX_LENGTH);
            inputTarmacLength.setPromptText("length");
            ObservableList<String> directionOptions = FXCollections.observableArrayList("unidirectional", "bidirectional");
            ComboBox<String> direction = new ComboBox<String>(directionOptions);
            direction.setValue(directionOptions.get(0));
            direction.setOnAction(e -> {
                if (direction.getValue() == directionOptions.get(1))
                    runwaysVBox.getChildren().add(new RunwayVBox("27"));
                else runwaysVBox.getChildren().remove(1);
            });
            tarmacParameters.getChildren().setAll(textTarmacName, inputTarmacLength, direction);
            tarmacParameters.setAlignment(Pos.CENTER);

            this.getChildren().addAll(tarmacParameters, runwaysVBox);

        }
    
        public Tarmac getTarmac() throws RunwayException {
            Tarmac tarmac = new Tarmac(tarmacID);

            int runwayCount = vboxRunways.getChildren().size();
            Runway[] runways = new Runway[runwayCount];
            for (int i = 0; i < runwayCount; i++) {
                RunwayVBox vboxRunway = (RunwayVBox)vboxRunways.getChildren().get(i);

                runways[i] = vboxRunway.getRunway(tarmac);
            }

            tarmac.setRunways(runways);

            return tarmac;
        } 
    }

    private class RunwayVBox extends VBox {
        private Text textDesignator;

        private TextField inputThreshold;
        private TextField inputStopway;
        private TextField inputClearway;

        private TextField inputTORA;
        private TextField inputTODA;
        private TextField inputLDA;
        private TextField inputASDA;

        public RunwayVBox(String runwayDesignator) {
            HBox runwayParameters = new HBox();
            textDesignator = new Text(runwayDesignator);
            inputThreshold = new TextField();
            inputThreshold.setPromptText("displacement threshold");
            inputStopway = new TextField();
            inputStopway.setPromptText("stopway");
            inputClearway = new TextField();
            inputClearway.setPromptText("clearway");
            runwayParameters.getChildren().addAll(textDesignator, inputStopway, inputClearway);
            runwayParameters.setAlignment(Pos.CENTER);
            runwayParameters.setPadding(new Insets(10,0,10,0));

            HBox runwayValues = new HBox();
            inputTORA = new TextField();
            inputTORA.setPromptText("tora");
            inputTODA = new TextField();
            inputTODA.setPromptText("toda");
            inputLDA = new TextField();
            inputLDA.setPromptText("lda");
            inputASDA = new TextField();
            inputASDA.setPromptText("asda");
            runwayValues.getChildren().setAll(inputTORA, inputTODA, inputLDA, inputASDA);
            runwayValues.setAlignment(Pos.CENTER);

            this.getChildren().setAll(runwayParameters, runwayValues);
        }
    
        public Runway getRunway(Tarmac _tarmac) throws RunwayException {
            String runwayDesignator = textDesignator.getText();

            int tora = Integer.valueOf(inputTORA.getText());
            int toda = Integer.valueOf(inputTODA.getText());
            int lda = Integer.valueOf(inputLDA.getText());
            int asda = Integer.valueOf(inputASDA.getText());
            RunwayValues runwayValues = new RunwayValues(tora, toda, asda, lda);

            int displacedThreshold = Integer.valueOf(inputThreshold.getText());
            int stopway = Integer.valueOf(inputStopway.getText());
            int clearway = Integer.valueOf(inputClearway.getText());

            return new Runway(runwayDesignator, _tarmac, runwayValues, displacedThreshold, stopway, clearway);
        }
    }
}
