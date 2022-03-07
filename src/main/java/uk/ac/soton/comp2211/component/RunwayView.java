package uk.ac.soton.comp2211.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;

import javafx.event.ActionEvent;

/**
 * Custom component to draw runway visualisation from parameters.
 */
public class RunwayView extends Canvas {
    
    // private Runway runway;
    private final BooleanProperty topView;
    
    public RunwayView() {
        super(600, 400);
        topView = new SimpleBooleanProperty(true);
    }

    /**
     * Bind view property from controller. True:Top, False:Side
     */
    public void bindViewProperty (BooleanProperty viewProperty) { 
        this.topView.bind(viewProperty);
    }

    /**
     * Redraw the runway in the correct view with current parameters.
     */
    public void update(ActionEvent actionEvent) {
        if (this.topView.get()) {
            System.out.println("Draw Top Down");
        } else {
            System.out.println("Draw Side On");
        }
    }
    
}
