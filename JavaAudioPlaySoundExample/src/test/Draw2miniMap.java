package test;

import java.lang.reflect.Field;
import javafx.application.Platform;

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
	public boolean hasNetMiniMap = false; 
	public double netMiniMapX, netMiniMapY;
	
	
	public Draw2miniMap(double w, double h, Draw2Model m) {		
		super(w,h,m);
		
		setCanvas(3,4);
		
		this.gc.setStroke(Color.BLACK);
		this.gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
	}
	
	
	public void drawViewPort() {
		gc.setLineWidth(5);
		gc.setStroke(Color.CADETBLUE);
		gc.strokeRect(iModel.viewPortX, iModel.viewPortY, iModel.viewPortWidth,iModel.viewPortHeight);
	}
	
	public void drawViewPortFromNet(double x, double y) {
		hasNetMiniMap = true;
		gc.setLineWidth(1);
		gc.setStroke(Color.YELLOW);
		gc.strokeRect(x, y, iModel.viewPortWidth, iModel.viewPortHeight);
		netMiniMapX =x;
		netMiniMapY =y;
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
						for (int i=0; i<model.getModelPaths().size();i++) {	
							gc.beginPath();
							gc.moveTo(model.modelPathsCoordinates.get(i).x*width/scale + iModel.viewPortXYLocation.get(i).x,
									model.modelPathsCoordinates.get(i).y*height/scale+ iModel.viewPortXYLocation.get(i).y );											
							gc.setStroke(model.getModelPaths().get(i).getStroke());
							gc.setLineWidth(model.getModelPaths().get(i).getStrokeWidth()/scale);
							if (model.getModelPaths().get(i).getElements().size()>0) {
							final double viewPortOffSetX = iModel.viewPortXYLocation.get(i).x;
							final double viewPortOffSetY = iModel.viewPortXYLocation.get(i).y;
							synchronized (this) {
							for (int a = 0; a<model.getModelPaths().get(i).getElements().size(); a++) {
								if (model.getModelPaths().get(i).getElements().get(a) instanceof LineTo) {
								gc.lineTo(((LineTo) model.getModelPaths().get(i).getElements().get(a)).getX()
										*width/scale +viewPortOffSetX, 
										((LineTo) model.getModelPaths().get(i).getElements().get(a)).getY()
										*height/scale +viewPortOffSetY);	
								}
							}
								
							/*	
							model.getModelPaths().get(i).getElements().
							forEach(a -> {				
								if (a instanceof LineTo) {
								//your code
								gc.lineTo(((LineTo) a).getX()*width/scale +viewPortOffSetX ,
										((LineTo) a).getY()*height/scale +viewPortOffSetY);	
								
								}			
							});*/
							}
							gc.stroke();
						}					
						}		
	}
	
	@Override
	public void paintOverPaths() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, c.getHeight(), c.getWidth());
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
		drawViewPort();
	}
	
	@Override
	public void modelChanged() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, c.getHeight(), c.getWidth());
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
		drawModelPaths();
		drawViewPort();			
		if (hasNetMiniMap == true){
			drawViewPortFromNet(netMiniMapX, netMiniMapY);
		}
		if (iModel.freezeTest) {
			paintOverPaths();
		}
		
		
		    }
		});
	}
	
	
	/**
	 * calculate relative coordinates for the center of the netWork viewport
	 * @return coordinate
	 */
	public Coordinate calculateNetViewPortCenter() {		 
		if (hasNetMiniMap) {
			Coordinate p =new Coordinate((((netMiniMapX+(iModel.viewPortWidth/2)))*7)/width, 
				(((netMiniMapY+(iModel.viewPortHeight/2)))*7)/height);
		System.out.println("netMiniMap center "+p.x+" "+p.y);
		//System.out.println("viewport relative width "+iModel.viewPortWidth);
		return p;
		}else {
			Coordinate p = new Coordinate((((0+(iModel.viewPortWidth/2)))*7)/width, 
					(((0+(iModel.viewPortHeight/2)))*7)/height);
			System.out.println("netMiniMap center "+p.x+" "+p.y);
			return p;
		}

	}
	
	/**
	 * what is the max distance the center point is from the edge?
	 * @param netViewPortCenter
	 * @return
	 */
	public Coordinate distanceFromNVP(Coordinate netViewPortCenter) {
		//check top and bottom
		//take the larger value		
		double xDist = netViewPortCenter.x;
		if (xDist < (iModel.viewPortWidth - netViewPortCenter.x)) {
			xDist = iModel.viewPortWidth - netViewPortCenter.x;
		}
		double yDist = netViewPortCenter.y;
		if (yDist < (iModel.viewPortHeight - netViewPortCenter.y)) {
			yDist = iModel.viewPortHeight - netViewPortCenter.y;
		}		
		return new Coordinate(xDist,yDist);
	}

}
