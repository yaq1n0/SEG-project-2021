package uk.ac.soton.comp2211.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.event.ValidateListener;
import uk.ac.soton.comp2211.exceptions.RunwayException;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.Tarmac;

public class TarmacVBox extends VBox {
    public VBox vboxRunways;

    private NumberField inputTarmacLength;

    private int tarmacID;
    private ValidateListener validateListener;

    public TarmacVBox(int _tarmacID, int _tarmacCount, ValidateListener listener) {
        super();

        validateListener = listener;
        this.setPadding(new Insets(10, 10, 10, 10));

        tarmacID = _tarmacID;

        vboxRunways = new VBox();
        vboxRunways.getChildren().add(new RunwayVBox(runwayDesignator(_tarmacID, _tarmacCount, 1), this.validateListener));

        HBox tarmacParameters = new HBox();
        Text textTarmacName = new Text("Tarmac " + tarmacID + "   ");
        textTarmacName.setFont(Font.font("Verdana"));

        inputTarmacLength = new NumberField(Tarmac.MIN_LENGTH, Tarmac.MAX_LENGTH);
        inputTarmacLength.setPromptText("length");
        inputTarmacLength.textProperty().addListener((e) -> { this.validateListener.validate(); });

        ObservableList<String> directionOptions = FXCollections.observableArrayList("unidirectional", "bidirectional");
        ComboBox<String> direction = new ComboBox<String>(directionOptions);
        direction.setValue(directionOptions.get(0));
        direction.setOnAction(e -> {
            vboxRunways.getChildren().clear();
            vboxRunways.getChildren().add(new RunwayVBox(
                runwayDesignator(_tarmacID, _tarmacCount, 1), this.validateListener));
            if (direction.getValue() == directionOptions.get(1)) {
                vboxRunways.getChildren().add(new RunwayVBox(
                    runwayDesignator(_tarmacID, _tarmacCount, 2), this.validateListener));
            }

            this.validateListener.validate();
        });

        tarmacParameters.getChildren().setAll(textTarmacName, inputTarmacLength, direction);
        tarmacParameters.setAlignment(Pos.CENTER);

        this.getChildren().addAll(tarmacParameters, vboxRunways);
    }

    private String runwayDesignator(int _tarmacID, int _tarmacCount, int _runwayID) {
        
        if (_tarmacCount == 1) {
            if (_runwayID == 1) return "09";
            else if (_runwayID == 2) return "27";
        } else if (_tarmacCount == 2) {
            if (_tarmacID == 1) {
                if (_runwayID == 1) return "09L";
                else if (_runwayID == 2) return "27R";
            } else if (_tarmacID == 2) {
                if (_runwayID == 1) return "09R";
                else if (_runwayID == 2) return "27L";
            }
        } else if (_tarmacCount == 3) {
            if (_tarmacID == 1) {
                if (_runwayID == 1) return "09L";
                else if (_runwayID == 2) return "27R";
            } else if (_tarmacID == 2) {
                if (_runwayID == 1) return "09C";
                else if (_runwayID == 2) return "27C";
            } else if (_tarmacID == 3) {
                if (_runwayID == 1) return "09R";
                else if (_runwayID == 2) return "27L";
            }
        }

        return "error";
    }

    public boolean hasValidateFields() {
        if (inputTarmacLength.getValue() == 0) return false;

        for (int i = 0; i < vboxRunways.getChildren().size(); i++)
            if (!((RunwayVBox) vboxRunways.getChildren().get(i))
                .hasValidateFields(inputTarmacLength.getValue())) return false;

        return true;
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