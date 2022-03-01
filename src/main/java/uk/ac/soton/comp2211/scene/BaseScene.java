package uk.ac.soton.comp2211.scene;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * The basic Scene object for the application.
 */
public class BaseScene extends Scene {
    
    public BaseScene() {
        super(new Pane(), 800, 600);
    }
}
