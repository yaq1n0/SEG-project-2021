package uk.ac.soton.comp2211.app;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.component.AirportContainer;
import javafx.event.ActionEvent;

import uk.ac.soton.comp2211.event.*;
import uk.ac.soton.comp2211.model.*;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainController.class);
    
    @FXML
    private Label airportName;
    @FXML
    private AnchorPane airportParent;
    @FXML
    private VBox vboxParent;
    
    private AirportContainer airportContainer;
    private final BooleanProperty topView;
    private Stage stage;
    private Button openAirportButton;
    private Label openAirportLabel;
    
    //Listeners
    private QuitListener quitListener;
    private ChooseAirportListener chooseAirportListener;
    private ImportAirportListener importAirportListener;
    private CreateAirportListener createAirportListener;
    private CreateObstacleListener createObstacleListener;

    public MainController() {
        this.topView = new SimpleBooleanProperty(true);
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add button to start to make it obvious how to open an airport.
        this.openAirportLabel = new Label("Don't know where to start? Why not try: ");
        this.openAirportButton = new Button("Open Airport");
        this.openAirportButton.setOnAction(this::openAirport);
        HBox startBox = new HBox();
        startBox.getChildren().addAll(this.openAirportLabel, this.openAirportButton);
        this.airportParent.getChildren().add(startBox);
        
        this.airportContainer = new AirportContainer(stage);
        
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
    public void openAirport(ActionEvent actionEvent) {
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
        this.airportName.setText("");
        this.airportContainer.closeAirport();
        this.openAirportButton.setVisible(true);
        this.openAirportLabel.setVisible(true);
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
        // Preference menu could be good for later requirements such as colour blind mode
        System.out.println("Preferences");
    }
    @FXML
    private void darkMode(ActionEvent actionEvent) {
        // Preference menu could be good for later requirements such as colour blind mode?
        vboxParent.getStylesheets().add("Styles/DarkMode.css");
        logger.error("preferences was clicked");
    }

    @FXML void lightMode(ActionEvent actionEvent){
        vboxParent.getStylesheets().remove("Styles/DarkMode.css");
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
     * Receive airport selection from user.
     * @param airportPath path of relevant airport.xml
     */
    public void setAirport(String airportPath) {
        try {
            SystemModel.loadAirport(airportPath);
            Airport airport = SystemModel.getAirport();
            this.airportName.setText(airport.getName());
            this.airportContainer.updateAirport(airport);
            this.openAirportButton.setVisible(false);
            this.openAirportLabel.setVisible(false);
        } catch (Exception e) {
            logger.error("Could not load airport! {}", airportPath);
            e.printStackTrace();
            closeAirport(new ActionEvent());
            this.airportName.setText("Error loading airport file: " + airportPath);
        }
    }

    /**
     * Receive airport selection from user.
     * @param airportPath path of relevant airport.xml
     */
    public void importAirport(String airportPath) {
        try {
            SystemModel.importAirport(airportPath);
            Airport airport = SystemModel.getAirport();
            this.airportName.setText(airport.getName());
            this.airportContainer.updateAirport(airport);

        } catch (Exception e) {
            logger.error("Could not load airport! {}", airportPath);
            e.printStackTrace();
            closeAirport(new ActionEvent());
            this.airportName.setText("Error loading airport file: " + airportPath);
        }
    }

    /**
     * Ran when user selects Menu>New Airport
     * @param event event
     */
    @FXML
    public void createAirport(ActionEvent event) {
        this.createAirportListener.openCreateDialogue();
    }

    /**
     * Set create airport listener
     * @param listener listener
     */
    public void setCreateAirportListener(CreateAirportListener listener) {
        this.createAirportListener = listener;
    }

    /**
     * Ran when user selects Menu>Create Obstacle
     * @param event event
     */
    @FXML
    public void createObstacle(ActionEvent event) {
        this.createObstacleListener.openCreateDialogue();
    }

    /**
     * Set create obstacle listener
     * @param listener listener
     */
    public void setCreateObstacleListener(CreateObstacleListener listener) {
        this.createObstacleListener = listener;
    }
}
