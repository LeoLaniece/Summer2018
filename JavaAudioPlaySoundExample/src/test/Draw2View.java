package test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class Draw2View extends Pane implements modelListener{
	Draw2Model model;
	Canvas c;
	GraphicsContext gc;
	
	public Draw2View(Draw2Model m) {
		model = m;
		c = new Canvas(700,300);
		gc = c.getGraphicsContext2D();	
		VBox root = new VBox();
		HBox underCanvas = new HBox();
		this.getChildren().add(root);
		gc.setFill(Color.AQUA);
		gc.fillRect(0, 0, 700, 300);
		root.getChildren().add(c);
		root.getChildren().add(underCanvas);
		VBox UCLeft = new VBox();
		UCLeft.setPrefWidth(300);
		VBox UCRight = new VBox();
		UCRight.setPrefWidth(300);
		underCanvas.getChildren().add(UCLeft);
		underCanvas.getChildren().add(UCRight);
		UCLeft.getChildren().add(model.labelStroke);		
		UCLeft.getChildren().add(model.sampleLine);		
		UCLeft.getChildren().add(model.strokeSlider);
		UCLeft.getChildren().add(model.btnClear);
		
		UCRight.getChildren().add(model.colorLabel);
		
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
		gc.setFill(Color.BISQUE);
		gc.fillRect(0, 0, 700, 400);
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
