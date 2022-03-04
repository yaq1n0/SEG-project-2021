package uk.ac.soton.comp2211.app;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * JavaFX Application class: Viewer part of MVC Pattern.
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);

    /**
     * Launch the JavaFx Application
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        logger.info("Launching Application...");
        launch(args);
    }

    /**
     * Initialisation Method
     */
    public void init() {
        logger.info("Initialising...");
    }

    /**
     * Initialise the stage and scene, then show them to the screen.
     * @param stage Application Stage
     */
    public void start(Stage stage) {
        // Set the stage's title.
        stage.setTitle("Runway Redeclaration App");
        
        Parent root;
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource("/base.fxml"));
            root = loader.load();
        } catch (IOException e) {
            logger.info("Could not load base.fxml");
            this.stop();
            return;
        }

        MainController controller = loader.<MainController>getController();
        
        // Set the listeners
        controller.setQuitListener(this::stop);
        controller.setInsertObstacleListener(() -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Insert Obstacle:"));
            
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        });
        controller.setOpenAirportListener(() -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);

            VBox dialogVbox = new VBox(20);
            HBox titleBox = new HBox();
            titleBox.setAlignment(Pos.CENTER);
            titleBox.getChildren().add(new Text("Open Airport:"));
            
            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.CENTER);
            Button cancel = new Button("Cancel");
            cancel.setOnAction((ActionEvent event) -> {
                dialog.close();
            });
            buttonBox.getChildren().add(cancel);
            dialogVbox.getChildren().addAll(titleBox, buttonBox);

            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        });
        controller.setImportAirportListener(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Airport File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                //openFile(file);
            }
        });
        
        Scene scene = new Scene(root, 800, 600);
        
        // Set the initial scene.
        stage.setScene(scene);
        
        // Show the stage and the initial scene.
        stage.show();
        
    }
    
    /**
     * Application Teardown.
     */
    public void stop() {
        logger.info("Stopping...");
        // Seems scuffed.
        Platform.exit();
        System.exit(0);
    }
    
}
