package uk.ac.soton.comp2211.app;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.component.RunwayView;

import javafx.event.ActionEvent;
import uk.ac.soton.comp2211.event.ImportAirportListener;
import uk.ac.soton.comp2211.event.InsertObstacleListener;
import uk.ac.soton.comp2211.event.ChooseAirportListener;
import uk.ac.soton.comp2211.event.QuitListener;
import uk.ac.soton.comp2211.model.Airport;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainController.class);
    
    @FXML
    private Label airportName;
    
    @FXML 
    private VBox airportContainer;
    
    @FXML
    private VBox runwayContainer;
    
    @FXML
    private Button testButton;
    
    private RunwayView runwayView;
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
        this.runwayView = new RunwayView();
        this.runwayContainer.getChildren().add(this.runwayView);
        this.testButton.setOnAction(this.runwayView::update);
        
        // Bind the boolean properties to show which profile the runway view should be.
        this.runwayView.bindViewProperty(this.topView);
    }

    private void doSomething(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            GraphicsContext gc = this.runwayView.getGraphicsContext2D();
            gc.setFill(Color.RED);
            gc.fillRect(0, 60,0, 80);
        });
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
    
}
