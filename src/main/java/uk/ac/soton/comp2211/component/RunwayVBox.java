package uk.ac.soton.comp2211.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.exceptions.RunwayException;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.RunwayValues;
import uk.ac.soton.comp2211.model.Tarmac;

public class RunwayVBox extends VBox {
    private Text textDesignator;

    // private NumberField inputThreshold;
    // private NumberField inputStopway;
    // private NumberField inputClearway;

    private NumberField inputTORA;
    private NumberField inputTODA;
    private NumberField inputLDA;
    private NumberField inputASDA;

    public RunwayVBox(String runwayDesignator) {
        HBox runwayParameters = new HBox();
        Text header = new Text("Runway: ");
        textDesignator = new Text(runwayDesignator);
        // inputThreshold = new NumberField(Runway.);
        // inputThreshold.setPromptText("displacement threshold");
        // inputStopway = new TextField();
        // inputStopway.setPromptText("stopway");
        // inputClearway = new TextField();
        // inputClearway.setPromptText("clearway");
        runwayParameters.getChildren().addAll(header, textDesignator);
        runwayParameters.setAlignment(Pos.CENTER_LEFT);
        runwayParameters.setPadding(new Insets(10,0,10,0));

        HBox runwayValues = new HBox();
        textDesignator = new Text(runwayDesignator);
        inputTORA = new NumberField(Runway.MIN_TORA, Runway.MAX_TORA);
        inputTORA.setPromptText("tora");
        inputTODA = new NumberField(Runway.MIN_TODA, Runway.MAX_TODA);
        inputTODA.setPromptText("toda");
        inputLDA = new NumberField(Runway.MIN_LDA, Runway.MAX_LDA);
        inputLDA.setPromptText("lda");
        inputASDA = new NumberField(Runway.MIN_ASDA, Runway.MAX_ASDA);
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

        return new Runway(runwayDesignator, _tarmac, runwayValues);
    }
}