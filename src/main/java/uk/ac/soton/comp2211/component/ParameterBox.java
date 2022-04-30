package uk.ac.soton.comp2211.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.RunwayValues;

/**
 * Component to display and update runway parameters to the UI automatically.
 */
public class ParameterBox extends HBox {

    /**
     * Bind-able parameter properties
     */
    private final StringProperty tora = new SimpleStringProperty();
    private final StringProperty toda = new SimpleStringProperty();
    private final StringProperty asda = new SimpleStringProperty();
    private final StringProperty lda = new SimpleStringProperty();
    private final String ogTora;
    private final String ogToda;
    private final String ogAsda;
    private final String ogLda;
    private RunwayView runwayView;

    /**
     * Create a parameter box component from runway values.
     * @param values initial parameters
     */
    public ParameterBox(RunwayValues values, RunwayView view) {
        super();
        this.runwayView = view;
        
        // Initial parameters
        this.ogTora = values.getTORA() + "m";
        this.ogToda = values.getTODA() + "m";
        this.ogAsda = values.getASDA() + "m";
        this.ogLda = values.getLDA() + "m";
        
        updateValues(values);

        double width = 150;
        
        Label recalculatedLabel = new Label("Recalculated Values:");

        VBox valuesBox = new VBox();
        
        HBox recalculatedHeaders = new HBox();
        Label toraLabel = new Label("TORA");
        Label todaLabel = new Label("TODA");
        Label asdaLabel = new Label("ASDA");
        Label ldaLabel = new Label("LDA");
        HBox.setHgrow(toraLabel, Priority.ALWAYS);
        HBox.setHgrow(todaLabel, Priority.ALWAYS);
        HBox.setHgrow(asdaLabel, Priority.ALWAYS);
        HBox.setHgrow(ldaLabel, Priority.ALWAYS);
        toraLabel.setPrefWidth(width);
        todaLabel.setPrefWidth(width);
        asdaLabel.setPrefWidth(width);
        ldaLabel.setPrefWidth(width);
        recalculatedHeaders.getChildren().addAll(toraLabel, todaLabel, asdaLabel, ldaLabel);

        HBox recalculatedValues = new HBox();
        Label toraBox = new Label();
        toraBox.textProperty().bind(this.tora);
        Label todaBox = new Label();
        todaBox.textProperty().bind(this.toda);
        Label asdaBox = new Label();
        asdaBox.textProperty().bind(this.asda);
        Label ldaBox = new Label();
        ldaBox.textProperty().bind(this.lda);
        HBox.setHgrow(toraBox, Priority.ALWAYS);
        HBox.setHgrow(todaBox, Priority.ALWAYS);
        HBox.setHgrow(asdaBox, Priority.ALWAYS);
        HBox.setHgrow(ldaBox, Priority.ALWAYS);
        toraBox.setPrefWidth(width);
        todaBox.setPrefWidth(width);
        asdaBox.setPrefWidth(width);
        ldaBox.setPrefWidth(width);
        recalculatedValues.getChildren().addAll(toraBox, todaBox, asdaBox, ldaBox);

        Label originalLabel = new Label("Original Values:");

        HBox originalHeaders = new HBox();
        Label toraLabel2 = new Label("TORA");
        Label todaLabel2 = new Label("TODA");
        Label asdaLabel2 = new Label("ASDA");
        Label ldaLabel2 = new Label("LDA");
        HBox.setHgrow(toraLabel2, Priority.ALWAYS);
        HBox.setHgrow(todaLabel2, Priority.ALWAYS);
        HBox.setHgrow(asdaLabel2, Priority.ALWAYS);
        HBox.setHgrow(ldaLabel2, Priority.ALWAYS);
        toraLabel2.setPrefWidth(width);
        todaLabel2.setPrefWidth(width);
        asdaLabel2.setPrefWidth(width);
        ldaLabel2.setPrefWidth(width);
        originalHeaders.getChildren().addAll(toraLabel2, todaLabel2, asdaLabel2, ldaLabel2);
        
        HBox originalValues = new HBox();
        Label ogToraBox = new Label(ogTora);
        Label ogTodaBox = new Label(ogToda);
        Label ogAsdaBox = new Label(ogAsda);
        Label ogLdaBox = new Label(ogLda);
        HBox.setHgrow(ogToraBox, Priority.ALWAYS);
        HBox.setHgrow(ogTodaBox, Priority.ALWAYS);
        HBox.setHgrow(ogAsdaBox, Priority.ALWAYS);
        HBox.setHgrow(ogLdaBox, Priority.ALWAYS);
        ogToraBox.setPrefWidth(width);
        ogTodaBox.setPrefWidth(width);
        ogAsdaBox.setPrefWidth(width);
        ogLdaBox.setPrefWidth(width);
        originalValues.getChildren().addAll(ogToraBox, ogTodaBox, ogAsdaBox, ogLdaBox);
        
        recalculatedLabel.setPadding(new Insets(10, 0, 5, 0));
        originalLabel.setPadding(new Insets(10, 0, 5, 0));
        
        valuesBox.getChildren().addAll(recalculatedLabel, recalculatedHeaders, recalculatedValues);
        valuesBox.getChildren().addAll(originalLabel, originalHeaders, originalValues);

        VBox valuesButtonBox = new VBox();
        CheckBox toraCheck = new CheckBox("TORA");
        toraCheck.setSelected(true);
        toraCheck.setOnAction((ActionEvent event) -> this.runwayView.displayTora());
        CheckBox todaCheck = new CheckBox("TODA");
        todaCheck.setSelected(true);
        todaCheck.setOnAction((ActionEvent event) -> this.runwayView.displayToda());
        CheckBox asdaCheck = new CheckBox("ASDA");
        asdaCheck.setSelected(true);
        asdaCheck.setOnAction((ActionEvent event) -> this.runwayView.displayAsda());
        CheckBox ldaCheck = new CheckBox("LDA");
        ldaCheck.setSelected(true);
        ldaCheck.setOnAction((ActionEvent event) -> this.runwayView.displayLda());
        CheckBox obsCheck = new CheckBox("Obstacle Distance Breakdown");
        obsCheck.setSelected(true);
        obsCheck.setOnAction((ActionEvent event) -> this.runwayView.displayObs());
        valuesButtonBox.setAlignment(Pos.CENTER_LEFT);
        valuesButtonBox.setPadding(new Insets(10, 0, 0, 0));
        valuesButtonBox.setSpacing(10);

        valuesButtonBox.getChildren().addAll(toraCheck, todaCheck, asdaCheck, ldaCheck, obsCheck);

        this.getChildren().addAll(valuesBox, valuesButtonBox);
    }

    /**
     * Update the parameter properties on new ones from RunwayValues.
     * @param values new parameters
     */
    public void updateValues(RunwayValues values) {
        this.tora.set(values.getTORA() + "m");
        this.toda.set(values.getTODA() + "m");
        this.asda.set(values.getASDA() + "m");
        this.lda.set(values.getLDA() + "m");
    }

    /**
     * Reset the parameters to their initial values.
     */
    public void resetValues() {
        this.tora.set(this.ogTora);
        this.toda.set(this.ogToda);
        this.asda.set(this.ogAsda);
        this.lda.set(this.ogLda);
    }
    
}
