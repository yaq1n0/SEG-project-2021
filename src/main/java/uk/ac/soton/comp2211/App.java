package uk.ac.soton.comp2211;

import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * JavaFX Application class: Viewer part of MVC Pattern.
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    
    public static void main(String[] args) {
        logger.info("Launching Application...");
        launch(args);
    }
    
    public void init() {
        logger.info("Initialising...");
    }
    
    public void start(Stage stage) {
        // Set the stage's title.
        stage.setTitle("Runway Redeclaration App");
        
        // Create an initial scene with a Pane root node.
        Pane rootNode = new Pane();
        Scene initialScene = new Scene(rootNode, 800, 600);
        
        // Set the initial scene.
        stage.setScene(initialScene);
        
        // Show the stage and the initial scene.
        stage.show();
    }
    
    public void stop() {
        logger.info("Stopping...");
    }
    
}
