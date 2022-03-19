package uk.ac.soton.comp2211.component;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
        this.gc.setFill(Color.LIGHTGREEN);
        this.gc.fillRect(0, 0, this.w, this.h);

        // Draw cleared and Graded Area
        Polygon clearedAndGradedArea;
        double[] points = {
                0, 0.3* this.h,
                0.2* this.w, 0.3* this.h,
                0.3* this.w, 0.2* this.h,
                0.7* this.w, 0.2* this.h,
                0.8* this.w, 0.3* this.h,
                this.w, 0.3* this.h,
                this.w, 0.7* this.h,
                0.8* this.w, 0.7* this.h,
                0.7* this.w, 0.8* this.h,
                0.3* this.w, 0.8* this.h,
                0.2* this.w, 0.7* this.h,
                0, 0.7* this.h};
        clearedAndGradedArea = new Polygon(points);
        clearedAndGradedArea.setFill(Color.LIGHTBLUE);
        this.getChildren().add(clearedAndGradedArea);

        //Handle size of runway and values lines
        Double runwayStart = this.w * 0.15;
        Double runwayRepresentationSize = this.w * 0.7;

        // Draw tarmac
        Rectangle runwayRectangle = new Rectangle();
        runwayRectangle.setFill(Color.GRAY);
        runwayRectangle.setX(runwayStart);
        runwayRectangle.setY(this.h * 0.45);
        runwayRectangle.setWidth(runwayRepresentationSize);
        runwayRectangle.setHeight(this.h *0.1);
        this.getChildren().add(runwayRectangle);
        runwayRectangle.toFront();

        //Draw line middle tarmac
        Line runwayCentreLine = new Line();
        runwayCentreLine.setStroke(Color.WHITE);
        runwayCentreLine.setStrokeWidth(5d);
        runwayCentreLine.getStrokeDashArray().addAll(20d, 20d);
        runwayCentreLine.setStartX(runwayStart+0.1*runwayRepresentationSize);
        runwayCentreLine.setStartY(this.h * 0.5-1);
        runwayCentreLine.setEndX(this.w *0.85-0.1*runwayRepresentationSize);
        runwayCentreLine.setEndY(this.h *0.5-1);
        this.getChildren().add(runwayCentreLine);

        //Draw value lines
        //tora
        Text toraText = new Text("TORA: " + runway.getCurrentValues().getTORA() + "m");
        toraText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        toraText.setX(runwayStart);
        toraText.setY(25);
        toraText.setFill(Color.BLACK);
        this.getChildren().add(toraText);
        //still need to account for obstacle placement that would change either start or end of value line
        Line toraLine = new Line(runwayStart, 30, runwayStart+runwayRepresentationSize, 30);
        toraLine.setStroke(Color.BLUE);
        toraLine.setStrokeWidth(3d);
        this.getChildren().add(toraLine);

        //toda
        Text todaText = new Text("TODA: " + runway.getCurrentValues().getTODA() + "m");
        todaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        todaText.setX(runwayStart);
        todaText.setY(45);
        todaText.setFill(Color.BLACK);
        this.getChildren().add(todaText);
        Line todaLine = new Line(runwayStart, 50, runwayStart+runwayRepresentationSize+(runway.getClearway()), 50);
        todaLine.setStroke(Color.YELLOW);
        todaLine.setStrokeWidth(3d);
        this.getChildren().add(todaLine);

        //asda
        Text asdaText = new Text("ASDA: " + runway.getCurrentValues().getASDA() + "m");
        asdaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        asdaText.setX(runwayStart);
        asdaText.setY(65);
        asdaText.setFill(Color.BLACK);
        this.getChildren().add(asdaText);
        Line asdaLine = new Line(runwayStart, 70, runwayStart+runwayRepresentationSize+(runway.getStopway()), 70);
        asdaLine.setStroke(Color.PURPLE);
        asdaLine.setStrokeWidth(3d);
        this.getChildren().add(asdaLine);

        //lda
        Text ldaText = new Text("LDA: " + runway.getCurrentValues().getLDA() + "m");
        ldaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        ldaText.setX(runwayStart+(runway.getDisplacedThreshold()/6));
        ldaText.setY(85);
        ldaText.setFill(Color.BLACK);
        this.getChildren().add(ldaText);
        Line ldaLine = new Line(runwayStart+(runway.getDisplacedThreshold()/6), 90, runwayStart+runwayRepresentationSize, 90);
        ldaLine.setStroke(Color.PINK);
        ldaLine.setStrokeWidth(3d);
        this.getChildren().add(ldaLine);

        // Draw displaced threshold
        if (runway.getDisplacedThreshold()>0) {
            Line dtLine = new Line(runwayStart+(runway.getDisplacedThreshold()/6), this.h*0.5-10, runwayStart+(runway.getDisplacedThreshold()/6), this.h*0.5+10);
            dtLine.setStroke(Color.RED);
            dtLine.setStrokeWidth(5d);
            this.getChildren().add(dtLine);
        }

        // Draw clearway
        if (runway.getClearway()>0) {
            //still need to account for bigger clearways/stopways
            Rectangle clearwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.35, runway.getClearway(), this.h*0.3);
            clearwayRec.setFill(Color.CORAL);
            this.getChildren().add(clearwayRec);
        }

        // Draw stop-way
        if (runway.getStopway()>0) {
            Rectangle stopwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.40, runway.getStopway(), this.h*0.2);
            stopwayRec.setFill(Color.CYAN);
            this.getChildren().add(stopwayRec);
            stopwayRec.toFront();
        }

        //draw obstacle

        //if (runway.getTarmac().getObstacle() != null)
        //runway.getTarmac().getObstacle().getPosition();



        /*
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

         */
        
        // Put a final border around the canvas
        this.gc.setStroke(Color.BLACK);
        this.gc.strokeRect(0, 0, this.w - 1, this.h - 1);
    }
    
}
