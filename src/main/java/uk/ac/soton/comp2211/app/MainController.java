package uk.ac.soton.comp2211.app;

import com.sun.javafx.css.StyleManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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

import uk.ac.soton.comp2211.component.NotificationsBox;
import uk.ac.soton.comp2211.event.*;
import uk.ac.soton.comp2211.exceptions.LoadingException;
import uk.ac.soton.comp2211.model.*;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainController.class);
    
    @FXML
    private Label airportName;
    @FXML
    private AnchorPane airportParent;

    private AirportContainer airportContainer;
    private final BooleanProperty topView;
    private final BooleanProperty colour;
    private Stage stage;
    private Button openAirportButton;
    private Label openAirportLabel;
    private VBox mainBox;
    private NotificationsBox notbox;
    
    //Listeners
    private QuitListener quitListener;
    private ChooseAirportListener chooseAirportListener;
    private ImportAirportListener importAirportListener;
    private CreateAirportListener createAirportListener;
    private CreateObstacleListener createObstacleListener;
    private CreateTarmacListener createTarmacListener;
    private ErrorListener errorListener;
    private WarningListener warningListener;
    private MessageListener messageListener;
    private WarningListener tarmacDeleteWarning;

    public MainController() {
        this.topView = new SimpleBooleanProperty(true);
        this.colour = new SimpleBooleanProperty(false);
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.mainBox = new VBox();
        // Add button to start to make it obvious how to open an airport.
        this.openAirportLabel = new Label("Don't know where to start? Why not try: ");
        this.openAirportButton = new Button("Open Airport");
        
        this.openAirportButton.setOnAction(this::openAirport);
        HBox startBox = new HBox(10);
        startBox.setPadding(new Insets(0,0,0,230));
        startBox.getChildren().addAll(this.openAirportLabel, this.openAirportButton);

        this.airportContainer = new AirportContainer(stage);

        this.notbox = new NotificationsBox();
        try {
            this.notbox.addNotifications(DataReader.getNotifications());
            
        } catch (LoadingException e) {
            logger.error("Issue loading notifications: " + e.getMessage());
        }
        
        this.mainBox.getChildren().addAll(startBox, notbox);
        // Bind the boolean properties to show which profile the runway view should be.
        this.airportContainer.bindViewProperty(this.topView);
        this.airportContainer.bindColourProperty(this.colour);
        this.airportContainer.setDeleteTarmacListeners(this::deleteTarmac);
        this.airportContainer.setAddTarmacListener(this::openAddTarmacDialogue);
        this.airportContainer.setNotificationListener(this::addNotification);
        
        // Add airport container and mainBox to scene
        this.airportParent.getChildren().addAll(this.mainBox, this.airportContainer);
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
        this.mainBox.setVisible(true);
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
    public void Default(ActionEvent action){
        this.colour.set(false);

    }
    @FXML
    public void ColourBlind(ActionEvent action){
        this.colour.set(true);
    }
    @FXML
    private void darkMode(ActionEvent actionEvent) {
        StyleManager.getInstance().removeUserAgentStylesheet("Styles/Button.css");
        StyleManager.getInstance().addUserAgentStylesheet("Styles/DarkMode.css");

    }

    @FXML void lightMode(ActionEvent actionEvent){
        StyleManager.getInstance().removeUserAgentStylesheet("Styles/DarkMode.css");
        StyleManager.getInstance().addUserAgentStylesheet("Styles/Button.css");


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
            SystemModel.openAirport(airportPath);
            Airport airport = SystemModel.getAirport();
            this.airportName.setText(airport.getName());
            this.airportContainer.updateAirport(airport);
            this.mainBox.setVisible(false);
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
    public void createAirport(ActionEvent event) { this.createAirportListener.openCreateDialogue();
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
    
    /**
     * Ran when user attempts to delete tarmac for current airport, warn them otherwise execute.
     * @param runway runway of the tarmac to delete
     */
    private void deleteTarmac(Runway runway) {
        SystemModel.deleteTarmac(runway);
        logger.info("Deleted runway {}", runway.getRunwayDesignator());
        try {
            this.airportContainer.updateAirport(airportContainer.getAirport());
        } catch (Exception e) {
            logger.error("Could not update airport container: {}", e.getMessage());
        }
    }

    /**
     * Used as a listener for when the user selects "Add Tarmac" in the airport container.
     */
    public void openAddTarmacDialogue(int tarmacID) {
        if (this.createTarmacListener != null) {
            this.createTarmacListener.openDialogue(tarmacID);
        }
    }

    /**
     * Set the create tarmac listener to open dialogue.
     * @param listener listener
     */
    public void setCreateTarmacListener(CreateTarmacListener listener) {
        this.createTarmacListener = listener;
    }

    /**
     * Set the error listener to show error messages.
     * @param listener listener
     */
    public void setErrorListener(ErrorListener listener) { this.errorListener = listener; }

    /**
     * Get the error listener.
     * @return error listener
     */
    public ErrorListener getErrorListener() { return this.errorListener; }
    
    /**
     * Set the warning listener to show warnings and receive confirmation.
     * @param listener listener
     */
    public void setWarningListener(WarningListener listener) { this.warningListener = listener; }

    /**
     * Get the warning listener.
     * @return warning listener
     */
    public WarningListener getWarningListener() { return this.warningListener; }
    
    /**
     * Set the message listener to display an arbitrary message to the user.
     * @param listener listener
     */
    public void setMessageListener(MessageListener listener) { this.messageListener = listener; }

    /**
     * Get the message listener.
     * @return message listener
     */
    public MessageListener getMessageListener() { return this.messageListener; }

    /**
     * Set tarmac deletion warning listener
     * @param listener listener
     */
    public void setTarmacDeleteWarning(WarningListener listener) {
        this.tarmacDeleteWarning = listener;
        this.airportContainer.setDeletionWarningListeners(this.tarmacDeleteWarning);
    }

    /**
     * Not a reset, more of a refresh tbh
     */
    public void resetAirport() {
        this.airportContainer.updateAirport(this.airportContainer.getAirport());
        logger.info("Reset (refreshed) airport.");
    }
    
    public void addNotification(String notif) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        
        notif = timestamp.toString() + " : " + notif;
        DataWriter.writeNotification(notif);
        try {
            this.notbox.addNotifications(DataReader.getNotifications());
        } catch (LoadingException e) {
            logger.error("Could not read notifications file: " + e.getMessage());
        }
    }

    /**
     * Get the name of the airport for use in other parts of the program like notifications.
     * @return name of the current airport.
     */
    public String getAirportName() {
        return this.airportName.getText();
    }
}
