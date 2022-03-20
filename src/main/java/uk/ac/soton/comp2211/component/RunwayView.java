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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.Position;
import uk.ac.soton.comp2211.model.Runway;


/**
 * Custom component to draw runway visualisation from parameters.
 */
public class RunwayView extends Group {

    protected static final Logger logger = LogManager.getLogger(RunwayView.class);
    
    private Runway runway;
    private final Canvas canvas;
    private GraphicsContext gc;
    private final double w;
    private final double h;

    //Handle size of runway and values lines
    private double runwayStart;
    private double runwayRepresentationSize;
    private double clearwaySize;
    private double stopwaySize;
    private double dtSize;
    
    public RunwayView(double width, double height, Runway runway) {
        super();
        
        this.runway = runway;
        this.w = width;
        this.h = height;
        this.canvas = new Canvas(width, height);
        this.gc = this.canvas.getGraphicsContext2D();
        
        this.getChildren().add(this.canvas);

        //Handle size of runway and values lines
        runwayStart = this.w * 0.15;
        runwayRepresentationSize = this.w * 0.7;
        clearwaySize = Math.min(runway.getClearway()/3, this.w*0.10);
        stopwaySize = Math.min(runway.getStopway()/3, this.w*0.10);
        dtSize = Math.min(runway.getDisplacedThreshold()/6, runwayRepresentationSize);
    }
    
    public void updateTopDown() {
        // Reset canvas
        this.getChildren().clear();
        Rectangle bg = new Rectangle(0, 0, this.w, this.h);
        bg.setFill(Color.LIGHTGREEN);
        this.getChildren().add(bg);

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

        // Draw tarmac
        Rectangle runwayRectangle = new Rectangle();
        runwayRectangle.setFill(Color.GRAY);
        runwayRectangle.setX(runwayStart);
        runwayRectangle.setY(this.h * 0.45);
        runwayRectangle.setWidth(runwayRepresentationSize);
        runwayRectangle.setHeight(this.h *0.1);
        this.getChildren().add(runwayRectangle);
        runwayRectangle.toFront();
        Text runwayDesignatorText = new Text(runway.getRunwayDesignator());
        runwayDesignatorText.setFont(Font.font("Jersey", FontWeight.BOLD, 15));
        runwayDesignatorText.setX(runwayStart+10);
        runwayDesignatorText.setY(this.h*0.52);
        runwayDesignatorText.setRotate(90);
        runwayDesignatorText.setFill(Color.WHITE);
        this.getChildren().add(runwayDesignatorText);

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
        drawValueLines();

        // Draw displaced threshold
        if (runway.getDisplacedThreshold()>0) {
            Line dtLine = new Line(runwayStart+(dtSize), this.h*0.5-10, runwayStart+(dtSize), this.h*0.5+10);
            dtLine.setStroke(Color.RED);
            dtLine.setStrokeWidth(5d);
            this.getChildren().add(dtLine);
        }

        // Draw clearway
        if (runway.getClearway()>0) {
            Rectangle clearwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.35, clearwaySize, this.h*0.3);
            clearwayRec.setFill(Color.CORAL);
            this.getChildren().add(clearwayRec);
        }

