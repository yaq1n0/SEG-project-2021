package uk.ac.soton.comp2211.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.event.*;
import uk.ac.soton.comp2211.model.Airport;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.Tarmac;

import java.util.List;

/**
 * Component containing all the Runway objects for an airport.
 */
public class AirportContainer extends VBox {

    private final BooleanProperty topView = new SimpleBooleanProperty();
    private final BooleanProperty colour = new SimpleBooleanProperty();
    private RunwayContainer[] runwayContainers;
    private DeleteTarmacListener deleteTarmacListener;
    private WarningListener warnDeletionListener;
    private AddTarmacListener addTarmacListener;
    private Stage stage;
    private Airport airport;
    private NotificationListener notificationListener;

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

    public void bindColourProperty (BooleanProperty viewProperty) {
        this.colour.bind(viewProperty);
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
            RunwayContainer runwayContainer = new RunwayContainer(rws[i], this.stage, this.airport.getName());
            runwayContainer.bindViewProperty(this.topView);
            runwayContainer.bindColourProperty(this.colour);
            runwayContainer.setDeleteTarmacListener(this.deleteTarmacListener);
            runwayContainer.setDeletionWarningListener(this.warnDeletionListener);
            runwayContainer.setNotificationListener(this.notificationListener);
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
        // Airports can have a maximum of 3 tarmacs with their naming convention
        if (this.airport.getTarmacs().size() >= 3) addTarmac.setDisable(true);
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
     * Set the deletion warning listener, which will pass on to each runway container as it gets created.
     * @param listener listener
     */
    public void setDeletionWarningListeners(WarningListener listener) {
        this.warnDeletionListener = listener;
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

    /**
     * Set notification listener
     * @param listener listener
     */
    public void setNotificationListener(NotificationListener listener) {
        this.notificationListener = listener;
    }
}
