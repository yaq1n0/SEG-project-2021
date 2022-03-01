package uk.ac.soton.comp2211.app;

import javafx.application.*;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.scene.BaseScene;

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

        BaseScene scene = new BaseScene();
        
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
    }
    
}
