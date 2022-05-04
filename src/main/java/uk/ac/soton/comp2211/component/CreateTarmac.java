package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.event.NotificationListener;
import uk.ac.soton.comp2211.event.ResetAirportListener;
import uk.ac.soton.comp2211.model.SystemModel;
import uk.ac.soton.comp2211.model.Tarmac;

public class CreateTarmac extends VBox {

    private static final Logger logger = LogManager.getLogger(CreateTarmac.class);

    private final TarmacVBox tarmacVBox;
    private final Button createButton;

    private ResetAirportListener resetAirportListener;
    private NotificationListener notificationListener;

    public CreateTarmac(Stage dialog, int _newTarmacID) {
        super(20);
        
        Text text = new Text("Create A Tarmac:");
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(text);
        titleBox.setPadding(new Insets(10,0,0,0));

        tarmacVBox = new TarmacVBox(_newTarmacID, _newTarmacID, this::validateFields);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        createButton = new Button("Create");
        createButton.setDisable(true);
        createButton.setOnAction((ActionEvent event) -> {
            try {
                generateTarmac();
                dialog.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        cancelButton.setStyle("-fx-background-color: #D3CECF; ");
        createButton.setStyle("-fx-background-color: #766A6B; ");
        buttonBox.getChildren().addAll(cancelButton, createButton);
        buttonBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(titleBox, tarmacVBox, buttonBox);
    }

    private void validateFields() {
        boolean valid = tarmacVBox.hasValidateFields();
        createButton.setDisable(!valid);
    }

    /**
     * Create tarmac from user's input.
     */
    public void generateTarmac() throws Exception {
        Tarmac newTarmac = tarmacVBox.getTarmac();
        SystemModel.addTarmac(newTarmac);

        logger.info("Tarmac successfully created.");
        
        // Reset airport container component
        if (this.resetAirportListener != null) {
            this.resetAirportListener.reset();
        }
        if (this.notificationListener != null) {
            try {
                this.notificationListener.addNotification("Created new tarmac in " + SystemModel.getAirport().getName() + ".");
            } catch (Exception e) {
                logger.error("Could not notify tarmac creation: " + e.getMessage());
            }
        }
        
    }

    /**
     * Set reset airport listener
     * @param listener listener
     */
    public void setResetAirportListener(ResetAirportListener listener) {
        this.resetAirportListener = listener;
    }

    /**
     * Set notification listener
     * @param listener listener
     */
    public void setNotificationListener(NotificationListener listener) {
        this.notificationListener = listener;
    }
}
