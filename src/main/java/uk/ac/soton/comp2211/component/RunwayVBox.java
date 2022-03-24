package uk.ac.soton.comp2211.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.exceptions.RunwayException;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.RunwayValues;
import uk.ac.soton.comp2211.model.Tarmac;

public class RunwayVBox extends VBox {
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

        return new Runway(runwayDesignator, _tarmac, runwayValues);
    }
}
