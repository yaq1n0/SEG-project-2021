package uk.ac.soton.comp2211.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.model.Airport;
import uk.ac.soton.comp2211.model.Runway;

/**
 * Component containing all the Runway objects for an airport.
 */
public class AirportContainer extends VBox {
    
    private final BooleanProperty topView = new SimpleBooleanProperty();
    private RunwayContainer[] runwayContainers;
    
    public AirportContainer() {
        this.topView.set(true);
    }

    /**
     * Bind view property from controller. True:Top, False:Side
     */
    public void bindViewProperty (BooleanProperty viewProperty) {
        this.topView.bind(viewProperty);
    }

    /**
     * Provide the component with all information necessary to be built.
     * @param airport Airport to display data for.
     */
    public void updateAirport(Airport airport) {
        this.getChildren().clear();
        Runway[] rws = airport.getRunways();
        this.runwayContainers = new RunwayContainer[rws.length];
        
        for (int i = 0; i < rws.length; i++) {
            RunwayContainer runwayContainer = new RunwayContainer(rws[i]);
            VBox.setVgrow(runwayContainer, Priority.ALWAYS);
            this.getChildren().add(runwayContainer);
            this.runwayContainers[i] = runwayContainer;
        }
    }

    /**
     * Delete all runway containers.
     */
    public void closeAirport() {
        this.getChildren().clear();
    }
    
}
