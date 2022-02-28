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
    
    public static void main(String[] args) {
        System.out.println("Launching viewer...");
        launch(args);
    }
    
    public void init() {
        System.out.println("Initialising...");
    }
    
    public void start(Stage stage) {
        // Set the stage's title.
        stage.setTitle("Runway Redeclaration App");
        
        // Create an initial scene with a Pane root node.
        Pane rootNode = new Pane();
        Scene initialScene = new Scene(rootNode, 300, 200);
        
        // Set the initial scene.
        stage.setScene(initialScene);
        
        // Show the stage and the initial scene.
        stage.show();
    }
    
    public void stop() {
        System.out.println("Stopping...");
    }
    
}
