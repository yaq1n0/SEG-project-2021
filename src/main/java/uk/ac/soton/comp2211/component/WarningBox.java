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
import uk.ac.soton.comp2211.event.ConfirmationListener;

public class WarningBox extends VBox {
    
    private ConfirmationListener confirmationListener;

    public WarningBox(Stage dialog, String[] messages) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        Text text = new Text("Warning:");
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));
        titleBox.getChildren().add(text);
        titleBox.setPadding(new Insets(10, 0, 0, 0));
        this.getChildren().add(titleBox);
        
        for (String message : messages) {
            this.getChildren().add(new Text(message));
        }

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
            dialog.close();
        });
        buttonBox.getChildren().addAll(cancel, confirm);
        buttonBox.setAlignment(Pos.CENTER);
        
        this.getChildren().add(buttonBox);
    }

    /**
     * Set the confirmation listener so an action with permission can continue.
     * @param listener listener
     */
    public void setConfirmationListener(ConfirmationListener listener) { this.confirmationListener = listener; }
}
