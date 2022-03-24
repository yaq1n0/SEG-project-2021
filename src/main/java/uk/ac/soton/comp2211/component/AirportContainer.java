package uk.ac.soton.comp2211.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.event.AddTarmacListener;
import uk.ac.soton.comp2211.event.DeleteTarmacListener;
import uk.ac.soton.comp2211.event.InsertObstacleListener;
import uk.ac.soton.comp2211.model.Airport;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.Tarmac;

import java.util.List;

/**
 * Component containing all the Runway objects for an airport.
 */
public class AirportContainer extends VBox {

    private final BooleanProperty topView = new SimpleBooleanProperty();
    private RunwayContainer[] runwayContainers;
    private DeleteTarmacListener deleteTarmacListener;
    private AddTarmacListener addTarmacListener;
    private Stage stage;
    private Airport airport;

    public AirportContainer(Stage stage) {
        this.topView.set(true);
        this.stage = stage;
    }

    /**
     * Bind view property from controller. True:Top, False:Side
     */
    public void bindViewProperty(BooleanProperty viewProperty) {
        this.topView.bind(viewProperty);
    }

    /**
     * Provide the component with all information necessary to be built.
     *
     * @param airport Airport to display data for.
     */
    public void updateAirport(Airport airport) {
        this.airport = airport;
        
        this.getChildren().clear();
        Runway[] rws = airport.getRunways();
        this.runwayContainers = new RunwayContainer[rws.length];

        for (int i = 0; i < rws.length; i++) {
            RunwayContainer runwayContainer = new RunwayContainer(rws[i], this.stage);
            runwayContainer.bindViewProperty(this.topView);
            runwayContainer.setDeleteTarmacListener(this.deleteTarmacListener);
            VBox.setVgrow(runwayContainer, Priority.ALWAYS);
            this.getChildren().add(runwayContainer);
            this.runwayContainers[i] = runwayContainer;
        }
        
        Button addTarmac = new Button("Add Tarmac");
        addTarmac.setOnAction((ActionEvent event) -> {
            if (this.addTarmacListener != null) {
                List<Tarmac> ts = this.airport.getTarmacs();
                int tid = ts.get(ts.size() - 1).getID() + 1;
                this.addTarmacListener.openAddTarmac(tid);
            }
        });
        this.getChildren().add(addTarmac);
    }

    /**
     * Delete all runway containers.
     */
    public void closeAirport() {
        this.getChildren().clear();
    }

    /**
     * Set the tarmac deletion listener, which will pass on to each runway container as it gets created.
     * @param listener listener
     */
    public void setDeleteTarmacListeners(DeleteTarmacListener listener) {
        this.deleteTarmacListener = listener;
    }

    /**
     * Set the tarmac addition listener for the airport.
     * @param listener listener
     */
    public void setAddTarmacListener(AddTarmacListener listener) {
        this.addTarmacListener = listener;
    }

    /**
     * Getter for the current airport.
     * @return airport object
     */
    public Airport getAirport() {
        return this.airport;
    }
}
