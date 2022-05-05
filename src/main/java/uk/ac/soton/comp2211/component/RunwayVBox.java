package uk.ac.soton.comp2211.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.event.ValidateListener;
import uk.ac.soton.comp2211.exceptions.RunwayException;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.RunwayValues;
import uk.ac.soton.comp2211.model.Tarmac;

public class RunwayVBox extends VBox {
    private static final String DESIGNATOR_INPUT_REGEX = "([0-9]|[0-2][0-9]|3[0-6])?[L, C, R]?";
    private static final String DESIGNATOR_REGEX = "([0-2][0-9]|3[0-6])[L, C, R]?";

    private TextField inputDesignator;

    private NumberField inputTORA;
    private NumberField inputTODA;
    private NumberField inputLDA;
    private NumberField inputASDA;
    private ValidateListener validateListener;

    public RunwayVBox(ValidateListener listener) {
        this.validateListener = listener;

        Text header = new Text("Runway: ");

        inputDesignator = new TextField();
        inputDesignator.setPromptText("designator");
        inputDesignator.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(DESIGNATOR_INPUT_REGEX)) {
                inputDesignator.setText(oldValue);
            }

            this.validateListener.validate();
        });
        
        HBox runwayParameters = new HBox();
        runwayParameters.getChildren().addAll(header, inputDesignator);
        runwayParameters.setAlignment(Pos.CENTER_LEFT);
        runwayParameters.setPadding(new Insets(10,0,10,0));

        HBox runwayValues = new HBox();
        inputTORA = new NumberField(Runway.MIN_TORA, Runway.MAX_TORA);
        inputTORA.setPromptText("tora");
        inputTORA.textProperty().addListener((e) -> { this.validateListener.validate(); });
        inputTODA = new NumberField(Runway.MIN_TODA, Runway.MAX_TODA);
        inputTODA.setPromptText("toda");
        inputTODA.textProperty().addListener((e) -> { this.validateListener.validate(); });
        inputLDA = new NumberField(Runway.MIN_LDA, Runway.MAX_LDA);
        inputLDA.setPromptText("lda");
        inputLDA.textProperty().addListener((e) -> { this.validateListener.validate(); });
        inputASDA = new NumberField(Runway.MIN_ASDA, Runway.MAX_ASDA);
        inputASDA.setPromptText("asda");
        inputASDA.textProperty().addListener((e) -> { this.validateListener.validate(); });
        runwayValues.getChildren().setAll(inputTORA, inputTODA, inputLDA, inputASDA);
        runwayValues.setAlignment(Pos.CENTER);

        this.getChildren().setAll(runwayParameters, runwayValues);
    }

    public Runway getRunway(Tarmac _tarmac) throws RunwayException {
        String runwayDesignator = inputDesignator.getText();

        int tora = Integer.valueOf(inputTORA.getText());
        int toda = Integer.valueOf(inputTODA.getText());
        int lda = Integer.valueOf(inputLDA.getText());
        int asda = Integer.valueOf(inputASDA.getText());
        RunwayValues runwayValues = new RunwayValues(tora, toda, asda, lda);

        return new Runway(runwayDesignator, _tarmac, runwayValues);
    }

    public boolean hasValidateFields(int _tarmacLength) {
        if (!inputDesignator.getText().matches(DESIGNATOR_REGEX)) return false;

        if (inputTORA.getValue() == 0) return false;
        if (inputTORA.getValue() > _tarmacLength) return false;

        if (inputTODA.getValue() == 0) return false;
        if (inputTODA.getValue() < inputTORA.getValue()) return false;

        if (inputLDA.getValue() == 0) return false;
        if (inputLDA.getValue() > _tarmacLength) return false;
        if (inputLDA.getValue() > inputTORA.getValue()) return false;

        if (inputASDA.getValue() == 0) return false;
        if (inputASDA.getValue() < inputTORA.getValue()) return false;

        return true;
    }
}
