package uk.ac.soton.comp2211.component;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * Custom component to draw runway visualisation from parameters.
 */
public class RunwayView extends Group {
    
    private final Canvas canvas;
    private GraphicsContext gc;
    
    public RunwayView(double width, double height) {
        super();
        
        this.canvas = new Canvas(width, height);
        this.gc = this.canvas.getGraphicsContext2D();
        
        this.getChildren().add(this.canvas);
    }
    
    public void test() {
        this.gc.setFill(Color.BLUE);
        this.gc.fillRect(0, 0, 1000, 1000);
    }
    
}
