package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateTarmac extends VBox {

    private static final Logger logger = LogManager.getLogger(CreateTarmac.class);
    
    private TextField inputObstacleName;
    private NumberField inputObstacleLength;
    private NumberField inputObstacleWidth;
    private NumberField inputObstacleHeight;

    public CreateTarmac(Stage dialog, int _newTarmacID) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        Text text = new Text("Create A Tarmac:");
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));
        titleBox.getChildren().add(text);
        titleBox.setPadding(new Insets(10,0,0,0));

        TarmacVBox tarmacVBox = new TarmacVBox(_newTarmacID, _newTarmacID - 1, null);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        Button create = new Button("Create");
        create.setOnAction((ActionEvent event) -> {
            try {
                generateTarmac();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        cancel.setStyle("-fx-background-color: #D3CECF; ");
        create.setStyle("-fx-background-color: #766A6B; ");
        buttonBox.getChildren().addAll(cancel, create);
        buttonBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(titleBox, tarmacVBox, buttonBox);
    }

    /**
     * Create tarmac from user's input.
     * @throws Exception exception
     */
    public void generateTarmac() throws Exception {

        // ********** ADD CODE **********

        logger.info("Tarmac successfully created.");
    }

}
