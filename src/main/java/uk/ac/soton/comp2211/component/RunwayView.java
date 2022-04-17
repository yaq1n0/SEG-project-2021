package uk.ac.soton.comp2211.component;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
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
    
    private final Runway runway;
    private final double w;
    private final double h;

    //Handle size of runway and values lines
    private final double runwayStart;
    private final double runwayRepresentationSize;
    private final double clearwaySize;
    private final double stopwaySize;
    private final double dtSize;
    
    // Scale will be used to zoom in/out.
    private double scale;
    // Offset will be used to pan around the view.
    private int offset_x;
    private int offset_y;
    // (x * scale) * offset_x, (y * scale) + offset_y
    
    public RunwayView(double width, double height, Runway runway) {
        super();
        
        this.runway = runway;
        this.w = width;
        this.h = height;

        //Handle size of runway and values lines
        runwayStart = this.w * 0.15;
        runwayRepresentationSize = this.w * 0.7;
        clearwaySize = Math.min(runway.getOriginalValues().getClearway()/3.0, this.w*0.10);
        stopwaySize = Math.min(runway.getOriginalValues().getStopway()/3.0, this.w*0.10);
        dtSize = this.runway.getOriginalValues().getDT()*runwayRepresentationSize/runway.getLength();
        
        this.scale = 1.0;
        this.offset_x = 0;
        this.offset_y = 0;
    }
    
    public void updateTopDown() {
        // Reset view
        this.getChildren().clear();
        Rectangle bg = new Rectangle(0, 0, this.w, this.h);
        bg.setFill(Color.LIGHTGREEN);
        this.getChildren().add(bg);

        // Draw cleared and Graded Area
        Polygon clearedAndGradedArea;
        double[] points = {
                Math.max(Math.min(offset_x, w), 0), Math.max(Math.min((0.3 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.2 * this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.3 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.3 * this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.2 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.7 * this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.2 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.8 * this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.3 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.3 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.7 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.8 * this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.7 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.7 * this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.8 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.3 * this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.8 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.2 * this.w * scale) + offset_x, w), 0), Math.max(Math.min((0.7 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min(offset_x, w), 0), Math.max(Math.min((0.7 * this.h * scale) + offset_y, h), 0)};
        clearedAndGradedArea = new Polygon(points);
        clearedAndGradedArea.setFill(Color.LIGHTBLUE);
        this.getChildren().add(clearedAndGradedArea);

        // Draw tarmac
        Rectangle runwayRectangle = new Rectangle();
        runwayRectangle.setFill(Color.GRAY);
        runwayRectangle.setX(Math.max(Math.min((runwayStart * scale) + offset_x, w), 0));
        runwayRectangle.setY(Math.max(Math.min((h * 0.45 * scale) + offset_x, h), 0));
        runwayRectangle.setWidth(Math.max(Math.min(runwayRepresentationSize * scale, w - runwayRectangle.getX()), 0));
        runwayRectangle.setHeight(Math.max(Math.min(this.h * 0.1 * scale, h - runwayRectangle.getY()), 0));
        this.getChildren().add(runwayRectangle);
        runwayRectangle.toFront();
        Text runwayDesignatorText = new Text(runway.getRunwayDesignator());
        runwayDesignatorText.setFont(Font.font("Jersey", FontWeight.BOLD, 15 * scale));
        runwayDesignatorText.setX(Math.max(Math.min(((runwayStart + 10) * scale) + offset_x, w), 0));
        runwayDesignatorText.setY(Math.max(Math.min((this.h * 0.52 * scale) + offset_y, h), 0));
        runwayDesignatorText.setRotate(90);
        runwayDesignatorText.setFill(Color.WHITE);
        this.getChildren().add(runwayDesignatorText);

        //Draw line middle tarmac
        Line runwayCentreLine = new Line();
        runwayCentreLine.setStroke(Color.WHITE);
        runwayCentreLine.setStrokeWidth(5d * scale);
        runwayCentreLine.getStrokeDashArray().addAll(20d * scale, 20d * scale);
        runwayCentreLine.setStartX(Math.max(Math.min(((runwayStart + 0.1 * runwayRepresentationSize) * scale) + offset_x, w), 0));
        runwayCentreLine.setStartY(Math.max(Math.min(((h * 0.5 - 1) * scale) + offset_y, h), 0));
        runwayCentreLine.setEndX(Math.max(Math.min(((w * 0.85 - 0.1 * runwayRepresentationSize) * scale) + offset_x, w), 0));
        runwayCentreLine.setEndY(Math.max(Math.min(((h * 0.5 - 1) * scale) + offset_y, h), 0));
        this.getChildren().add(runwayCentreLine);

        //Draw value lines
        drawValueLines();

        // Draw displaced threshold
        if (runway.getOriginalValues().getDT()>0) {
            Line dtLine = new Line();
            dtLine.setStartX(Math.max(Math.min(((runwayStart + dtSize) * scale) + offset_x, w), 0));
            dtLine.setStartY(Math.max(Math.min(((h * 0.5 - 10) * scale) + offset_y, h), 0));
            dtLine.setEndX(Math.max(Math.min(((runwayStart + dtSize) * scale) + offset_x, w), 0));
            dtLine.setEndY(Math.max(Math.min(((h * 0.5 + 10) * scale) + offset_y, h), 0));
            dtLine.setStroke(Color.RED);
            dtLine.setStrokeWidth(5d * scale);
            this.getChildren().add(dtLine);
        }

        // Draw clearway
        if (runway.getOriginalValues().getClearway()>0) {
            Rectangle clearwayRec = new Rectangle();
            clearwayRec.setX(Math.max(Math.min(((runwayStart + runwayRepresentationSize) * scale) + offset_x, w), 0));
            clearwayRec.setY(Math.max(Math.min((h * 0.4 * scale) + offset_y, h), 0));
            clearwayRec.setWidth(Math.max(Math.min(clearwaySize * scale, w - clearwayRec.getX()), 0));
            clearwayRec.setHeight(Math.max(Math.min(h * 0.2 * scale, h - clearwayRec.getY()), 0));
            clearwayRec.setFill(Color.CORAL);
            this.getChildren().add(clearwayRec);
        }

        // Draw stop-way
        if (runway.getOriginalValues().getStopway()>0) {
            Rectangle stopwayRec = new Rectangle();
            stopwayRec.setX(Math.max(Math.min(((runwayStart + runwayRepresentationSize) * scale) + offset_x, w), 0));
            stopwayRec.setY(Math.max(Math.min((h * 0.45 * scale) + offset_y, h), 0));
            stopwayRec.setWidth(Math.max(Math.min(stopwaySize * scale, w - stopwayRec.getX()), 0));
            stopwayRec.setHeight(Math.max(Math.min(h * 0.1 * scale, h - stopwayRec.getY()), 0));
            stopwayRec.setFill(Color.CYAN);
            this.getChildren().add(stopwayRec);
            stopwayRec.toFront();
        }
        
        // Draw obstacle
        Obstacle obs = runway.getTarmac().getObstacle();
        if (obs != null) {
            try {
                Position pos = obs.getPosition();
                Rectangle obsRect = new Rectangle();
                obsRect.setX(Math.max(Math.min(((runwayStart + dtSize + ((pos.getDistance() - (obs.getWidth() / 2.0)) * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                obsRect.setY(Math.max(Math.min((((h * 0.5) + ((pos.getCentreLineDisplacement() - (obs.getLength() / 2.0)) * runwayRepresentationSize / runway.getLength())) * scale) + offset_y, h), 0));
                obsRect.setHeight(Math.max(Math.min(obs.getWidth() * runwayRepresentationSize / runway.getLength() * scale, w - obsRect.getX()), 0));
                obsRect.setWidth(Math.max(Math.min(obs.getLength() * runwayRepresentationSize / runway.getLength() * scale, h - obsRect.getY()), 0));
                obsRect.setFill(Color.RED);
                obsRect.setStroke(Color.BLACK);
                this.getChildren().add(obsRect);
                logger.info("Obstacle drawn to top-down.");

                //Draw obstacle dotted line
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                Line dottedObsline = new Line(runwayStart+dtSize+ dWestRepresentation, this.h*0.5, runwayStart+dtSize+ dWestRepresentation, this.h*0.75);
                dottedObsline.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0));
                dottedObsline.setStartY(Math.max(Math.min((h * 0.5 * scale) + offset_y, h), 0));
                dottedObsline.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0));
                dottedObsline.setEndY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                dottedObsline.setStroke(Color.WHITE);
                dottedObsline.setStrokeWidth(2d * scale);
                dottedObsline.getStrokeDashArray().addAll(7d * scale, 7d * scale);
                this.getChildren().add(dottedObsline);

            } catch (PositionException ignored) {
                logger.info("Obstacle has not position, so is not being drawn.");
            }
        }
        
        // Draw Take-off/Landing direction
        Polygon direction;
        double[] ps = {
                20, 0.11 * this.h,
                60, 0.11 * this.h,
                60, 0.075 * this.h,
                80, 0.125 * this.h,
                60, 0.175 * this.h,
                60, 0.14 * this.h,
                20, 0.14 * this.h
        };
        direction = new Polygon(ps);
        direction.setFill(Color.RED);
        this.getChildren().add(direction);


        // Draw scale indicator
        // Draw compass


        // Draw border
        Rectangle border = new Rectangle(0, 0, this.w, this.h);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        this.getChildren().add(border);
        
    }
    
    public void updateSideOn() {
        // Reset view
        this.getChildren().clear();
        // Draw ground
        Rectangle bg = new Rectangle();
        bg.setX(0);
        bg.setY(Math.max(Math.min((h * 0.60 * scale) + offset_y, h), 0));
        bg.setWidth(w);
        bg.setHeight(Math.max(Math.min(h - (h * 0.60 * scale) + offset_y, h - bg.getY()), 0));
        bg.setFill(Color.LIGHTGREEN);
        this.getChildren().add(bg);

        //Draw sky
        Rectangle sky = new Rectangle();
        sky.setX(0);
        sky.setY(0);
        sky.setWidth(w);
        sky.setHeight(Math.max(Math.min((this.h * 0.60 * scale) + offset_y, h), 0));
        sky.setFill(Color.LIGHTBLUE);
        this.getChildren().add(sky);

        //Draw value lines
        drawValueLines();

        // Draw tarmac
        Rectangle runwayRectangle = new Rectangle();
        runwayRectangle.setFill(Color.GRAY);
        runwayRectangle.setX(Math.max(Math.min((runwayStart * scale) + offset_x, w), 0));
        runwayRectangle.setY(Math.max(Math.min((h * 0.60 * scale) + offset_x, h), 0));
        runwayRectangle.setWidth(Math.max(Math.min(runwayRepresentationSize * scale, w - runwayRectangle.getX()), 0));
        runwayRectangle.setHeight(Math.max(Math.min(this.h * 0.05 * scale, h - runwayRectangle.getY()), 0));
        this.getChildren().add(runwayRectangle);
        runwayRectangle.toFront();

        // Draw displaced threshold
        if (runway.getOriginalValues().getDT() > 0) {
            Line dtLine = new Line();
            dtLine.setStartX(Math.max(Math.min(((runwayStart + dtSize) * scale) + offset_x, w), 0));
            dtLine.setStartY(Math.max(Math.min((h * 0.61 * scale) + offset_y, h), 0));
            dtLine.setEndX(Math.max(Math.min(((runwayStart + dtSize) * scale) + offset_x, w), 0));
            dtLine.setEndY(Math.max(Math.min((h * 0.64 * scale) + offset_y, h), 0));
            dtLine.setStroke(Color.RED);
            dtLine.setStrokeWidth(5d * scale);
            this.getChildren().add(dtLine);
        }

        // Draw clearway
        if (runway.getOriginalValues().getClearway()>0) {
            Rectangle clearwayRec = new Rectangle();
            clearwayRec.setX(Math.max(Math.min(((runwayStart + runwayRepresentationSize) * scale) + offset_x, w), 0));
            clearwayRec.setY(Math.max(Math.min((h * 0.6 * scale) + offset_y, h), 0));
            clearwayRec.setWidth(Math.max(Math.min(clearwaySize, w - clearwayRec.getX()), 0));
            clearwayRec.setHeight(Math.max(Math.min(h * 0.2 * scale, h - clearwayRec.getY()), 0));
            clearwayRec.setFill(Color.CORAL);
            this.getChildren().add(clearwayRec);
        }

        // Draw stop-way
        if (runway.getOriginalValues().getStopway()>0) {
            Rectangle stopwayRec = new Rectangle();
            stopwayRec.setX(Math.max(Math.min(((runwayStart + runwayRepresentationSize) * scale) + offset_x, w), 0));
            stopwayRec.setY(Math.max(Math.min((h * 0.6 * scale) + offset_y, h), 0));
            stopwayRec.setWidth(Math.max(Math.min(stopwaySize, w - stopwayRec.getX()), 0));
            stopwayRec.setHeight(Math.max(Math.min(h * 0.12 * scale, h - stopwayRec.getY()), 0));
            stopwayRec.setFill(Color.CYAN);
            this.getChildren().add(stopwayRec);
            stopwayRec.toFront();
        }

        // Draw Take-off/Landing direction
        Polygon direction;
        double[] ps = {
                20, 0.11* this.h,
                60, 0.11* this.h,
                60, 0.075* this.h,
                80, 0.125* this.h,
                60, 0.175* this.h,
                60, 0.14* this.h,
                20, 0.14* this.h
        };
        direction = new Polygon(ps);
        direction.setFill(Color.RED);
        this.getChildren().add(direction);
        
        // Draw Obstacle
        Obstacle obs = runway.getTarmac().getObstacle();
        if (obs != null) {
            try {
                Position pos = obs.getPosition();
                Rectangle obsRect = new Rectangle();
                obsRect.setX(Math.max(Math.min(((runwayStart + dtSize + ((pos.getDistance() - (obs.getWidth() / 2.0)) * this.runwayRepresentationSize / this.runway.getLength())) * scale) + offset_x, w), 0));
                obsRect.setY(Math.max(Math.min((((h * 0.6) - (obs.getHeight() * this.runwayRepresentationSize / this.runway.getLength())) * scale) + offset_y, h), 0));
                obsRect.setHeight(Math.max(Math.min(obs.getHeight() * this.runwayRepresentationSize / this.runway.getLength() * scale, w - obsRect.getX()), 0));
                obsRect.setWidth(Math.max(Math.min(obs.getLength() * this.runwayRepresentationSize / this.runway.getLength() * scale, h - obsRect.getY()), 0));

                obsRect.setFill(Color.RED);
                obsRect.setStroke(Color.BLACK);
                this.getChildren().add(obsRect);
                logger.info("Obstacle drawn to side-on.");

                // draw obstacle dotted line
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                Line dottedObsline = new Line();
                dottedObsline.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0));
                dottedObsline.setStartY(Math.max(Math.min((h * 0.6 * scale) + offset_y, h), 0));
                dottedObsline.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0));
                dottedObsline.setEndY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                dottedObsline.setStroke(Color.WHITE);
                dottedObsline.setStrokeWidth(2d * scale);
                dottedObsline.getStrokeDashArray().addAll(7d * scale, 7d * scale);
                this.getChildren().add(dottedObsline);

            } catch (PositionException ignored) {
                logger.info("Obstacle has not position, so is not being drawn.");
            }
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
        toraText.setY((25 * scale) + offset_y);
        toraText.setFill(Color.BLACK);
        Line toraLine = null;

        //toda
        Text todaText = new Text("TODA: " + runway.getCurrentValues().getTODA() + "m");
        todaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        todaText.setY((45 * scale) + offset_y);
        todaText.setFill(Color.BLACK);
        Line todaLine = null;

        //asda
        Text asdaText = new Text("ASDA: " + runway.getCurrentValues().getASDA() + "m");
        asdaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        asdaText.setY((65 * scale) + offset_y);
        asdaText.setFill(Color.BLACK);
        Line asdaLine = null;

        //lda
        Text ldaText = new Text("LDA: " + runway.getCurrentValues().getLDA() + "m");
        ldaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        ldaText.setY((85 * scale) + offset_y);
        ldaText.setFill(Color.BLACK);
        Line ldaLine = null;

        // handle sizing
        if (runway.getTarmac().getObstacle()!=null && runway.getTarmac().getObstacle().hasValidPosition()) {
            try {
                double slopeOrResa = Math.max(240, this.runway.getTarmac().getObstacle().getHeight()*50);
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                double blastAllowanceR = 300*runwayRepresentationSize/this.runway.getLength();

                if (runway.getTarmac().getObstacle().getPosition().getDistance()>(runway.getLength()-runway.getTarmac().getObstacle().getPosition().getDistance())) {
                    toraLine = new Line();
                    toraLine.setStartX(Math.max(Math.min((runwayStart * scale) + offset_x, w), 0));
                    toraLine.setStartY(Math.max(Math.min((30 * scale) + offset_y, h), 0));
                    toraLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize/runway.getLength())) * scale) + offset_x, w), 0));
                    toraLine.setEndY(Math.max(Math.min((30 * scale) + offset_y, h), 0));
                    toraText.setX(runwayStart);

                    todaLine = new Line();
                    todaLine.setStartX(Math.max(Math.min((runwayStart * scale) + offset_x, w), 0));
                    todaLine.setStartY(Math.max(Math.min((50 * scale) + offset_y, h), 0));
                    todaLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize/runway.getLength())) * scale) + offset_x, w), 0));
                    todaLine.setEndY(Math.max(Math.min((50 * scale) + offset_y, h), 0));
                    todaText.setX(runwayStart);

                    asdaLine = new Line();
                    asdaLine.setStartX(Math.max(Math.min((runwayStart * scale) + offset_x, w), 0));
                    asdaLine.setStartY(Math.max(Math.min((70 * scale) + offset_y, h), 0));
                    asdaLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize/runway.getLength())) * scale) + offset_x, w), 0));
                    asdaLine.setEndY(Math.max(Math.min((70 * scale) + offset_y, h), 0));
                    asdaText.setX(runwayStart);

                    ldaLine = new Line();
                    ldaLine.setStartX(Math.max(Math.min(((runwayStart + dtSize) * scale) + offset_x, w), 0));
                    ldaLine.setStartY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
                    ldaLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - (300 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    ldaLine.setEndY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
                    ldaText.setX(runwayStart+(dtSize));

                    Line slopeLine = new Line();
                    slopeLine.setStartX(Math.max(Math.min(((runwayStart + dtSize+ dWestRepresentation - (slopeOrResa * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    slopeLine.setStartY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                    slopeLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0));
                    slopeLine.setEndY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                    slopeLine.setStrokeWidth(5d * scale);
                    slopeLine.setStroke(Color.AZURE);
                    slopeLine.toFront();
                    this.getChildren().add(slopeLine);

                    Line stripendLine2 = new Line();
                    stripendLine2.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    stripendLine2.setStartY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                    stripendLine2.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - (slopeOrResa * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    stripendLine2.setEndY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                    stripendLine2.setStrokeWidth(5d * scale);
                    stripendLine2.setStroke(Color.DARKGREEN);
                    stripendLine2.toFront();
                    this.getChildren().add(stripendLine2);

                    Line resaLine = new Line();
                    resaLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - (240 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    resaLine.setStartY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                    resaLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0));
                    resaLine.setEndY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                    resaLine.setStrokeWidth(5d * scale);
                    resaLine.setStroke(Color.ORANGE);
                    resaLine.toFront();
                    this.getChildren().add(resaLine);

                    Line stripendLine = new Line();
                    stripendLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - (300 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    stripendLine.setStartY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                    stripendLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - (240 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    stripendLine.setEndY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                    stripendLine.setStrokeWidth(5d * scale);
                    stripendLine.setStroke(Color.DARKGREEN);
                    stripendLine.toFront();
                    this.getChildren().add(stripendLine);

                    Line dottedTake = new Line();
                    dottedTake.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    dottedTake.setStartY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                    dottedTake.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    dottedTake.setEndY(Math.max(Math.min((30 * scale) + offset_y, h), 0));
                    dottedTake.setStroke(Color.WHITE);
                    dottedTake.setStrokeWidth(2d * scale);
                    dottedTake.getStrokeDashArray().addAll(7d * scale, 7d * scale);
                    this.getChildren().add(dottedTake);

                    Line dottedLand = new Line();
                    dottedLand.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - (300 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    dottedLand.setStartY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                    dottedLand.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation - (300 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                    dottedLand.setEndY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
                    dottedLand.setStroke(Color.WHITE);
                    dottedLand.setStrokeWidth(2d * scale);
                    dottedLand.getStrokeDashArray().addAll(7d * scale, 7d * scale);
                    this.getChildren().add(dottedLand);

                } else {
                    toraLine = new Line();
                    toraLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + blastAllowanceR + dWestRepresentation) * scale) + offset_x, w), 0));
                    toraLine.setStartY(Math.max(Math.min((30 * scale) + offset_y, h), 0));
                    toraLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + runwayRepresentationSize) * scale) + offset_x, w), 0));
                    toraLine.setEndY(Math.max(Math.min((30 * scale) + offset_y, h), 0));
                    toraText.setX(((runwayStart + dtSize + blastAllowanceR + dWestRepresentation) * scale) + offset_x);

                    todaLine = new Line();
                    todaLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + blastAllowanceR + dWestRepresentation) * scale) + offset_x, w), 0));
                    todaLine.setStartY(Math.max(Math.min((50 * scale) + offset_y, h), 0));
                    todaLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + runwayRepresentationSize + clearwaySize) * scale) + offset_x, w), 0));
                    todaLine.setEndY(Math.max(Math.min((50 * scale) + offset_y, h), 0));
                    todaText.setX(((runwayStart+dtSize+ blastAllowanceR+ dWestRepresentation) * scale) + offset_x);

                    asdaLine = new Line();
                    asdaLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + blastAllowanceR + dWestRepresentation) * scale) + offset_x, w), 0));
                    asdaLine.setStartY(Math.max(Math.min((70 * scale) + offset_y, h), 0));
                    asdaLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + runwayRepresentationSize + stopwaySize) * scale) + offset_y, h), 0));
                    asdaLine.setEndY(Math.max(Math.min((70 * scale) + offset_y, h), 0));
                    asdaText.setX(((runwayStart+dtSize + blastAllowanceR+ dWestRepresentation) * scale) + offset_x);

                    Line blastLine = new Line();
                    blastLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0));
                    blastLine.setStartY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                    blastLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation + blastAllowanceR) * scale) + offset_x, w), 0));
                    blastLine.setEndY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                    blastLine.setStrokeWidth(5d * scale);
                    blastLine.setStroke(Color.BROWN);
                    blastLine.toFront();
                    this.getChildren().add(blastLine);

                    Line dottedBlastline = new Line();
                    dottedBlastline.setStartY(Math.max(Math.min((runwayStart + dtSize + dWestRepresentation + blastAllowanceR) + offset_x, w), 0));
                    dottedBlastline.setStartY(Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));
                    dottedBlastline.setStartY(Math.max(Math.min((runwayStart + dtSize + dWestRepresentation + blastAllowanceR) + offset_x, w), 0));
                    dottedBlastline.setStartY(Math.max(Math.min((30 * scale) + offset_y, h), 0));
                    dottedBlastline.setStroke(Color.WHITE);
                    dottedBlastline.setStrokeWidth(2d * scale);
                    dottedBlastline.getStrokeDashArray().addAll(7d * scale, 7d * scale);
                    this.getChildren().add(dottedBlastline);

                    if (runway.getOriginalValues().getDT()<dWestRepresentation+(slopeOrResa+60)) {
                        ldaLine = new Line();
                        ldaLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x, w), 0));
                        ldaLine.setStartY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
                        ldaLine.setEndX(Math.max(Math.min(((runwayStart + runwayRepresentationSize) * scale) + offset_x, w), 0));
                        ldaLine.setEndY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
                        ldaText.setX(Math.max(Math.min(((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x, w), 0));

                        Line resaLine = new Line();
                        resaLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0));
                        resaLine.setStartY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                        resaLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + (slopeOrResa * runwayRepresentationSize / runway.getLength())) * scale) + offset_x, w), 0));
                        resaLine.setEndY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                        resaLine.setStrokeWidth(5d * scale);
                        if (slopeOrResa==240) {
                            resaLine.setStroke(Color.ORANGE);
                        } else {
                            resaLine.setStroke(Color.AZURE);
                        }
                        resaLine.toFront();
                        this.getChildren().add(resaLine);

                        Line stripendLine = new Line();
                        stripendLine.setStartX(Math.max(Math.min(((runwayStart + dtSize + (slopeOrResa * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x, w), 0));
                        stripendLine.setStartY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                        stripendLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x, w), 0));
                        stripendLine.setEndY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                        stripendLine.setStrokeWidth(5d * scale);
                        stripendLine.setStroke(Color.DARKGREEN);
                        stripendLine.toFront();
                        this.getChildren().add(stripendLine);

                        Line dottedStripend = new Line();
                        dottedStripend.setStartX(Math.max(Math.min(((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x, w), 0));
                        dottedStripend.setStartY(Math.max(Math.min((h * 0.7 * scale) + offset_y, h), 0));
                        dottedStripend.setEndX(Math.max(Math.min(((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x, w), 0));
                        dottedStripend.setEndY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
                        dottedStripend.setStroke(Color.WHITE);
                        dottedStripend.setStrokeWidth(2d * scale);
                        dottedStripend.getStrokeDashArray().addAll(7d * scale, 7d * scale);
                        this.getChildren().add(dottedStripend);

                    } else {
                        ldaLine = new Line();
                        ldaLine.setStartX(Math.max(Math.min(((runwayStart + dtSize) * scale) + offset_x, w), 0));
                        ldaLine.setStartY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
                        ldaLine.setEndX(Math.max(Math.min(((runwayStart + dtSize + runwayRepresentationSize) * scale) + offset_x, w), 0));
                        ldaLine.setEndY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
                        ldaText.setX(Math.max(Math.min(((runwayStart+ dtSize) * scale) + offset_x, w), 0));
                    }

                }
            } catch (PositionException ignored){
                logger.info("Obstacle has no position");
            }
        } else {
            toraLine = new Line();
            toraLine.setStartX(Math.max(Math.min(((runwayStart) * scale) + offset_x, w), 0));
            toraLine.setStartY(Math.max(Math.min((30 * scale) + offset_y, h), 0));
            toraLine.setEndX(Math.max(Math.min(((runwayStart + runwayRepresentationSize) * scale) + offset_x, w), 0));
            toraLine.setEndY(Math.max(Math.min((30 * scale) + offset_y, h), 0));
            toraText.setX(Math.max(Math.min(((runwayStart) * scale) + offset_x, w), 0));

            todaLine = new Line();
            todaLine.setStartX(Math.max(Math.min(((runwayStart) * scale) + offset_x, w), 0));
            todaLine.setStartY(Math.max(Math.min((50 * scale) + offset_y, h), 0));
            todaLine.setEndX(Math.max(Math.min(((runwayStart + runwayRepresentationSize + clearwaySize) * scale) + offset_x, w), 0));
            todaLine.setEndY(Math.max(Math.min((50 * scale) + offset_y, h), 0));
            todaText.setX(Math.max(Math.min(((runwayStart) * scale) + offset_x, w), 0));

            asdaLine = new Line();
            asdaLine.setStartX(Math.max(Math.min(((runwayStart) * scale) + offset_x, w), 0));
            asdaLine.setStartY(Math.max(Math.min((70 * scale) + offset_y, h), 0));
            asdaLine.setEndX(Math.max(Math.min(((runwayStart + runwayRepresentationSize + stopwaySize) * scale) + offset_x, w), 0));
            asdaLine.setEndY(Math.max(Math.min((70 * scale) + offset_y, h), 0));
            asdaText.setX(Math.max(Math.min(((runwayStart) * scale) + offset_x, w), 0));

            ldaLine = new Line();
            ldaLine.setStartX(Math.max(Math.min(((runwayStart + dtSize) * scale) + offset_x, w), 0));
            ldaLine.setStartY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
            ldaLine.setEndX(Math.max(Math.min(((runwayStart + runwayRepresentationSize) * scale) + offset_x, w), 0));
            ldaLine.setEndY(Math.max(Math.min((90 * scale) + offset_y, h), 0));
            ldaText.setX(Math.max(Math.min(((runwayStart + dtSize) * scale) + offset_x, w), 0));
        }

        // set colors of lines
        if (toraLine != null) {
            toraLine.setStroke(Color.BLUE);
            toraLine.setStrokeWidth(5d * scale);
        }
        if (todaLine != null) {
            todaLine.setStroke(Color.YELLOW);
            todaLine.setStrokeWidth(5d * scale);
        }
        if (asdaLine != null) {
            asdaLine.setStroke(Color.PURPLE);
            asdaLine.setStrokeWidth(5d * scale);
        }
        if (ldaLine != null) {
            ldaLine.setStroke(Color.PINK);
            ldaLine.setStrokeWidth(5d * scale);
        }

        // add color meanings
        Text stripEndText = new Text("Strip End");
        stripEndText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        stripEndText.setY(this.h-25);
        stripEndText.setX(10);
        stripEndText.setFill(Color.DARKGREEN);

        Text blastText = new Text("Blast Allowance");
        blastText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        blastText.setY(this.h-10);
        blastText.setX(10);
        blastText.setFill(Color.BROWN);

        Text resaText = new Text("RESA");
        resaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        resaText.setY(this.h-55);
        resaText.setX(10);
        resaText.setFill(Color.ORANGE);

        Text slopeText = new Text("Slope");
        slopeText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        slopeText.setY(this.h-40);
        slopeText.setX(10);
        slopeText.setFill(Color.AZURE);


        // Draw take-off direction and landing direction


        // add everything
        this.getChildren().addAll(toraLine,toraText,todaLine,todaText,asdaLine,asdaText,ldaLine,ldaText,stripEndText,blastText,resaText,slopeText);
    }

    /**
     * Increase the zoom scale and update the view.
     * @param top boolean property indicating the selected view.
     */
    public void zoomIn(BooleanProperty top) {
        this.scale += 0.1;
        if (top.get()) {
            updateTopDown();
        } else {
            updateSideOn();
        }
    }

    /**
     * Decrease the zoom scale and update the view.
     * @param top boolean property indicating the selected view.
     */
    public void zoomOut(BooleanProperty top) {
        this.scale -= 0.1;
        if (this.scale < 1.0) {
            this.scale = 1.0;
        }
        if (top.get()) {
            updateTopDown();
        } else {
            updateSideOn();
        }
    }
}
