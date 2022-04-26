package uk.ac.soton.comp2211.component;

import javafx.beans.property.BooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.exceptions.PositionException;
import uk.ac.soton.comp2211.model.Obstacle;
import uk.ac.soton.comp2211.model.Position;
import uk.ac.soton.comp2211.model.Runway;



/**
 * Custom component to draw runway visualisation from parameters.
 */
public class RunwayView extends Canvas {

    protected static final Logger logger = LogManager.getLogger(RunwayView.class);
    
    private final Runway runway;
    private final double w;
    private final double h;
    private final GraphicsContext gc;
    private int angle;

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

        this.gc = this.getGraphicsContext2D();
        this.setHeight(height);
        this.setWidth(width);

        this.runway = runway;
        this.w = width;
        this.h = height;
        this.angle = 0;

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
        // variables for reuse
        double x1;
        double y1;
        double x2;
        double y2;

        // Reset view
        this.gc.clearRect(-w, -h, w * 3, h * 3);
        this.gc.setFill(Color.LIGHTGREEN);
        this.gc.fillRect(-w, -h, w * 3, h * 3);

        // Draw cleared and Graded Area
        double[] pointsX = {
                Math.max(Math.min(offset_x, w), 0),
                Math.max(Math.min((0.2 * this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((0.3 * this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((0.7 * this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((0.8 * this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((0.8 * this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((0.7 * this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((0.3 * this.w * scale) + offset_x, w), 0),
                Math.max(Math.min((0.2 * this.w * scale) + offset_x, w), 0),
                Math.max(Math.min(offset_x, w), 0)};
        double[] pointsY = {
                Math.max(Math.min((0.3 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.3 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.2 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.2 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.3 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.3 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.7 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.7 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.8 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.8 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.7 * this.h * scale) + offset_y, h), 0),
                Math.max(Math.min((0.7 * this.h * scale) + offset_y, h), 0)};
        this.gc.setFill(Color.LIGHTBLUE);
        this.gc.fillPolygon(pointsX , pointsY , 12);


        // Draw tarmac

        x1 = (runwayStart * scale) + offset_x;
        y1 = (h * 0.45 * scale) + offset_y;

        this.gc.setFill(Color.GRAY);

        if (x1 < 0) {
            if (y1 < 0) {
                this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0), Math.max(Math.min(((runwayRepresentationSize * scale) + x1), w - x1), 0), Math.max(Math.min(((h * 0.1 * scale) + y1), h - y1), 0));
            } else {
                this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0), Math.max(Math.min(((runwayRepresentationSize * scale) + x1), w - x1), 0), Math.max(Math.min(h * 0.1 * scale, h - y1), 0));
            }
        } else {
            if (y1 < 0) {
                this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0), Math.max(Math.min(runwayRepresentationSize * scale, w - x1), 0), Math.max(Math.min(((h * 0.1 * scale) + y1), h - y1), 0));
            } else {
                this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0), Math.max(Math.min(runwayRepresentationSize * scale, w - x1), 0), Math.max(Math.min(h * 0.1 * scale, h - y1), 0));
            }
        }


        double runwayDesignatorX = ((runwayStart + 10) * scale) + offset_x;
        double runwayDesignatorY = (this.h * 0.52 * scale) + offset_y;

        //runway designator
        this.gc.setFill(Color.WHITE);
        this.gc.setFont(Font.font("Jersey", FontWeight.BOLD, 15 * scale));

        this.gc.fillText(this.runway.getRunwayDesignator(), runwayDesignatorX, runwayDesignatorY);


        //Draw line middle tarmac

        this.gc.setStroke(Color.WHITE);
        this.gc.setLineWidth(5d * scale);
        this.gc.setLineDashes(20d * scale, 20d * scale);
        this.gc.strokeLine(((runwayStart + 0.1 * runwayRepresentationSize) * scale) + offset_x, ((h * 0.5 - 1) * scale) + offset_y,
                ((w * 0.85 - 0.1 * runwayRepresentationSize) * scale) + offset_x, ((h * 0.5 - 1) * scale) + offset_y);


        //Draw value lines
        drawValueLines();

        // Draw displaced threshold
        if (runway.getOriginalValues().getDT()>0) {
            x1 = ((runwayStart + dtSize) * scale) + offset_x;
            y1 = ((h * 0.5 - 10) * scale) + offset_y;
            x2 = ((runwayStart + dtSize) * scale) + offset_x;
            y2 = ((h * 0.5 + 10) * scale) + offset_y;

            this.gc.setStroke(Color.RED);
            this.gc.setLineWidth(5d * scale);
            this.gc.strokeLine(x1, y1, x2, y2);
        }

        // Draw clearway
        if (runway.getOriginalValues().getClearway() > 0) {
            x1 = ((runwayStart + runwayRepresentationSize) * scale) + offset_x;
            y1 = (h * 0.4 * scale) + offset_y;

            this.gc.setFill(Color.CORAL);

            if (x1 < 0) {
                if (y1 < 0) {
                    this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0), Math.max(Math.min((clearwaySize * scale) + x1, w - x1), 0), Math.max(Math.min(((h * 0.2 * scale) + y1), h - y1), 0));
                } else {
                    this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0), Math.max(Math.min((clearwaySize * scale) + x1, w - x1), 0), Math.max(Math.min(h * 0.2 * scale, h - y1), 0));
                }
            } else {
                if (y1 < 0) {
                    this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0), Math.max(Math.min(clearwaySize * scale, w - x1), 0), Math.max(Math.min(((h * 0.2 * scale) + y1), h - y1), 0));
                } else {
                    this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0), Math.max(Math.min(clearwaySize * scale, w - x1), 0), Math.max(Math.min(h * 0.2 * scale, h - y1), 0));
                }
            }

        }

        // Draw stop-way
        if (runway.getOriginalValues().getStopway()>0) {
            x1 = ((runwayStart + runwayRepresentationSize) * scale) + offset_x;
            y1 = (h * 0.45 * scale) + offset_y;

            this.gc.setFill(Color.CYAN);

            if (x1 < 0) {
                if (y1 < 0) {
                    this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0),Math.max(Math.min(((stopwaySize * scale) + x1), w - x1), 0) , Math.max(Math.min(((h * 0.1 * scale) + y1), h - y1), 0));
                } else {
                    this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0),Math.max(Math.min(((stopwaySize * scale) + x1), w - x1), 0) , Math.max(Math.min(h * 0.1 * scale, h - y1), 0));
                }
            } else {
                if (y1 < 0) {
                    this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0),Math.max(Math.min(stopwaySize * scale, w - x1), 0) , Math.max(Math.min(((h * 0.1 * scale) + y1), h - y1), 0));
                } else {
                    this.gc.fillRect(Math.max(Math.min(x1, w), 0), Math.max(Math.min(y1, h), 0),Math.max(Math.min(stopwaySize * scale, w - x1), 0) , Math.max(Math.min(h * 0.1 * scale, h - y1), 0));
                }
            }

        }
        
        // Draw obstacle
        Obstacle obs = runway.getTarmac().getObstacle();
        if (obs != null) {
            try {
                Position pos = obs.getPosition();

                double obsRectX = ((runwayStart + dtSize + ((pos.getDistance() - (obs.getWidth() / 2.0)) * runwayRepresentationSize / runway.getLength())) * scale) + offset_x;
                double obsRectY = (((h * 0.5) + ((pos.getCentreLineDisplacement() - (obs.getLength() / 2.0)) * runwayRepresentationSize / runway.getLength())) * scale) + offset_y;

                this.gc.setFill(Color.RED);
                this.gc.setStroke(Color.BLACK);
                this.gc.fillRect(obsRectX, obsRectY,
                                 Math.max(Math.min(obs.getLength() * runwayRepresentationSize / runway.getLength() * scale, h - obsRectY), 0),
                                 Math.max(Math.min(obs.getWidth() * runwayRepresentationSize / runway.getLength() * scale, w - obsRectX), 0));

                logger.info("Obstacle drawn to top-down.");

                //Draw obstacle dotted line
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                this.gc.setStroke(Color.WHITE);
                this.gc.setLineWidth(2d * scale);
                this.gc.setLineDashes(7d * scale, 7d * scale);
                this.gc.strokeLine(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0), Math.max(Math.min((h * 0.5 * scale) + offset_y, h), 0), Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0), Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));


            } catch (PositionException ignored) {
                logger.info("Obstacle has no position, so is not being drawn.");
            }
        }
        
        // Draw Take-off/Landing direction
        this.gc.setFill(Color.RED);
        double[] psX = {
                20,
                60,
                60,
                80,
                60,
                60,
                20,
        };
        double[] psY = {
                0.11 * this.h,
                0.11 * this.h,
                0.075 * this.h,
                0.125 * this.h,
                0.175 * this.h,
                0.14 * this.h,
                0.14 * this.h
        };
        this.gc.fillPolygon(psX, psY, 7);


        // Draw scale indicator
        // Draw compass
        
        if (this.angle == 0) {
            drawBorder();
        }
    }

    public void updateSideOn() {
        // Reset view
        this.gc.clearRect(0,0,this.w, this.h);
        // Draw ground
        this.gc.setFill(Color.LIGHTGREEN);
        this.gc.fillRect(-w, -h, w * 3, h * 3);

        //Draw sky
        this.gc.setFill(Color.LIGHTBLUE);
        this.gc.fillRect(-w, -h, w * 3, Math.max(Math.min((this.h * 0.60 * scale) + offset_y, h), 0));

        //Draw value lines
        drawValueLines();

        // Draw tarmac
        this.gc.setFill(Color.GRAY);
        double runwayRectangleX = (runwayStart * scale) + offset_x;
        double runwayRectangleY = (h * 0.60 * scale) + offset_y;
        this.gc.fillRect(runwayRectangleX, runwayRectangleY, Math.max(Math.min(runwayRepresentationSize * scale, w - runwayRectangleX), 0), Math.max(Math.min(this.h * 0.05 * scale, h - runwayRectangleY), 0));

        // Draw displaced threshold
        if (runway.getOriginalValues().getDT() > 0) {
            this.gc.setStroke(Color.RED);
            this.gc.setLineWidth(5d * scale);
            this.gc.strokeLine(((runwayStart + dtSize) * scale) + offset_x, (h * 0.61 * scale) + offset_y, ((runwayStart + dtSize) * scale) + offset_x, (h * 0.64 * scale) + offset_y);
        }

        // Draw clearway
        if (runway.getOriginalValues().getClearway()>0) {
            this.gc.setFill(Color.CORAL);
            double clearwayRecX = ((runwayStart + runwayRepresentationSize) * scale) + offset_x;
            double clearwayRecY = (h * 0.6 * scale) + offset_y;
            this.gc.fillRect(clearwayRecX, clearwayRecY, Math.max(Math.min(clearwaySize, w - clearwayRecX), 0), Math.max(Math.min(h * 0.2 * scale, h - clearwayRecY), 0));
        }

        // Draw stop-way
        if (runway.getOriginalValues().getStopway()>0) {
            this.gc.setFill(Color.CYAN);
            double stopwayRecX = ((runwayStart + runwayRepresentationSize) * scale) + offset_x;
            double stopwayRecY = (h * 0.6 * scale) + offset_y;
            this.gc.fillRect(stopwayRecX, stopwayRecY, Math.max(Math.min(stopwaySize, w - stopwayRecX), 0), Math.max(Math.min(h * 0.12 * scale, h - stopwayRecY), 0));
        }

        // Draw Take-off/Landing direction
        this.gc.setFill(Color.RED);
        double[] psX = {
                20,
                60,
                60,
                80,
                60,
                60,
                20,
        };
        double[] psY= {
                0.11* this.h,
                0.11* this.h,
                0.075* this.h,
                0.125* this.h,
                0.175* this.h,
                0.14* this.h,
                0.14* this.h
        };
        this.gc.fillPolygon(psX, psY, 7);
        
        // Draw Obstacle
        Obstacle obs = runway.getTarmac().getObstacle();
        if (obs != null) {
            try {
                Position pos = obs.getPosition();
                double obsRectX = ((runwayStart + dtSize + ((pos.getDistance() - (obs.getWidth() / 2.0)) * this.runwayRepresentationSize / this.runway.getLength())) * scale) + offset_x;
                double obsRectY = (((h * 0.6) - (obs.getHeight() * this.runwayRepresentationSize / this.runway.getLength())) * scale) + offset_y;
                this.gc.setFill(Color.BLACK);
                this.gc.fillRect(obsRectX, obsRectY, obs.getLength() * this.runwayRepresentationSize / this.runway.getLength() * scale, obs.getHeight() * this.runwayRepresentationSize / this.runway.getLength() * scale);
                logger.info("Obstacle drawn to side-on.");

                // draw obstacle dotted line
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                this.gc.setStroke(Color.WHITE);
                this.gc.setLineWidth(2d * scale);
                this.gc.setLineDashes(7d * scale, 7d * scale);
                this.gc.strokeLine(Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0),
                                   Math.max(Math.min((h * 0.6 * scale) + offset_y, h), 0),
                                   Math.max(Math.min(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x, w), 0),
                                   Math.max(Math.min((h * 0.75 * scale) + offset_y, h), 0));

            } catch (PositionException ignored) {
                logger.info("Obstacle has not position, so is not being drawn.");
            }
        }

        if (this.angle == 0) {
            drawBorder();
        }

    }

    private void drawValueLines() {
        //Draw value lines
        this.gc.setLineDashes(0);
        // handle sizing
        if (runway.getTarmac().getObstacle()!=null && runway.getTarmac().getObstacle().hasValidPosition()) {
            try {
                double slopeOrResa = Math.max(240, this.runway.getTarmac().getObstacle().getHeight()*50);
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                double blastAllowanceR = 300*runwayRepresentationSize/this.runway.getLength();

                if (runway.getTarmac().getObstacle().getPosition().getDistance()>(runway.getLength()-runway.getTarmac().getObstacle().getPosition().getDistance())) {
                    this.gc.setStroke(Color.BLUE);
                    this.gc.setLineWidth(5d * scale);
                    this.gc.strokeLine((runwayStart * scale) + offset_x, (30 * scale) + offset_y,
                            ((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize/runway.getLength())) * scale) + offset_x,
                            (30 * scale) + offset_y);

                    this.gc.setFill(Color.BLACK);
                    this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                    this.gc.fillText("TORA: " + runway.getCurrentValues().getTORA() + "m", (runwayStart*scale) + offset_x, (25 * scale) + offset_y);

                    this.gc.setStroke(Color.YELLOW);
                    this.gc.setLineWidth(5d * scale);
                    this.gc.strokeLine((runwayStart * scale) + offset_x, (50 * scale) + offset_y,
                            ((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize/runway.getLength())) * scale) + offset_x,
                            (50 * scale) + offset_y);
                    this.gc.setFill(Color.BLACK);
                    this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                    this.gc.fillText("TODA: " + runway.getCurrentValues().getTODA() + "m", (runwayStart*scale) + offset_x, (45 * scale) + offset_y);

                    this.gc.setStroke(Color.PURPLE);
                    this.gc.setLineWidth(5d * scale);
                    this.gc.strokeLine((runwayStart * scale) + offset_x, (70 * scale) + offset_y,
                            ((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize/runway.getLength())) * scale) + offset_x,
                            (70 * scale) + offset_y);
                    this.gc.setFill(Color.BLACK);
                    this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                    this.gc.fillText("ASDA: " + runway.getCurrentValues().getASDA() + "m", (runwayStart*scale) + offset_x,(65 * scale) + offset_y);

                    this.gc.setStroke(Color.PINK);
                    this.gc.setLineWidth(5d * scale);
                    this.gc.strokeLine(((runwayStart + dtSize) * scale) + offset_x, (90 * scale) + offset_y,
                            ((runwayStart + dtSize + dWestRepresentation - (300 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (90 * scale) + offset_y);
                    this.gc.setFill(Color.BLACK);
                    this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                    this.gc.fillText("LDA: " + runway.getCurrentValues().getLDA() + "m", ((runwayStart+dtSize)*scale) + offset_x,(85 * scale) + offset_y);

                    this.gc.setLineWidth(5d*scale);
                    this.gc.setStroke(Color.HOTPINK);
                    this.gc.strokeLine(((runwayStart + dtSize+ dWestRepresentation - (slopeOrResa * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (h * 0.75 * scale) + offset_y, ((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x,
                            (h * 0.75 * scale) + offset_y);

                    this.gc.setStroke(Color.DARKGREEN);
                    this.gc.strokeLine(((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (h * 0.75 * scale) + offset_y,
                            ((runwayStart + dtSize + dWestRepresentation - (slopeOrResa * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (h * 0.75 * scale) + offset_y);

                    this.gc.setStroke(Color.BLACK);
                    this.gc.strokeLine(((runwayStart + dtSize + dWestRepresentation - (240 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (h * 0.7 * scale) + offset_y, ((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x,
                            (h * 0.7 * scale) + offset_y);

                    this.gc.setStroke(Color.DARKGREEN);
                    this.gc.strokeLine(((runwayStart + dtSize + dWestRepresentation - (300 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (h * 0.7 * scale) + offset_y, ((runwayStart + dtSize + dWestRepresentation - (240 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (h * 0.7 * scale) + offset_y);

                    this.gc.setStroke(Color.WHITE);
                    this.gc.setLineDashes(7d * scale, 7d * scale);
                    this.gc.setLineWidth(2d * scale);
                    this.gc.strokeLine(((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (h * 0.75 * scale) + offset_y, ((runwayStart + dtSize + dWestRepresentation - ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (30 * scale) + offset_y);

                    this.gc.setStroke(Color.WHITE);
                    this.gc.setLineWidth(2d * scale);
                    this.gc.strokeLine(((runwayStart + dtSize + dWestRepresentation - (300 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (h * 0.7 * scale) + offset_y, ((runwayStart + dtSize + dWestRepresentation - (300 * runwayRepresentationSize / runway.getLength())) * scale) + offset_x,
                            (90 * scale) + offset_y);

                    this.gc.setLineDashes(0);

                } else {
                    this.gc.setStroke(Color.BLUE);
                    this.gc.setLineWidth(5d * scale);
                    this.gc.strokeLine(((runwayStart + dtSize + blastAllowanceR + dWestRepresentation) * scale) + offset_x,
                            (30 * scale) + offset_y,((runwayStart + runwayRepresentationSize) * scale) + offset_x,
                            (30 * scale) + offset_y);
                    this.gc.setFill(Color.BLACK);
                    this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                    this.gc.fillText("TORA: " + runway.getCurrentValues().getTORA() + "m", ((runwayStart + dtSize + blastAllowanceR + dWestRepresentation) * scale) + offset_x, (25 * scale) + offset_y);

                    this.gc.setStroke(Color.YELLOW);
                    this.gc.setLineWidth(5d * scale);
                    this.gc.strokeLine(((runwayStart + dtSize + blastAllowanceR + dWestRepresentation) * scale) + offset_x, (50 * scale) + offset_y,
                            ((runwayStart + runwayRepresentationSize + clearwaySize) * scale) + offset_x, (50 * scale) + offset_y);
                    this.gc.setFill(Color.BLACK);
                    this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                    this.gc.fillText("TODA: " + runway.getCurrentValues().getTODA() + "m", ((runwayStart+dtSize+ blastAllowanceR+ dWestRepresentation) * scale) + offset_x, (45 * scale) + offset_y);

                    this.gc.setStroke(Color.PURPLE);
                    this.gc.setLineWidth(5d * scale);
                    this.gc.strokeLine(((runwayStart + dtSize + blastAllowanceR + dWestRepresentation) * scale) + offset_x,
                            (70 * scale) + offset_y, ((runwayStart + runwayRepresentationSize + stopwaySize) * scale) + offset_x,
                            (70 * scale) + offset_y);

                    this.gc.setFill(Color.BLACK);
                    this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                    this.gc.fillText("ASDA: " + runway.getCurrentValues().getASDA() + "m", ((runwayStart+dtSize + blastAllowanceR+ dWestRepresentation) * scale) + offset_x,(65 * scale) + offset_y);

                    this.gc.setStroke(Color.DARKORANGE);
                    this.gc.setLineWidth(5d* scale);
                    this.gc.strokeLine(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x,
                            (h * 0.75 * scale) + offset_y,
                            ((runwayStart + dtSize + dWestRepresentation + blastAllowanceR) * scale) + offset_x,
                            (h * 0.75 * scale) + offset_y);

                    this.gc.setStroke(Color.WHITE);
                    this.gc.setLineWidth(2d * scale);
                    this.gc.setLineDashes(7d * scale, 7d * scale);
                    this.gc.strokeLine((runwayStart + dtSize + dWestRepresentation + blastAllowanceR) + offset_x,
                            (h * 0.75 * scale) + offset_y,
                            (runwayStart + dtSize + dWestRepresentation + blastAllowanceR) + offset_x,
                            (30 * scale) + offset_y);
                    this.gc.setLineDashes(0);

                    //ldaLine
                    if (runway.getOriginalValues().getDT()<dWestRepresentation+(slopeOrResa+60)) {
                        this.gc.setStroke(Color.PINK);
                        this.gc.setLineWidth(5d * scale);
                        this.gc.strokeLine(((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x,
                                (90 * scale) + offset_y, ((runwayStart + runwayRepresentationSize) * scale) + offset_x,
                                (90 * scale) + offset_y);
                        this.gc.setFill(Color.BLACK);
                        this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                        this.gc.fillText("LDA: " + runway.getCurrentValues().getLDA() + "m", ((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x,(85 * scale) + offset_y);

                        //resaLine
                        if (slopeOrResa==240) {
                            this.gc.setStroke(Color.BLACK);
                        } else {
                            this.gc.setStroke(Color.HOTPINK);
                        }
                        this.gc.setLineWidth(5d*scale);
                        this.gc.strokeLine(((runwayStart + dtSize + dWestRepresentation) * scale) + offset_x,
                                (h * 0.7 * scale) + offset_y,
                                ((runwayStart + dtSize + (slopeOrResa * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x,
                                (h * 0.7 * scale) + offset_y);

                        //stripendLine
                        this.gc.setLineWidth(5d*scale);
                        this.gc.setStroke(Color.DARKGREEN);
                        this.gc.strokeLine(((runwayStart + dtSize + (slopeOrResa * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x,
                                (h * 0.7 * scale) + offset_y,
                                ((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x,
                                (h * 0.7 * scale) + offset_y);

                        this.gc.setStroke(Color.WHITE);
                        this.gc.setLineDashes(7d * scale, 7d * scale);
                        this.gc.setLineWidth(2d * scale);
                        this.gc.strokeLine(((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x,
                                (h * 0.7 * scale) + offset_y, ((runwayStart + dtSize + ((slopeOrResa + 60) * runwayRepresentationSize / runway.getLength()) + dWestRepresentation) * scale) + offset_x,
                                (90 * scale) + offset_y);

                        this.gc.setLineDashes(0);


                    } else {
                        this.gc.setStroke(Color.PINK);
                        this.gc.setLineWidth(5d * scale);
                        this.gc.strokeLine(((runwayStart + dtSize) * scale) + offset_x, (90 * scale) + offset_y,
                                ((runwayStart + dtSize + runwayRepresentationSize) * scale) + offset_x, (90 * scale) + offset_y);
                        this.gc.setFill(Color.BLACK);
                        this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                        this.gc.fillText("LDA: " + runway.getCurrentValues().getLDA() + "m", ((runwayStart+ dtSize) * scale) + offset_x, (85 * scale) + offset_y);
                    }

                }
            } catch (PositionException ignored){
                logger.info("Obstacle has no position");
            }
        } else {
            this.gc.setStroke(Color.BLUE);
            this.gc.setLineWidth(5d * scale);
            this.gc.strokeLine(((runwayStart) * scale) + offset_x, (30 * scale) + offset_y,
                    ((runwayStart + runwayRepresentationSize) * scale) + offset_x, (30 * scale) + offset_y);
            this.gc.setFill(Color.BLACK);
            this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            this.gc.fillText("TORA: " + runway.getCurrentValues().getTORA() + "m", ((runwayStart) * scale) + offset_x, (25 * scale) + offset_y);

            this.gc.setStroke(Color.YELLOW);
            this.gc.setLineWidth(5d * scale);
            this.gc.strokeLine(((runwayStart) * scale) + offset_x, (50 * scale) + offset_y,
                    ((runwayStart + runwayRepresentationSize + clearwaySize) * scale) + offset_x, (50 * scale) + offset_y);
            this.gc.setFill(Color.BLACK);
            this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            this.gc.fillText("TODA: " + runway.getCurrentValues().getTODA() + "m", ((runwayStart) * scale) + offset_x, (45 * scale) + offset_y);


            this.gc.setStroke(Color.PURPLE);
            this.gc.setLineWidth(5d * scale);
            this.gc.strokeLine(((runwayStart) * scale) + offset_x, (70 * scale) + offset_y, ((runwayStart + runwayRepresentationSize + stopwaySize) * scale) + offset_x, (70 * scale) + offset_y);
            this.gc.setFill(Color.BLACK);
            this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            this.gc.fillText("ASDA: " + runway.getCurrentValues().getASDA() + "m", ((runwayStart) * scale) + offset_x,(65 * scale) + offset_y);

            this.gc.setStroke(Color.PINK);
            this.gc.setLineWidth(5d * scale);
            this.gc.strokeLine(((runwayStart + dtSize) * scale) + offset_x, (90 * scale) + offset_y,
                    ((runwayStart + runwayRepresentationSize) * scale) + offset_x,  (90 * scale) + offset_y);
            this.gc.fillText("LDA: " + runway.getCurrentValues().getLDA() + "m", ((runwayStart + dtSize) * scale) + offset_x,(85 * scale) + offset_y);
            this.gc.setFill(Color.BLACK);
            this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        }


        // add color meanings
        this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        this.gc.setFill(Color.DARKGREEN);
        this.gc.fillText("Strip End", 10, this.h-25);

        this.gc.setFill(Color.DARKORANGE);
        this.gc.fillText("Blast Allowance", 10, this.h-10);

        this.gc.setFill(Color.BLACK);
        this.gc.fillText("RESA", 10, this.h-55);

        this.gc.setFill(Color.HOTPINK);
        this.gc.fillText("Slope", 10, this.h-40);

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
        drawRotated(0, top);
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
        drawRotated(0, top);
    }

    /**
     * Change the offset, so the user can pan around the view.
     * @param dx change in x_offset
     * @param dy change in y_offset
     */
    public void move(int dx, int dy, BooleanProperty top) {
        this.offset_x += dx;
        this.offset_y += dy;
        if (top.get()) {
            updateTopDown();
        } else {
            updateSideOn();
        }
        logger.info("OffsetX {}", offset_x);
        logger.info("OffsetY {}", offset_y);
        drawRotated(0, top);
    }

    /**
     * Handle rotation
     */
    public void rotate(int myAngle) {
        this.angle += myAngle;
        Rotate r = new Rotate(this.angle, w/2, h/2);
        this.gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    public void drawRotated(int myAngle, BooleanProperty top) {
        this.gc.save();
        this.gc.clearRect(0,0,this.w, this.h);
        rotate(myAngle);
        if (top.get()) {
            updateTopDown();
        } else {
            updateSideOn();
        }
        gc.restore();
        drawBorder();
    }
    
    public void drawBorder() {
        // Draw border
        this.gc.setLineDashes(0);
        this.gc.setStroke(Color.BLACK);
        this.gc.strokeRect(0, 0, this.w, this.h);
    }

    public void resetAngle(){
        this.angle = 0;
    }

}
