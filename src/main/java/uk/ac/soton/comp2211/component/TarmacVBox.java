package uk.ac.soton.comp2211.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.exceptions.RunwayException;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.Tarmac;

public class TarmacVBox extends VBox {
    public VBox vboxRunways;

    private NumberField inputTarmacLength;

    private int tarmacID;

    public TarmacVBox(int _tarmacID, int _tarmacCount) {
        super();

        tarmacID = _tarmacID;
        VBox runwaysVBox = new VBox();
        runwaysVBox.getChildren().add(new RunwayVBox("09     "));

        HBox tarmacParameters = new HBox();
        Text textTarmacName = new Text("Tarmac " + tarmacID + "   ");
        textTarmacName.setFont(Font.font("Verdana"));

        inputTarmacLength = new NumberField(1, 1000);
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

    public boolean hasValidateFields() {
        boolean valid = true;

        if (inputTarmacLength.getText().equals("")) valid = false;

        return valid;
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