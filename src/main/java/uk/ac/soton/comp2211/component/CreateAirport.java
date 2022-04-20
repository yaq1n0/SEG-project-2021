package uk.ac.soton.comp2211.component;

import javafx.collections.FXCollections;
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
import uk.ac.soton.comp2211.exceptions.LoadingException;
import uk.ac.soton.comp2211.exceptions.RunwayException;
import uk.ac.soton.comp2211.exceptions.WritingException;
import uk.ac.soton.comp2211.model.Airport;
import uk.ac.soton.comp2211.model.SystemModel;
import uk.ac.soton.comp2211.model.Tarmac;


public class CreateAirport extends VBox {

    private TextField inputAirportName;
    private VBox vboxTarmacs;

    private Button create;

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
        inputAirportName.textProperty().addListener((e) -> { validateFields(); });
        var tarmacCountOptions = FXCollections.observableArrayList("1 tarmac", "2 tarmacs", "3 tarmacs");
        ComboBox<String> dropdownTarmacCount = new ComboBox<String>(tarmacCountOptions);
        dropdownTarmacCount.setValue(tarmacCountOptions.get(0));
        dropdownTarmacCount.setOnAction(e -> {
            int tarmacCount = 1;
            if (dropdownTarmacCount.getValue() == tarmacCountOptions.get(1)) tarmacCount = 2;
            else if (dropdownTarmacCount.getValue() == tarmacCountOptions.get(2)) tarmacCount = 3;

            vboxTarmacs.getChildren().clear();
            for (int tarmacID = 1; tarmacID <= tarmacCount; tarmacID++) 
                vboxTarmacs.getChildren().add(new TarmacVBox(tarmacID, tarmacCount));

        });
        airportParameters.getChildren().addAll(inputAirportName, dropdownTarmacCount);

        Button cancel = new Button("Cancel");
        cancel.setOnAction((e) -> { dialog.close(); });
        cancel.setStyle("-fx-background-color: #D3CECF; ");

        create = new Button("Create");
        create.setDisable(true);
        create.setOnAction((e) -> {
            try {
                generateAirport();
            } catch (RunwayException | WritingException | LoadingException e1) {
                
            }
        });

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(cancel, create);
        buttonBox.setAlignment(Pos.CENTER);
        
        this.getChildren().addAll(titleBox, airportParameters, vboxTarmacs, buttonBox);
    }

    private void validateFields() {
        boolean valid = true;

        if (inputAirportName.getText().equals("")) valid = false;

        for (int i = 0; i < vboxTarmacs.getChildren().size(); i++)
            if (!((TarmacVBox) vboxTarmacs.getChildren().get(i)).hasValidateFields()) valid = false;

        create.setDisable(!valid);
    }

    private void generateAirport() throws RunwayException, WritingException, LoadingException {
        String airportName = inputAirportName.getText();

        int tarmacCount = vboxTarmacs.getChildren().size();
        Tarmac[] tarmacs = new Tarmac[tarmacCount];
        for (int i = 0; i < tarmacCount; i++) {
            TarmacVBox vboxTarmac = (TarmacVBox)vboxTarmacs.getChildren().get(i);

            tarmacs[i] = vboxTarmac.getTarmac();
        }

        Airport airport = new Airport(airportName, tarmacs, airportName + ".xml");
        SystemModel.addAirport(airport, airportName + ".xml");
    }
}
