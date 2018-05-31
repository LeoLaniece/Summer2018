package test;

import java.lang.reflect.Field;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

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
import javafx.scene.shape.LineTo;
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
	
	int scale;
	
	
	public Draw2miniMap(double w, double h, Draw2Model m) {
		
		super(w,h,m);
		
		setCanvas(3,4);
		
		this.gc.setStroke(Color.BLACK);
		this.gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
	}
	public void drawViewPort() {
		gc.setStroke(Color.CADETBLUE);
		gc.strokeRect(iModel.viewPortX, iModel.viewPortY, iModel.viewPortWidth,iModel.viewPortHeight);
	}
	
	@Override
	public void mainViewOnly() {
		//do nothing
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
		scale =7;
		c = new Canvas(width/scale,height/scale);
		gc = c.getGraphicsContext2D();
		this.getChildren().add(c);
		
	}
	
	
	@Override
	public void setSampleStroke(VBox UCLeft, VBox UCRight) {
		//do nothing
	}
	@Override
	public void drawModelPaths() {
		//DRAW MODELpATHS
		//draw the paths within the off set bounds of the viewPort
		//when a path is created, record the location of viewPort X and Y
		//for that path, always draw it with the X and Y offset.
		double viewHeight = model.view.height;
		double viewWidth = model.view.width;
				for (int i=0; i<model.modelPaths.size();i++) {	
					gc.beginPath();
					gc.moveTo(model.modelPathsCoordinates.get(i).x*width/scale + iModel.viewPortXYLocation.get(i).x,
							model.modelPathsCoordinates.get(i).y*height/scale+ iModel.viewPortXYLocation.get(i).y );											
					gc.setStroke(model.modelPaths.get(i).getStroke());
					gc.setLineWidth(model.modelPaths.get(i).getStrokeWidth()/scale);
					if (model.modelPaths.get(i).getElements().size()>0) {
					final double viewPortOffSetX = iModel.viewPortXYLocation.get(i).x;
					final double viewPortOffSetY = iModel.viewPortXYLocation.get(i).y;
					model.modelPaths.get(i).getElements().
					forEach(a -> {				
						if (a instanceof LineTo) {
						//your code
						gc.lineTo(((LineTo) a).getX()*width/scale +viewPortOffSetX , ((LineTo) a).getY()*height/scale +viewPortOffSetY);	
						
						}			
					});
					gc.stroke();
				}					
				}
	}
	@Override
	public void modelChanged() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, c.getHeight(), c.getWidth());
		gc.setLineWidth(1);
		this.gc.setStroke(Color.BLACK);
		this.gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
		drawViewPort();
		drawModelPaths();				
	}
	
	

}
