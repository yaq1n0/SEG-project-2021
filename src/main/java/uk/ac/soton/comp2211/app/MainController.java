package uk.ac.soton.comp2211.app;

import javafx.application.Platform;
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
            
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.runwayView = new RunwayView();
        this.runwayContainer.getChildren().add(this.runwayView);
        this.testButton.setOnAction(this::doSomething);
    }

    private void doSomething(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            GraphicsContext gc = this.runwayView.getGraphicsContext2D();
            gc.setFill(Color.RED);
            gc.fillRect(0, 60,0, 80);
        });
    }
}
