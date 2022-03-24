package uk.ac.soton.comp2211.component;

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
    
    public RunwayView(double width, double height, Runway runway) {
        super();
        
        this.runway = runway;
        this.w = width;
        this.h = height;
        Canvas canvas = new Canvas(width, height);
        
        this.getChildren().add(canvas);

        //Handle size of runway and values lines
        runwayStart = this.w * 0.15;
        runwayRepresentationSize = this.w * 0.7;
        clearwaySize = Math.min(runway.getOriginalValues().getClearway()/3.0, this.w*0.10);
        stopwaySize = Math.min(runway.getOriginalValues().getStopway()/3.0, this.w*0.10);
        dtSize = runway.getOriginalValues().getDT()*runwayRepresentationSize/runway.getLength();
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
        if (runway.getOriginalValues().getDT()>0) {
            Line dtLine = new Line(runwayStart+(dtSize), this.h*0.5-10, runwayStart+(dtSize), this.h*0.5+10);
            dtLine.setStroke(Color.RED);
            dtLine.setStrokeWidth(5d);
            this.getChildren().add(dtLine);
        }

        // Draw clearway
        if (runway.getOriginalValues().getClearway()>0) {
            Rectangle clearwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.4, clearwaySize, this.h*0.2);
            clearwayRec.setFill(Color.CORAL);
            this.getChildren().add(clearwayRec);
        }

        // Draw stop-way
        if (runway.getOriginalValues().getStopway()>0) {
            Rectangle stopwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.45, stopwaySize, this.h*0.1);
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
                obsRect.setX(runwayStart + dtSize + ((pos.getDistance() - (obs.getWidth() / 2.0)) * this.runwayRepresentationSize / this.runway.getLength()));
                obsRect.setY((this.h * 0.5) + ((pos.getCentreLineDisplacement() - (obs.getHeight() / 2.0)) * this.runwayRepresentationSize / this.runway.getLength()));
                obsRect.setHeight(obs.getWidth() * this.runwayRepresentationSize / this.runway.getLength());
                obsRect.setWidth(obs.getLength() * this.runwayRepresentationSize / this.runway.getLength());
                obsRect.setFill(Color.RED);
                obsRect.setStroke(Color.BLACK);
                this.getChildren().add(obsRect);
                logger.info("Obstacle drawn to top-down.");

                //Draw obstacle dotted line
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                Line dottedObsline = new Line(runwayStart+ dWestRepresentation, this.h*0.5, runwayStart+ dWestRepresentation, this.h*0.75);
                dottedObsline.setStroke(Color.WHITE);
                dottedObsline.setStrokeWidth(2d);
                dottedObsline.getStrokeDashArray().addAll(7d, 7d);
                this.getChildren().add(dottedObsline);

            } catch (PositionException ignored) {
                logger.info("Obstacle has not position, so is not being drawn.");
            }
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


        // Draw scale indicator
        // Draw compass


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
        if (runway.getOriginalValues().getDT()>0) {
            Line dtLine = new Line(runwayStart+(dtSize), this.h*0.61, runwayStart+(dtSize), this.h*0.64);
            dtLine.setStroke(Color.RED);
            dtLine.setStrokeWidth(5d);
            this.getChildren().add(dtLine);
        }

        // Draw clearway
        if (runway.getOriginalValues().getClearway()>0) {
            Rectangle clearwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.6, clearwaySize, this.h*0.20);
            clearwayRec.setFill(Color.CORAL);
            this.getChildren().add(clearwayRec);
        }

        // Draw stop-way
        if (runway.getOriginalValues().getStopway()>0) {
            Rectangle stopwayRec = new Rectangle(runwayStart+runwayRepresentationSize, this.h*0.6, stopwaySize, this.h*0.12);
            stopwayRec.setFill(Color.CYAN);
            this.getChildren().add(stopwayRec);
            stopwayRec.toFront();
        }
        
        // Draw Obstacle
        Obstacle obs = runway.getTarmac().getObstacle();
        if (obs != null) {
            try {
                Position pos = obs.getPosition();
                Rectangle obsRect = new Rectangle();
                obsRect.setX(runwayStart + (pos.getDistance() * this.runwayRepresentationSize / this.runway.getLength()));
                obsRect.setY((this.h * 0.6) - (obs.getHeight() * this.runwayRepresentationSize / this.runway.getLength()));
                obsRect.setHeight(obs.getHeight() * this.runwayRepresentationSize / this.runway.getLength());
                obsRect.setWidth(obs.getLength() * this.runwayRepresentationSize / this.runway.getLength());
                obsRect.setFill(Color.RED);
                obsRect.setStroke(Color.BLACK);
                this.getChildren().add(obsRect);
                logger.info("Obstacle drawn to side-on.");

                // draw obstacle dotted line
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                Line dottedObsline = new Line(runwayStart+ dWestRepresentation, this.h*0.6, runwayStart+ dWestRepresentation, this.h*0.75);
                dottedObsline.setStroke(Color.WHITE);
                dottedObsline.setStrokeWidth(2d);
                dottedObsline.getStrokeDashArray().addAll(7d, 7d);
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
        toraText.setY(25);
        toraText.setFill(Color.BLACK);
        Line toraLine = null;

        //toda
        Text todaText = new Text("TODA: " + runway.getCurrentValues().getTODA() + "m");
        todaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        todaText.setY(45);
        todaText.setFill(Color.BLACK);
        Line todaLine = null;

        //asda
        Text asdaText = new Text("ASDA: " + runway.getCurrentValues().getASDA() + "m");
        asdaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        asdaText.setY(65);
        asdaText.setFill(Color.BLACK);
        Line asdaLine = null;

        //lda
        Text ldaText = new Text("LDA: " + runway.getCurrentValues().getLDA() + "m");
        ldaText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        ldaText.setY(85);
        ldaText.setFill(Color.BLACK);
        Line ldaLine = null;

        // handle sizing
        if (runway.getTarmac().getObstacle()!=null && runway.getTarmac().getObstacle().hasValidPosition()) {
            try {
                double slopeOrResa = Math.max(240, this.runway.getTarmac().getObstacle().getHeight()*50);
                double dWestRepresentation = this.runway.getTarmac().getObstacle().getPosition().getDistance()*runwayRepresentationSize/runway.getLength();
                double blastAllowanceR = 300*runwayRepresentationSize/this.runway.getLength();

                if (runway.getTarmac().getObstacle().getPosition().getDistance()>(runway.getLength()-runway.getTarmac().getObstacle().getPosition().getDistance())) {
                    toraLine = new Line(runwayStart, 30, runwayStart + (dWestRepresentation)-((slopeOrResa+60)*runwayRepresentationSize/runway.getLength()), 30);
                    toraText.setX(runwayStart);

                    todaLine = new Line(runwayStart, 50, runwayStart+ (dWestRepresentation)-((slopeOrResa+60)*runwayRepresentationSize/runway.getLength()), 50);
                    todaText.setX(runwayStart);

                    asdaLine = new Line(runwayStart, 70, runwayStart+ (dWestRepresentation)-((slopeOrResa+60)*runwayRepresentationSize/runway.getLength()), 70);
                    asdaText.setX(runwayStart);

                    ldaLine = new Line(runwayStart+(dtSize), 90, runwayStart+ (dWestRepresentation)-(300*runwayRepresentationSize/runway.getLength()), 90);
                    ldaText.setX(runwayStart+(dtSize));

                    Line slopeLine = new Line(runwayStart+ (dWestRepresentation)-((slopeOrResa)*runwayRepresentationSize/runway.getLength()), this.h*0.75, runwayStart+ dWestRepresentation, this.h*0.75);
                    slopeLine.setStrokeWidth(5d);
                    slopeLine.setStroke(Color.AZURE);
                    slopeLine.toFront();
                    this.getChildren().add(slopeLine);

                    Line stripendLine2 = new Line(runwayStart+ (dWestRepresentation)-((slopeOrResa+60)*runwayRepresentationSize/runway.getLength()), this.h*0.75, runwayStart+ (dWestRepresentation)-((slopeOrResa)*runwayRepresentationSize/runway.getLength()), this.h*0.75);
                    stripendLine2.setStrokeWidth(5d);
                    stripendLine2.setStroke(Color.DARKGREEN);
                    stripendLine2.toFront();
                    this.getChildren().add(stripendLine2);

                    Line resaLine = new Line(runwayStart+ (dWestRepresentation)-(240*runwayRepresentationSize/runway.getLength()), this.h*0.7, runwayStart+ dWestRepresentation, this.h*0.7);
                    resaLine.setStrokeWidth(5d);
                    resaLine.setStroke(Color.ORANGE);
                    resaLine.toFront();
                    this.getChildren().add(resaLine);

                    Line stripendLine = new Line(runwayStart+ (dWestRepresentation)-(300*runwayRepresentationSize/runway.getLength()), this.h*0.75, runwayStart+ dWestRepresentation-(240*runwayRepresentationSize/runway.getLength()), this.h*0.75);
                    stripendLine.setStrokeWidth(5d);
                    stripendLine.setStroke(Color.DARKGREEN);
                    stripendLine.toFront();
                    this.getChildren().add(stripendLine);

                    Line dottedTake = new Line(runwayStart+ (dWestRepresentation)-((slopeOrResa+60)*runwayRepresentationSize/runway.getLength()), this.h*0.8, runwayStart+ (dWestRepresentation)-((slopeOrResa+60)*runwayRepresentationSize/runway.getLength()), 30);
                    dottedTake.setStroke(Color.WHITE);
                    dottedTake.setStrokeWidth(2d);
                    dottedTake.getStrokeDashArray().addAll(7d, 7d);
                    this.getChildren().add(dottedTake);

                    Line dottedLand = new Line(runwayStart+ (dWestRepresentation)-(300*runwayRepresentationSize/runway.getLength()), this.h*0.70, runwayStart+ (dWestRepresentation)-(300*runwayRepresentationSize/runway.getLength()), 90);
                    dottedLand.setStroke(Color.WHITE);
                    dottedLand.setStrokeWidth(2d);
                    dottedLand.getStrokeDashArray().addAll(7d, 7d);
                    this.getChildren().add(dottedLand);

                } else {
                    toraLine = new Line(runwayStart + blastAllowanceR + dWestRepresentation, 30, runwayStart + runwayRepresentationSize, 30);
                    toraText.setX(runwayStart + blastAllowanceR + dWestRepresentation);

                    todaLine = new Line(runwayStart + blastAllowanceR + dWestRepresentation, 50, runwayStart+runwayRepresentationSize+(clearwaySize), 50);
                    todaText.setX(runwayStart+ blastAllowanceR+ dWestRepresentation);

                    asdaLine = new Line(runwayStart + blastAllowanceR+ dWestRepresentation, 70, runwayStart+runwayRepresentationSize+(stopwaySize), 70);
                    asdaText.setX(runwayStart + blastAllowanceR+ dWestRepresentation);

                    Line blastLine = new Line(runwayStart+ (dWestRepresentation), this.h*0.75, runwayStart+ dWestRepresentation + blastAllowanceR, this.h*0.75);
                    blastLine.setStrokeWidth(5d);
                    blastLine.setStroke(Color.BROWN);
                    blastLine.toFront();
                    this.getChildren().add(blastLine);

                    Line dottedBlastline = new Line(runwayStart+ dWestRepresentation + blastAllowanceR, this.h*0.75, runwayStart+ dWestRepresentation + blastAllowanceR, 30);
                    dottedBlastline.setStroke(Color.WHITE);
                    dottedBlastline.setStrokeWidth(2d);
                    dottedBlastline.getStrokeDashArray().addAll(7d, 7d);
                    this.getChildren().add(dottedBlastline);

                    if (runway.getOriginalValues().getDT()<dWestRepresentation+(slopeOrResa+60)) {
                        ldaLine = new Line(runwayStart+((slopeOrResa+60)*runwayRepresentationSize/runway.getLength()) + dWestRepresentation, 90, runwayStart+runwayRepresentationSize, 90);
                        ldaText.setX(runwayStart+((slopeOrResa+60)*runwayRepresentationSize/runway.getLength())+ dWestRepresentation);

                        Line resaLine = new Line(runwayStart+ dWestRepresentation, this.h*0.7,runwayStart+(slopeOrResa*runwayRepresentationSize/runway.getLength())+ dWestRepresentation, this.h*0.7);
                        resaLine.setStrokeWidth(5d);
                        if (slopeOrResa==240) {
                            resaLine.setStroke(Color.ORANGE);
                        } else {
                            resaLine.setStroke(Color.AZURE);
                        }
                        resaLine.toFront();
                        this.getChildren().add(resaLine);

                        Line stripendLine = new Line(runwayStart+(slopeOrResa*runwayRepresentationSize/runway.getLength())+ dWestRepresentation, this.h*0.7, runwayStart+((slopeOrResa+60)*runwayRepresentationSize/runway.getLength())+ dWestRepresentation, this.h*0.7);
                        stripendLine.setStrokeWidth(5d);
                        stripendLine.setStroke(Color.DARKGREEN);
                        stripendLine.toFront();
                        this.getChildren().add(stripendLine);

                        Line dottedStripend = new Line(runwayStart+((slopeOrResa+60)*runwayRepresentationSize/runway.getLength())+ dWestRepresentation, this.h*0.70, runwayStart+((slopeOrResa+60)*runwayRepresentationSize/runway.getLength())+ dWestRepresentation, 90);
                        dottedStripend.setStroke(Color.WHITE);
                        dottedStripend.setStrokeWidth(2d);
                        dottedStripend.getStrokeDashArray().addAll(7d, 7d);
                        this.getChildren().add(dottedStripend);

                    } else {
                        ldaLine = new Line(runwayStart+ dtSize, 90, runwayStart+runwayRepresentationSize, 90);
                        ldaText.setX(runwayStart+ dtSize);
                    }

                }
            } catch (PositionException ignored){
                logger.info("Obstacle has no position");
            }
        } else {
            toraLine = new Line(runwayStart, 30, runwayStart + runwayRepresentationSize, 30);
            toraText.setX(runwayStart);

            todaLine = new Line(runwayStart, 50, runwayStart+runwayRepresentationSize+(clearwaySize), 50);
            todaText.setX(runwayStart);

            asdaLine = new Line(runwayStart, 70, runwayStart+runwayRepresentationSize+(stopwaySize), 70);
            asdaText.setX(runwayStart);

            ldaLine = new Line(runwayStart+(dtSize), 90, runwayStart+runwayRepresentationSize, 90);
            ldaText.setX(runwayStart+(dtSize));
        }

        // set colors of lines
        if (toraLine != null) {
            toraLine.setStroke(Color.BLUE);
            toraLine.setStrokeWidth(5d);
        }
        if (todaLine != null) {
            todaLine.setStroke(Color.YELLOW);
            todaLine.setStrokeWidth(5d);
        }
        if (asdaLine != null) {
            asdaLine.setStroke(Color.PURPLE);
            asdaLine.setStrokeWidth(5d);
        }
        if (ldaLine != null) {
            ldaLine.setStroke(Color.PINK);
            ldaLine.setStrokeWidth(5d);
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
}
