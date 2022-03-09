package uk.ac.soton.comp2211.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.model.RunwayValues;

/**
 * Component to display and update runway parameters to the UI automatically.
 */
public class ParameterBox extends VBox {

    /**
     * Bind-able parameter properties
     */
    private final StringProperty tora = new SimpleStringProperty();
    private final StringProperty toda = new SimpleStringProperty();
    private final StringProperty asda = new SimpleStringProperty();
    private final StringProperty lda = new SimpleStringProperty();

    /**
     * Create a parameter box component from runway values.
     * @param values initial parameters
     */
    public ParameterBox(RunwayValues values) {
        super();
        
        updateValues(values);
        
        Label toraBox = new Label();
        toraBox.textProperty().bind(this.tora);
        Label todaBox = new Label();
        todaBox.textProperty().bind(this.toda);
        Label asdaBox = new Label();
        asdaBox.textProperty().bind(this.asda);
        Label ldaBox = new Label();
        ldaBox.textProperty().bind(this.lda);
        
        this.getChildren().addAll(toraBox, todaBox, asdaBox, ldaBox);
    }

    /**
     * Update the parameter properties on new ones from RunwayValues.
     * @param values new parameters
     */
    public void updateValues(RunwayValues values) {
        this.tora.set("TORA: " + String.valueOf(values.getTORA()));
        this.toda.set("TODA: " + String.valueOf(values.getTODA()));
        this.asda.set("ASDA: " + String.valueOf(values.getASDA()));
        this.lda.set("LDA: " + String.valueOf(values.getLDA()));
    }
    
}