        // Draw stop-way
        if (runway.getStopway()>0) {
            Rectangle stopwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.40, stopwaySize, this.h*0.2);
            stopwayRec.setFill(Color.CYAN);
            this.getChildren().add(stopwayRec);
            stopwayRec.toFront();
        }
        
        // Draw obstacle
        Obstacle obs = runway.getTarmac().getObstacle();
        logger.info("Obstacle: {}", obs);
        if (obs != null) {
            try {
                Position pos = obs.getPosition();
                Rectangle obsRect = new Rectangle();
                obsRect.setX(runwayStart + (pos.getDistanceFromWest() * this.runwayRepresentationSize / this.runway.getLength()));
                obsRect.setY((this.h * 0.5) + (pos.getCentreLineDisplacement() * this.runwayRepresentationSize / this.runway.getLength()));
                obsRect.setHeight(obs.getWidth() * this.runwayRepresentationSize / this.runway.getLength());
                obsRect.setWidth(obs.getLength() * this.runwayRepresentationSize / this.runway.getLength());
                obsRect.setFill(Color.RED);
                obsRect.setStroke(Color.BLACK);
                this.getChildren().add(obsRect);
                logger.info("Obstacle drawn to top-down.");
            } catch (PositionException ignored) {
                logger.info("Obstacle has not position, so is not being drawn.");
            }
        }



        

        // Draw scale indicator
        // Draw compass

        // Draw color legend

        // Draw take-off direction and landing direction

        // Draw border
        Rectangle border = new Rectangle(0, 0, this.w, this.h);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        this.getChildren().add(border);
        
    }
    
    public void updateSideOn() {
        // Reset canvas and draw ground
        this.getChildren().clear();
        Rectangle bg = new Rectangle(0, this.h*0.60, this.w, this.h*0.40);
        bg.setFill(Color.LIGHTGREEN);
        this.getChildren().add(bg);

        //Draw sky
        Rectangle sky = new Rectangle(0, 0, this.w, this.h*0.60);
        sky.setFill(Color.LIGHTBLUE);
        this.getChildren().add(sky);

        //Draw value lines
        drawValueLines();

        // Draw tarmac
        Rectangle runwayRectangle = new Rectangle();
        runwayRectangle.setFill(Color.GRAY);
        runwayRectangle.setX(runwayStart);
        runwayRectangle.setY(this.h * 0.60);
        runwayRectangle.setWidth(runwayRepresentationSize);
        runwayRectangle.setHeight(this.h *0.05);
        this.getChildren().add(runwayRectangle);
        runwayRectangle.toFront();

        // Draw displaced threshold
        if (runway.getDisplacedThreshold()>0) {
            Line dtLine = new Line(runwayStart+(dtSize), this.h*0.61, runwayStart+(dtSize), this.h*0.64);
            dtLine.setStroke(Color.RED);
            dtLine.setStrokeWidth(5d);
            this.getChildren().add(dtLine);
        }

        // Draw clearway
        if (runway.getClearway()>0) {
            Rectangle clearwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.6, clearwaySize, this.h*0.20);
            clearwayRec.setFill(Color.CORAL);
            this.getChildren().add(clearwayRec);
        }

        // Draw stop-way
        if (runway.getStopway()>0) {
            Rectangle stopwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.6, stopwaySize, this.h*0.12);
            stopwayRec.setFill(Color.CYAN);
            this.getChildren().add(stopwayRec);
            stopwayRec.toFront();
        }

        // Draw border
        Rectangle border = new Rectangle(0, 0, this.w, this.h);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        this.getChildren().add(border);

    }

    private void drawValueLines() {
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
        Line todaLine = new Line(runwayStart, 50, runwayStart+runwayRepresentationSize+(clearwaySize), 50);
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
        Line asdaLine = new Line(runwayStart, 70, runwayStart+runwayRepresentationSize+(stopwaySize), 70);
        asdaLine.setStroke(Color.PURPLE);
        asdaLine.setStrokeWidth(3d);
        this.getChildren().add(asdaLine);

        //lda
        Text ldaText = new Text("LDA: " + runway.getCurrentValues().getLDA() + "m");
        ldaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        ldaText.setX(runwayStart+(dtSize));
        ldaText.setY(85);
        ldaText.setFill(Color.BLACK);
        this.getChildren().add(ldaText);
        Line ldaLine = new Line(runwayStart+(dtSize), 90, runwayStart+runwayRepresentationSize, 90);
        ldaLine.setStroke(Color.PINK);
        ldaLine.setStrokeWidth(3d);
        this.getChildren().add(ldaLine);
    }
}
