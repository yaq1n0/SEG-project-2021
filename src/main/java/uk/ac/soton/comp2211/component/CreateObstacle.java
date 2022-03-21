package uk.ac.soton.comp2211.component;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        titleBox.getChildren().add(new Text("Create Obstacle:"));

        HBox obstacleParameters = new HBox();
        inputObstacleName = new TextField();
        inputObstacleName.setPromptText("name");
        inputObstacleLength = new NumberField(Obstacle.MIN_LENGTH, Obstacle.MAX_LENGTH);
        inputObstacleLength.setPromptText("length");
        inputObstacleWidth = new NumberField(Obstacle.MIN_WIDTH, Obstacle.MAX_WIDTH);
        inputObstacleWidth.setPromptText("width");
        inputObstacleHeight = new NumberField(Obstacle.MIN_HEIGHT, Obstacle.MAX_HEIGHT);
        inputObstacleHeight.setPromptText("height");
        obstacleParameters.getChildren().addAll(inputObstacleName, inputObstacleLength, 
                                                inputObstacleWidth, inputObstacleHeight);

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
