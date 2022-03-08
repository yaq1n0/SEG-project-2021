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
import uk.ac.soton.comp2211.component.AirportSelect;
import uk.ac.soton.comp2211.component.ObstacleSelect;
import uk.ac.soton.comp2211.model.Airport;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.SystemModel;

import java.io.File;
import java.io.IOException;

/**
 * JavaFX Application class: Viewer part of MVC Pattern.
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    
    private String[][] airportNames;
    private Obstacle[] obstacles;
    
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
        
        // Initialise model
        SystemModel.loadSchemas();
        this.airportNames = SystemModel.listAirports();
        
        try {
            SystemModel.loadObstacles();
        } catch (Exception e) {
            logger.error("Could not load obstacles.");
        }
                
        this.obstacles = SystemModel.getObstacles();
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

            ObstacleSelect obstacleSelect = new ObstacleSelect(dialog, this.obstacles);
            obstacleSelect.setPassObstacleListener(this::getObstacleChoice);
            
            Scene dialogScene = new Scene(obstacleSelect, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        });
        controller.setOpenAirportListener(() -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);

            AirportSelect airportSelect = new AirportSelect(dialog, this.airportNames);
            airportSelect.setPassAirportListener(this::getAirportChoice);
            
            Scene dialogScene = new Scene(airportSelect, 300, 200);
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

    /**
     * Receive airport selection from user.
     * @param airportPath path of relevant airport.xml
     */
    public void getAirportChoice(String airportPath) {
        System.out.println(airportPath);
    }
    
    public void getObstacleChoice(Obstacle obstacle) {
        System.out.println(obstacle.getName());
    }
    
}
