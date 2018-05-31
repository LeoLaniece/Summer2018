package test;

import java.lang.reflect.Field;


import javafx.scene.shape.LineTo; 

import java.lang.reflect.Modifier;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Scene;


/**
 * should show a canvas with a minimap
 * a sample stroke controller 
 * and a color picker 
 * @author HCI Lab
 *
 */


//take out the sample line and colour picker while you implement the pan-able minimap
public class Draw2View extends Pane implements modelListener{
	Pane topPane;
	Draw2Model model;
	Canvas c;
	GraphicsContext gc;
	double pathStartx=0;
	double pathStarty=0;
	public double height;
	public double width;
	double logicalHeight;
	double logicalWidth;
	public Group lineGroup;
	InteractionModel iModel;
	public Draw2miniMap radarView;
	
	//put in a logical size?
	public Draw2View(double w, double h, Draw2Model m) {
		model = m;
		//canvas height and width
		height = h;
		width = w;
		logicalHeight = 1000;
		logicalWidth =1000;
		lineGroup = model.lineGroup;
		
		topPane = new Pane();
		
		setCanvas(logicalHeight,logicalWidth);
		setLineGroup();     
		mainViewOnly();
	}
	
	public void mainViewOnly() {
		VBox root = new VBox();
		HBox underCanvas = new HBox();
		this.getChildren().add(root);				
		root.getChildren().add(c);
		
		root.getChildren().add(underCanvas);
		VBox UCLeft = new VBox();
		UCLeft.setPrefWidth(height);
		UCLeft.setAlignment(Pos.BOTTOM_LEFT);
		VBox UCRight = new VBox();
		UCRight.setPrefWidth(height);
		UCRight.setAlignment(Pos.BOTTOM_RIGHT);
		underCanvas.getChildren().add(UCLeft);
		underCanvas.getChildren().add(UCRight);
		
		setSampleStroke(UCLeft, UCRight);						
		setFlowPane(UCRight);
	}
	public void setLineGroup() {
		StackPane group = new StackPane();
		group.getChildren().add(lineGroup);
		group.setAlignment(Pos.TOP_LEFT);
		group.setPrefHeight(height);
		group.setPrefWidth(width);
		topPane.getChildren().add(group);
		
	}

	
	public void setCanvas(double h, double w) {
		c = new Canvas(width,height);
		gc = c.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width,height);
		gc.setFill(Color.RED);
		gc.fillRect(375, 150, 50, 50);
		
		//topPane.getChildren().add(c);
	}
	
	public void setSampleStroke(VBox UCLeft, VBox UCRight) {
		UCLeft.getChildren().add(model.labelStroke);		
		//sizing for sampleLine
		StackPane stackpane = new StackPane();
        stackpane.setPrefHeight(model.MAXSTROKE);
        stackpane.setPrefWidth(model.sampleLine.getEndX() + model.MAXSTROKE);
        stackpane.setAlignment(Pos.CENTER);
        stackpane.getChildren().add(model.sampleLine);
		
		UCLeft.getChildren().add(stackpane);		
		UCLeft.getChildren().add(model.strokeSlider);
		UCLeft.getChildren().add(model.btnClear);		
		UCRight.getChildren().add(model.colorLabel);
	}
	
	public void setFlowPane(VBox UCRight) {
		//set up the color picker grid
		FlowPane flow = new FlowPane();
        flow.setVgap(2);
        flow.setHgap(2);
        flow.setPrefWrapLength(400);
        // Get the declared fields for the Color class
        Field[] colorFields = Color.class.getDeclaredFields();
        for (Field fieldname : colorFields) {
            int mods = fieldname.getModifiers();
            // Only look at the field if it's public, static, and NOT 'TRANSPARENT'
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)
                    && !(fieldname.getName().equals("TRANSPARENT"))) {
                try {
                    Color c = Color.web(fieldname.getName());
                    // Make a rectangle with that field name's color
                    final Rectangle r = new Rectangle(15, 15, c);
                    // Configure the rectangle
                    r.setCursor(Cursor.HAND);
                    Tooltip t = new Tooltip(fieldname.getName().toLowerCase());
                    Tooltip.install(r, t);
                    r.setUserData(t.getText());
                    r.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override

                        public void handle(MouseEvent me) {

                            model.sampleLine.setStroke(r.getFill());

                            model.colorLabel.setText("color: " + ((String) r.getUserData()));

                        }

                    });
                    flow.getChildren().add(r);
                }catch (IllegalArgumentException e) {
                    // just ignore it if for some reason we can't make
                    // a color
                }
            }
        }
        UCRight.getChildren().add(flow);
	}
	
	public void drawModelPaths() {
		//DRAW MODELpATHS
		//darw the relativized modelpath coordinates
		//do everything the same, but! multiply by the canvas height and width where appropriate
				for (int i=0; i<model.modelPaths.size();i++) {	
					gc.beginPath();
					gc.moveTo(model.modelPathsCoordinates.get(i).x*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(i).x),
							model.modelPathsCoordinates.get(i).y*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(i).y));											
					gc.setStroke(model.modelPaths.get(i).getStroke());
					gc.setLineWidth(model.modelPaths.get(i).getStrokeWidth());
					if (model.modelPaths.get(i).getElements().size()>0) {
					final double translateX = iModel.modelPathsTranslateByCoordinates.get(i).x;
					final double translateY = iModel.modelPathsTranslateByCoordinates.get(i).y;						
					model.modelPaths.get(i).getElements().
					forEach(a -> {				
						if (a instanceof LineTo) {
						//your code
						gc.lineTo(((LineTo) a).getX()*radarView.width +translateX, ((LineTo) a).getY()*radarView.height+translateY);	
						
						}			
					});
					gc.stroke();
				}
					
				}
	}
	
	
	public void modelChanged() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		gc.setFill(Color.RED);
		gc.fillRect(375, 150, 50, 50);
		drawModelPaths();				
	}
	public void startPath(double x, double y) {
		//should take into account the size of the view
		//need to relativize the coodinates
		//0 = left
		//1.0 = right
		pathStartx = x*width;
		pathStarty = y*height;
	}
	public void strokePath(double x, double y) {
        gc.setLineWidth(model.sampleLine.getStrokeWidth());
        gc.setStroke(model.sampleLine.getStroke());
		gc.strokeLine(pathStartx, pathStarty, x*width, y*height);
	}
	

	public void setModel(Draw2Model m) {
		model = m;
	}
	public void setIModel(InteractionModel iM) {
		iModel = iM;
	}
	public void setModelRadarView(Draw2miniMap v) {
		radarView = v;
	}

}
