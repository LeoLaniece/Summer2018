package test;

import java.lang.reflect.Field;

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
	double height;
	double width;
	double logicalHeight;
	double logicalWidth;
	public Group lineGroup;
	
	//put in a logical size?
	public Draw2View(double h, double w, Draw2Model m) {
		model = m;
		height = h;
		width = w;
		logicalHeight = 1000;
		logicalWidth =1000;
		lineGroup = model.lineGroup;
		//setLineGroup();
		
		
		//dont need the canvas
		
		//gc = c.getGraphicsContext2D();	
		topPane = new Pane();////
		
		setCanvas(1000,1000);
setLineGroup();
        
		
		VBox root = new VBox();
		HBox underCanvas = new HBox();
		this.getChildren().add(root);

		
		//topPane.getChildren().add(c);
		
		root.getChildren().add(topPane);
		
		root.getChildren().add(underCanvas);
		VBox UCLeft = new VBox();
		UCLeft.setPrefWidth(width);
		UCLeft.setAlignment(Pos.BOTTOM_LEFT);
		VBox UCRight = new VBox();
		UCRight.setPrefWidth(width);
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
		group.setPrefHeight(300);
		group.setPrefWidth(800);
		topPane.getChildren().add(group);
		
	}

	
	public void setCanvas(double h, double w) {
		c = new Canvas(800,300);
		gc = c.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, 800, 500);
		topPane.getChildren().add(c);
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
	
	
	
	
	public void modelChanged() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, height, width);
		//line group
		//c.getChildren().add(path);
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
	
		/*
        Button btnClear = new Button();
        btnClear.setText("Clear");
        
        StackPane stackpane = new StackPane();
        stackpane.setPrefHeight(model.MAXSTROKE);
        stackpane.setPrefWidth(model.sampleLine.getEndX() + model.MAXSTROKE);
        stackpane.setAlignment(Pos.CENTER);
        stackpane.getChildren().add(model.sampleLine);

        // Build the color label and its layout container
        final Label colorLabel = new Label("color: blue");
        StackPane stackpane2 = new StackPane();
        stackpane2.setPrefHeight(model.MAXSTROKE);
        stackpane2.setPrefWidth(model.sampleLine.getEndX() + model.MAXSTROKE);
        stackpane2.setAlignment(Pos.CENTER_LEFT);
        stackpane2.getChildren().add(colorLabel);

        // Build the HBox layout container for the two stackpane's
        HBox lineBox = new HBox(20);
        lineBox.setAlignment(Pos.CENTER);
        lineBox.getChildren().addAll(stackpane, stackpane2);
        
     // Build the color picker and use a flowpane
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
                    flow.getChildren().add(r);
                }catch (IllegalArgumentException e) {
                    // just ignore it if for some reason we can't make
                    // a color
                }
            }
        }
        
     // Build the layout container for the VBox utility box and the flowpane
        HBox toolBox = new HBox(10);
        toolBox.setAlignment(Pos.TOP_CENTER);
        toolBox.getChildren().addAll(utilBox, flow);        
        
        // Build the canvas
        final Rectangle canvas = new Rectangle(scene.getWidth() - 20, scene.getHeight() - 230);
        canvas.setCursor(Cursor.CROSSHAIR);
        canvas.setFill(Color.LIGHTGRAY);
        
        // Build the VBox container for the lineBox, canvas, and toolBox
        VBox vb = new VBox(20);
        vb.setLayoutX(10);
        vb.setLayoutY(20);
        vb.getChildren().addAll(lineBox, canvas, toolBox);
        root.getChildren().addAll(vb, lineGroup);
        
                    
	}*/
	public void setModel(Draw2Model m) {
		model = m;
	}


}
