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
import uk.ac.soton.comp2211.event.MessageListener;
import uk.ac.soton.comp2211.event.NotificationListener;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.SystemModel;


public class CreateObstacle extends VBox {
    private TextField inputObstacleName;
    private NumberField inputObstacleLength;
    private NumberField inputObstacleWidth;
    private NumberField inputObstacleHeight;

    private Button create;
    private NotificationListener notificationListener;
    private MessageListener messageListener;

    public CreateObstacle(Stage dialog) {
        super(20);

        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        Text text = new Text("Create An Obstacle:");
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 20));
        titleBox.getChildren().add(text);
        titleBox.setPadding(new Insets(10,0,0,0));

        HBox obstacleParameters = new HBox();
        inputObstacleName = new TextField();
        inputObstacleName.setPromptText("name");
        inputObstacleName.textProperty().addListener((e) -> { validateFields(); });
        inputObstacleLength = new NumberField(1, 1000);
        inputObstacleLength.setPromptText("length");
        inputObstacleLength.textProperty().addListener((e) -> { validateFields(); });
        inputObstacleWidth = new NumberField(1, 1000);
        inputObstacleWidth.setPromptText("width");
        inputObstacleWidth.textProperty().addListener((e) -> { validateFields(); });
        inputObstacleHeight = new NumberField(1, 1000);
        inputObstacleHeight.setPromptText("height");
        inputObstacleHeight.textProperty().addListener((e) -> { validateFields(); });
        obstacleParameters.setPadding(new Insets(20,0,0,0));
        obstacleParameters.getChildren().addAll(inputObstacleName, inputObstacleLength,
                                                inputObstacleWidth, inputObstacleHeight);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
    
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        create = new Button("Create");
        create.setDisable(true);
        create.setOnAction((ActionEvent event) -> {
            try {
                generateObstacle();
                dialog.close();
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        });

        cancel.setStyle("-fx-background-color: #D3CECF; ");
        create.setStyle("-fx-background-color: #766A6B; ");
        buttonBox.getChildren().addAll(cancel, create);
        buttonBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(titleBox, obstacleParameters, buttonBox);
    }

    /**
     * If all fields are valid then enables the create button.
     */
    private void validateFields() {
        boolean valid = true;

        if (inputObstacleName.getText().equals("")) valid = false;
        else if (inputObstacleLength.getText().equals("")) valid = false;
        else if (inputObstacleWidth.getText().equals("")) valid = false;
        else if (inputObstacleHeight.getText().equals("")) valid = false;

        create.setDisable(!valid);
    }

    public void generateObstacle() throws Exception {
        String obstacleName = inputObstacleName.getText();
        int obstacleLength = Integer.valueOf(inputObstacleLength.getText());
        int obstacleWidth = Integer.valueOf(inputObstacleWidth.getText());
        int obstacleHeight = Integer.valueOf(inputObstacleHeight.getText());

        Obstacle obstacle = new Obstacle(obstacleName, obstacleLength, obstacleWidth, obstacleHeight);

        SystemModel.addObstacle(obstacle);
        
        if (this.notificationListener != null) {
            this.notificationListener.addNotification("Added new obstacle preset: " + obstacleName);
        }
        if (this.messageListener != null) {
            this.messageListener.openDialog(new String[]{"Obstacle creation was successful."});
        }
    }

    /**
     * Set notification listener
     * @param listener listener
     */
    public void setNotificationListener(NotificationListener listener) {
        this.notificationListener = listener;
    }

    /**
     * Set message listener
     * @param listener listener
     */
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }
}