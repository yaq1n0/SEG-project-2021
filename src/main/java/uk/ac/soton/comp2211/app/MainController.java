package uk.ac.soton.comp2211.app;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.component.AirportContainer;
import javafx.event.ActionEvent;
import uk.ac.soton.comp2211.event.ImportAirportListener;
import uk.ac.soton.comp2211.event.InsertObstacleListener;
import uk.ac.soton.comp2211.event.ChooseAirportListener;
import uk.ac.soton.comp2211.event.QuitListener;
import uk.ac.soton.comp2211.model.*;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainController.class);
    
    @FXML
    private Label airportName;
    @FXML
    private AnchorPane airportParent;
    
    private AirportContainer airportContainer;
    private final BooleanProperty topView;
    
    //Listeners
    
    private QuitListener quitListener;
    private InsertObstacleListener insertObstacleListener;
    private ChooseAirportListener chooseAirportListener;
    private ImportAirportListener importAirportListener;

    public MainController() {
        this.topView = new SimpleBooleanProperty(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.airportContainer = new AirportContainer();
        
        // Bind the boolean properties to show which profile the runway view should be.
        this.airportContainer.bindViewProperty(this.topView);
        
        // Add airport container to scene
        this.airportParent.getChildren().add(this.airportContainer);
        VBox.setVgrow(this.airportContainer, Priority.ALWAYS);
    }

    /**
     * Set the quit listener for the controller
     * @param listener quit listener
     */
    public void setQuitListener(QuitListener listener) {
        this.quitListener = listener;
    }

    /**
     * Set the insertObstacle listener for the controller
     * @param listener insertObstacle listener
     */
    public void setInsertObstacleListener(InsertObstacleListener listener) {
        this.insertObstacleListener = listener;
    }

    /**
     * Set the openAirport listener for the controller
     * @param listener openAirport listener
     */
    public void setOpenAirportListener(ChooseAirportListener listener) {
        this.chooseAirportListener = listener;
    }

    /**
     * Set the importAirport listener for the controller
     * @param listener listener
     */
    public void setImportAirportListener(ImportAirportListener listener) {
        this.importAirportListener = listener;
    }
    
    /**
     * Ran when user selects Top-Down in Menu>View
     * @param actionEvent event
     */
    @FXML
    private void selectTopView(ActionEvent actionEvent) {
        this.topView.set(true);
    }

    /**
     * Ran when user selects Side-On in Menu>View
     * @param actionEvent event
     */
    @FXML
    private void selectSideView(ActionEvent actionEvent) {
        this.topView.set(false);
    }
    
    /**
     * Ran when user selects Open Airport in Menu>File
     * @param actionEvent event
     */
    @FXML
    private void openAirport(ActionEvent actionEvent) {
        this.chooseAirportListener.openAirportDialogue();
    }

    /**
     * Ran when user selects Import From... in Menu>File
     * @param actionEvent event
     */
    @FXML
    private void importAirport(ActionEvent actionEvent) {
        this.importAirportListener.importAirport();
    }

    /**
     * Ran when user selects Close in Menu>File
     * @param actionEvent event
     */
    @FXML
    private void closeAirport(ActionEvent actionEvent) {
        System.out.println("Close file.");
    }

    /**
     * Ran when user selects Export As... in Menu>File
     * @param actionEvent event
     */
    @FXML
    private void exportAs(ActionEvent actionEvent) {
        System.out.println("Export file?");
    }

    /**
     * Ran when user selects Preferences in Menu>File
     * @param actionEvent event
     */
    @FXML
    private void preferencesMenu(ActionEvent actionEvent) {
        // Preference menu could be good for later requirements such as colour blind mode?
        System.out.println("Preferences");
    }

    /**
     * Ran when user selects Quit in Menu>File
     * @param actionEvent event
     */
    @FXML
    private void quitApplication(ActionEvent actionEvent) {
        this.quitListener.quitApplication();
    }

    /**
     * Ran when user selects About in Menu>Help
     * @param actionEvent event
     */
    @FXML
    private void showAbout(ActionEvent actionEvent) {
        System.out.println("About");
    }

    /**
     * Ran when user selects Instructions in Menu>Help
     * @param actionEvent event
     */
    @FXML
    private void showInstructions(ActionEvent actionEvent) {
        System.out.print("Instructions");
    }

    /**
     * Ran when user selects Insert Obstacles in Menu>Run
     * @param actionEvent event
     */
    @FXML
    private void openObstacleDialogue(ActionEvent actionEvent) {
        this.insertObstacleListener.openObstacleDialogue();
    }

    /**
     * Ran when user selects Run Redeclaration in Menu>Run
     * @param actionEvent event
     */
    @FXML
    private void runRedeclaration(ActionEvent actionEvent) {
        System.out.println("Run redeclaration");
    }

    /**
     * Receive airport selection from user.
     * @param airportPath path of relevant airport.xml
     */
    public void setAirport(String airportPath) {
        try {
            /*
            SystemModel.loadAirport(airportPath);
            this.airportContainer.updateAirport(SystemModel.getAirport());
            */
            RunwayValues testValues = new RunwayValues(3902, 3902, 3902, 3596);
            Runway testRunway = new Runway("09L", null, testValues, 306);
            Runway testRunway2 = new Runway("16R", null, testValues, 300);
            Airport testAirport = new Airport("Heathrow", new Runway[] {testRunway, testRunway2});
            
            this.airportName.setText(testAirport.getName());
            this.airportContainer.updateAirport(testAirport);
        } catch (Exception e) {
            logger.error("Could not load airport! {}", airportPath);
        }
    }
    
}
