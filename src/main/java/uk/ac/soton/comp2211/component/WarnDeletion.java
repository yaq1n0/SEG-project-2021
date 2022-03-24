package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.event.ConfirmationListener;
import uk.ac.soton.comp2211.event.PassObstacleListener;
import uk.ac.soton.comp2211.model.Obstacle;

/**
 * Dialogue to confirm whether the user wants to continue with the tarmac deletion operation.
 */
public class WarnDeletion extends VBox {
    
    private ConfirmationListener confirmationListener;

    public WarnDeletion(Stage dialog) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(new Text("Are you sure you want to delete this tarmac? This action is irreversible."));
        
        this.getChildren().addAll(titleBox, new Text("Deleting this tarmac will remove all logical runways."));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event) -> {
            if (this.confirmationListener != null) {
                this.confirmationListener.confirmed(false);
            }
            dialog.close();
        });
        Button confirm = new Button("Confirm");
        confirm.setOnAction((ActionEvent event) -> {
            if (this.confirmationListener != null) {
                this.confirmationListener.confirmed(true);
            }
        });
        buttonBox.getChildren().addAll(cancel, confirm);
        buttonBox.setAlignment(Pos.CENTER);
        this.getChildren().add(buttonBox);
    }

    /**
     * Set confirmation listener.
     * @param listener listener
     */
    public void setConfirmationListener(ConfirmationListener listener) {
        this.confirmationListener = listener;
    }
    
}
