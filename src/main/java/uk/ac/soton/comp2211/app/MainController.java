package uk.ac.soton.comp2211.app;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.component.RunwayView;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainController.class);
    
    @FXML
    private VBox runwayContainer;
    @FXML
    private Button testButton;
    
    private RunwayView runwayView;
    private final BooleanProperty topView;

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
}
