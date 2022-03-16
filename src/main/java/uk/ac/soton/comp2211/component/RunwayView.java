package uk.ac.soton.comp2211.component;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import uk.ac.soton.comp2211.model.Runway;
import uk.ac.soton.comp2211.model.RunwayValues;


/**
 * Custom component to draw runway visualisation from parameters.
 */
public class RunwayView extends Group {
    
    private Runway runway;
    private final Canvas canvas;
    private GraphicsContext gc;
    private final double w;
    private final double h;
    
    public RunwayView(double width, double height, Runway runway) {
        super();
        
        this.runway = runway;
        this.w = width;
        this.h = height;
        this.canvas = new Canvas(width, height);
        this.gc = this.canvas.getGraphicsContext2D();
        
        this.getChildren().add(this.canvas);
    }
    
    public void updateTopDown() {
        // Reset canvas
        this.gc.setFill(Color.WHITE);
        this.gc.fillRect(0, 0, this.w, this.h);
        
        // Draw tarmac
        int l = this.runway.getLength();
        int w = this.runway.getWidth();
        this.gc.setFill(Color.GRAY);
        this.gc.fillRect((this.w/2) - (l/12.0), (this.h/2) - (w/12.0), l/6.0, w/6.0);
        this.gc.strokeRect((this.w/2) - (l/12.0), (this.h/2) - (w/12.0), l/6.0, w/6.0);

        // Draw parameters
        RunwayValues values = this.runway.getCurrentValues();
        int toda = values.getTODA();
        int tora = values.getTORA();
        int asda = values.getASDA();
        int lda = values.getLDA();
        this.gc.setFill(Color.WHITE);
        this.gc.strokeLine((this.w/2) - (toda/12.0), 30, (this.w/2) + (toda/12.0), 30);
        this.gc.fillRect((this.w/2) - (l/12.0) + (toda/12.0) - 20, 20, 80, 20);
        this.gc.strokeText("TODA: " + toda + "m", (this.w/2) - (l/12.0) + (toda/12.0) - 15, 35);
        this.gc.strokeLine((this.w/2) - (toda/12.0), 70, (this.w/2) - (toda/12.0) + (tora/6.0), 70);
        this.gc.fillRect((this.w/2) - (l/12.0) + (toda/12.0) - 20, 60, 80, 20);
        this.gc.strokeText("TORA: " + tora + "m", (this.w/2) - (l/12.0) + (toda/12.0) - 15, 75);
        this.gc.strokeLine((this.w/2) - (toda/12.0), 50, (this.w/2) - (toda/12.0) + (asda/6.0), 50);
        this.gc.fillRect((this.w/2) - (l/12.0) + (toda/12.0) - 20, 40, 80, 20);
        this.gc.strokeText("ASDA: " + asda + "m", (this.w/2) - (l/12.0) + (toda/12.0) - 15, 55);
        this.gc.strokeLine((this.w/2) + (toda/12.0) - (lda/6.0), 90, (this.w/2) + (toda/12.0), 90);
        this.gc.fillRect((this.w/2) + (l/12.0) - (lda/12.0) - 20, 80, 80, 20);
        this.gc.strokeText("LDA: " + lda + "m", (this.w/2) + (l/12.0) - (lda/12.0) - 15, 95);
        
        // Draw displaced threshold
        int dt = this.runway.getDisplacedThreshold();
        
        // Draw clearways
        // Draw stop-ways
        // Draw scale indicator
        // Draw compass
        
        // Put a final border around the canvas
        this.gc.setStroke(Color.BLACK);
        this.gc.strokeRect(0, 0, this.w - 1, this.h - 1);
    }
    
}
