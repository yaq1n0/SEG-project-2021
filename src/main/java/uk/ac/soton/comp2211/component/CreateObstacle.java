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
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.SystemModel;


public class CreateObstacle extends VBox {
    private TextField inputObstacleName;
    private NumberField inputObstacleLength;
    private NumberField inputObstacleWidth;
    private NumberField inputObstacleHeight;

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
        inputObstacleLength = new NumberField(1, 1000);
        inputObstacleLength.setPromptText("length");
        inputObstacleWidth = new NumberField(1, 1000);
        inputObstacleWidth.setPromptText("width");
        inputObstacleHeight = new NumberField(1, 1000);
        inputObstacleHeight.setPromptText("height");
        obstacleParameters.setPadding(new Insets(20,0,0,0));
        obstacleParameters.getChildren().addAll(inputObstacleName, inputObstacleLength,
                inputObstacleWidth, inputObstacleHeight);
        //titleBox.getChildren().add(new Text("Create Airport:"));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
    
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        Button create = new Button("Create");
        create.setOnAction((ActionEvent event) -> {
            try {
                generateObstacle();
            } catch (Exception e1) {
                e1.printStackTrace(); 
            }
        });

        cancel.setStyle("-fx-background-color: #D3CECF; ");
        create.setStyle("-fx-background-color: #766A6B; ");
        buttonBox.getChildren().addAll(cancel, create);
        buttonBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(titleBox, obstacleParameters, buttonBox);
        }

        public void generateObstacle() throws Exception {
            String obstacleName = inputObstacleName.getText();
            int obstacleLength = Integer.valueOf(inputObstacleLength.getText());
            int obstacleWidth = Integer.valueOf(inputObstacleWidth.getText());
            int obstacleHeight = Integer.valueOf(inputObstacleHeight.getText());

            Obstacle obstacle = new Obstacle(obstacleName, obstacleLength, obstacleWidth, obstacleHeight);

            SystemModel.addObstacle(obstacle);
        }

    }