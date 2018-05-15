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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * radar view for the model
 * receives notifications from the model? to draw the lines of the user.
 * @author HCI Lab
 *
 */
public class Draw2miniMap extends Draw2View implements modelListener {
	
	public Draw2miniMap(double h, double w, Draw2Model m) {
		super(h,w,m);
		//FIGURE OUT HOW TO CONTAIN THIS BACKGROUND COLOR
		//this.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
		
		setCanvas(3,4);
		this.gc.setStroke(Color.BLACK);
		this.gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
	}
	


	@Override
	public void setLineGroup() {
		//do nothing
	}
	
	@Override
	public void setFlowPane(VBox UCRight){
		//do nothing
	}
	@Override
	public void setCanvas(double h, double w) {
		c = new Canvas(1000/7,1000/7);
		gc = c.getGraphicsContext2D();
		this.getChildren().add(c);
	}
	
	
	@Override
	public void setSampleStroke(VBox UCLeft, VBox UCRight) {
		//do nothing
	}
	
	public void modelChanged() {
		strokePath();
		//gc.setFill(Color.WHITE);
		//gc.fillRect(0, 0, height, width);
	}
	
	
	public void strokePath() {
		//get the path list from the model
		//get the path translate by coordinates from the model
		//reproduce each path within the bounds of the minimap
		
		for (int i = 0; i< model.modelPaths.size();i++) {
			
		}
	}

}
