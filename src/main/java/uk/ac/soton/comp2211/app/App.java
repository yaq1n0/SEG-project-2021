package uk.ac.soton.comp2211.app;

import com.sun.javafx.css.StyleManager;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.component.*;
import uk.ac.soton.comp2211.event.ConfirmationListener;
import uk.ac.soton.comp2211.model.SystemModel;

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
        
        // Initialise model
        try {
            SystemModel.loadObstacles();
        } catch (Exception e) {
            logger.error("Couldn't load obstacles.");
        }
    }

    /**
     * Initialise the stage and scene, then show them to the screen.
     * @param stage Application Stage
     */
    public void start(Stage stage) {
        // Set the stage's title.
        stage.setTitle("Runway Redeclaration App");
        
        logger.info("Attempting to load FXML.");
        Parent root;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(getClass().getResource("/base.fxml"));
            root = loader.load();
        } catch (IOException e) {
            logger.info("Could not load base.fxml");
            this.stop();
            return;
        }
        logger.info("FXML file loaded.");
        
        MainController controller = loader.getController();
        controller.setStage(stage);
        
        logger.info("Main controller loaded with FXML.");
        
        // Set the listeners
        controller.setQuitListener(this::stop);
        // Set the general listeners that may be reused later
        controller.setErrorListener((String[] messages) -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            ErrorBox box = new ErrorBox(dialog, messages);

            Scene dialogScene = new Scene(box, 300, 200);
            dialog.setScene(dialogScene);

            dialog.show();
        });
        controller.setWarningListener((String[] messages, ConfirmationListener listener) -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            WarningBox box = new WarningBox(dialog, messages);
            box.setConfirmationListener(listener);

            Scene dialogScene = new Scene(box, 300, 200);
            dialog.setScene(dialogScene);

            dialog.show();
        });
        controller.setMessageListener((String[] messages) -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            MessageBox box = new MessageBox(dialog, messages);

            Scene dialogScene = new Scene(box, 300, 200);
            dialog.setScene(dialogScene);

            dialog.show();
        });
        // Other dialogs
        controller.setOpenAirportListener(() -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);

            AirportSelect airportSelect;
            try {
                airportSelect = new AirportSelect(dialog, SystemModel.listAirports());
                airportSelect.setPassAirportListener(controller::setAirport);

                Scene dialogScene = new Scene(airportSelect, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        controller.setImportAirportListener(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Airport File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                controller.importAirport(file.getPath());
            }
        });
        controller.setCreateAirportListener(() -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);

            CreateAirport airportCreate = new CreateAirport(dialog);
            // Make sure the controller listeners have been set beforehand.
            airportCreate.setErrorListener(controller.getErrorListener());
            airportCreate.setMessageListener(controller.getMessageListener());
            airportCreate.setWarningListener(controller.getWarningListener());

            Scene dialogScene = new Scene(airportCreate, 400, 250);
            dialog.setScene(dialogScene);
            dialog.show();
        });
        controller.setCreateObstacleListener(() -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            CreateObstacle obstacleCreate = new CreateObstacle(dialog);

            Scene dialogScene = new Scene(obstacleCreate, 300, 200);
            dialog.setScene(dialogScene);

            dialog.show();
        });
        controller.setTarmacDeleteWarning((String[] messages, ConfirmationListener listener) -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            WarningBox warnDeletion = new WarningBox(dialog, messages);
            warnDeletion.setConfirmationListener(listener);

            Scene dialogScene = new Scene(warnDeletion, 500, 300);
            dialog.setScene(dialogScene);

            dialog.show();
        });
        controller.setCreateTarmacListener((int tarmacID) -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            CreateTarmac createTarmac = new CreateTarmac(dialog, tarmacID);
            createTarmac.setResetAirportListener(controller::resetAirport);

            Scene dialogScene = new Scene(createTarmac, 300, 200);
            dialog.setScene(dialogScene);

            dialog.show();
        });
        
        
        logger.info("Listeners set for controller.");
        
        Scene scene = new Scene(root, 900, 720);

        // Set the initial scene.
        stage.setScene(scene);
        
        // Show the stage and the initial scene.
        stage.show();

        StyleManager.getInstance().addUserAgentStylesheet("Styles/Button.css");
        
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
