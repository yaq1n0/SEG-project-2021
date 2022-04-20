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

public class MessageBox extends VBox {

    public MessageBox(Stage dialog, String[] messages) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        Text text = new Text("Message:");
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));
        titleBox.getChildren().add(text);
        titleBox.setPadding(new Insets(10, 0, 0, 0));
        this.getChildren().add(titleBox);

        for (String message : messages) {
            this.getChildren().add(new Text(message));
        }

        Button close = new Button("Close");
        close.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        this.getChildren().add(close);
    }
}
