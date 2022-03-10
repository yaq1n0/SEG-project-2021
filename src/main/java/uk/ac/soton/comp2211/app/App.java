package uk.ac.soton.comp2211.app;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.component.AirportSelect;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.Runway;
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
        controller.setStage(stage);
        
        // Set the listeners
        controller.setQuitListener(this::stop);
        controller.setOpenAirportListener(() -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);

            AirportSelect airportSelect = new AirportSelect(dialog, this.airportNames);
            airportSelect.setPassAirportListener(controller::setAirport);
            
            Scene dialogScene = new Scene(airportSelect, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        });
        controller.setImportAirportListener(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Airport File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                controller.setAirport(file.getPath());
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
    
    public void getObstacleChoice(Obstacle obstacle) {
        System.out.println(obstacle.getName());
    }

    /**
     * Recalculation of runway parameters.
     * @param runway runway
     */
    public void recalculate(Runway runway) {
        System.out.println(runway.getRunwayDesignator());
    }
    
}
