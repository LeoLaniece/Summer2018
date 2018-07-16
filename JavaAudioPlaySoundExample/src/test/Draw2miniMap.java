package test;

import java.io.File;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class is in charge of drawing all elements in the application radarView
 * receives notifications from the model and from the network to draw elements
 * @author HCI Lab
 *
 */
public class Draw2miniMap extends Draw2View implements modelListener {
	/**
	 * The workspace is scaled down in the radarView by this unit
	 * ex) if you have a 1000 by 1000 workspace, the radar view will appear as a 1000/scale by 1000/scale 
	 * representation in the application
	 */
	int scale ;
	/**
	 * Variable which indicates if there is a network viewPort to be drawn
	 */
	public boolean hasNetMiniMap = false; 
	/**
	 * Variable which indicates the location of the network viewPort (used for networking)
	 */
	public double netMiniMapX, netMiniMapY;
	/**
	 * Variable which indicates the last know location of a path segment drawn from the network
	 */
	public Coordinate lastNetPathCoordinate = null;	
	/**
	 * constructor for application radarView
	 * @param w width of the total virtual workspace
	 * @param h height of the total virtual workspace
	 * @param m model for the drawing application
	 */
	public Draw2miniMap(double w, double h, Draw2Model m) {		
		super(w,h,m);		
		setCanvas();			
	}
	
	/**
	 * Will draw the viewPort on in the radarView
	 * The viewPort represents the relative location of the canvas within the total workspace
	 */
	public void drawViewPort() {
		gc.setLineWidth(5);
		gc.setStroke(Color.CADETBLUE);
		gc.strokeRect(iModel.viewPortX, iModel.viewPortY, iModel.viewPortWidth,iModel.viewPortHeight);
	}
	/**
	 * Will draw the viewPort location from the information sent by the network
	 * Should render a yellow rectangle in the radar view which indicates the location of the other user's 
	 * viewPort location
	 * @param x x coordinate of network viewPort
	 * @param y y coordinate of network viewPort
	 */
	public void drawViewPortFromNet(double x, double y) {
		//all changes made to the canvas must be made on the Platform thread if the change is concurrent to other threads
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {			    			    			    	
		gc.drawImage(getImage(), 0, 0, width/scale, height/scale);		
		drawModelPaths();
		drawViewPort();	
		hasNetMiniMap = true;
		gc.setLineWidth(1);
		gc.setStroke(Color.YELLOW);
		gc.strokeRect(x, y, iModel.viewPortWidth, iModel.viewPortHeight);
		netMiniMapX =x;
		netMiniMapY =y;
		if (controller.drawViewPort) {
		//do nothing
	}else {
		//paint over everything
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, c.getHeight(), c.getWidth());
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
	}
		    }
		});
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
	/**
	 * Initializes the canvas for the radarView 
	 */
	@Override
	public void setCanvas() {
		//needs to be initialized here for some reason
		scale =7;
		
		c = new Canvas(width/scale,height/scale);
		gc = c.getGraphicsContext2D();
		this.getChildren().add(c);
        gc.drawImage(image, 0, 0, width/scale, height/scale);		
	//	gc.setStroke(Color.BLACK);
	//	gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
	}
	
	@Override
	public void setSampleStroke(VBox UCLeft, VBox UCRight) {
		//do nothing
	}
	/**
	 * Draw all the model paths onto the radarView
	 * An expensive operation
	 */
	@Override
	public void drawModelPaths() {		
		//synchronized means no other thread can access getModelPaths() while this code is executing
				synchronized (model.getModelPaths()) {
						for (int i=0; i<model.getModelPaths().size();i++) {	
							gc.beginPath();
							gc.moveTo(model.modelPathsCoordinates.get(i).x*width/scale + iModel.viewPortXYLocation.get(i).x,
									model.modelPathsCoordinates.get(i).y*height/scale+ iModel.viewPortXYLocation.get(i).y );											
							gc.setStroke(model.getModelPaths().get(i).getStroke());
							gc.setLineWidth(model.getModelPaths().get(i).getStrokeWidth()/scale);
							if (model.getModelPaths().get(i).getElements().size()>0) {
							final double viewPortOffSetX = iModel.viewPortXYLocation.get(i).x;
							final double viewPortOffSetY = iModel.viewPortXYLocation.get(i).y;							
							for (int a = 0; a<model.getModelPaths().get(i).getElements().size(); a++) {
								if (model.getModelPaths().get(i).getElements().get(a) instanceof LineTo) {
								gc.lineTo(((LineTo) model.getModelPaths().get(i).getElements().get(a)).getX()
										*width/scale +viewPortOffSetX, 
										((LineTo) model.getModelPaths().get(i).getElements().get(a)).getY()
										*height/scale +viewPortOffSetY);	
								}
							}															
							}							
							gc.stroke();
							}				
						}		
	}
	/**
	 * Paint over the entire radarView
	 */
	@Override
	public void paintOverPaths() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, c.getHeight(), c.getWidth());
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
	}
	/**
	 * Whenever the model changes:
	 * Draw something
	 */
	@Override
	public void modelChanged() {
		//all changes made to the canvas must be made on the Platform thread if the change is concurrent to other threads
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {				    			    	
		//if the user is panning the viewPort, draw eveything in the new location
		if (controller.state == controller.PAN_READY) {
			gc.drawImage(getImage(), 0, 0, width/scale, height/scale);
			drawModelPaths();
		drawViewPort();	
		//if there is an active network viewPort, draw it.
		if (hasNetMiniMap == true){
			drawViewPortFromNet(netMiniMapX, netMiniMapY);
		}}
		//if the user is not panning the viewPort they must be drawing something
		else {
			//add a segment to the path
			drawPath();
		}				
		//if the user is conducting a variant of the freeze test task
		if (iModel.freezeTest) {
			paintOverPaths();
		}
		//in any case trace the outline of the radarView
		gc.setLineWidth(1);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
		drawViewPort();
		//if the task requires the radarView to be covered, cover it
		if (!controller.drawViewPort) {		
		    		gc.setFill(Color.WHITE);
		    		gc.fillRect(0, 0, c.getHeight(), c.getWidth());
		    		gc.setLineWidth(1);
		    		gc.setStroke(Color.BLACK);
		    		gc.strokeRect(0, 0, c.getHeight(), c.getWidth());
		    	}
		    }
		});
	}
	/**
	 * Will draw the background image scaled down to fit the radarView
	 */
	@Override
	public void drawImage() {			
		synchronized (getImage()) {
			gc.drawImage(getImage(), 0, 0, width/scale, height/scale);
			drawModelPaths();
		}		
	}	
	
	/**
	 * calculate the coordinate of the center of the netWork viewport
	 * @return coordinate network viewPort center location
	 */
	public Coordinate calculateNetViewPortCenter() {
		//if the network viewPort is active
		if (hasNetMiniMap) {
			Coordinate p =new Coordinate((((netMiniMapX+(iModel.viewPortWidth/2)))*7)/width, 
				(((netMiniMapY+(iModel.viewPortHeight/2)))*7)/height);				
		return p;
		}else {
			//if the network view port has never been active, return the center of the view port
			//when the stage begins
			Coordinate p = new Coordinate((((0+(iModel.viewPortWidth/2)))*7)/width, 
					(((0+(iModel.viewPortHeight/2)))*7)/height);			
			return p;
		}

	}
	
	/**
	 * calculate the coordinate of the center of the viewport
	 * @return coordinate network viewPort center location
	 */
	public Coordinate calculateViewPortCenter() {
		//iModel.viewPortX, iModel.viewPortY, iModel.viewPortWidth,iModel.viewPortHeight
			//if the network view port has never been active, return the center of the view port
			//when the stage begins
			Coordinate p = new Coordinate((((iModel.viewPortX+(iModel.viewPortWidth/2)))*7)/width, 
					(((iModel.viewPortY+(iModel.viewPortHeight/2)))*7)/height);			
			return p;
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
	
	@Override
	public void addToPath() {
		gc.beginPath();
		synchronized (model.getModelPaths()) {
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() > 2) {					
		//move to the second last coordinate on the elements list
		gc.moveTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getX())
				*width/scale+iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).x, 
				(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getY())
				*height/scale+ iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).y);	
		gc.setStroke(model.getModelPaths().get(model.getModelPaths().size()-1).getStroke());
		gc.setLineWidth(model.getModelPaths().get(model.getModelPaths().size()-1).getStrokeWidth()/scale);
		
		//draw a LineTo to the last element in the path
		gc.lineTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getX())
				*width/scale+iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).x, 
				(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getY())
				*height/scale+ iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).y);		
		//stroke the bit
		gc.stroke();
		}
		}
	}
	@Override
	public void drawPath() {
		synchronized (model.getModelPaths()) {		
		if (model.getModelPaths().size()>0) {		
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() > 2) {
			addToPath();
		}else if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() ==2){
			gc.beginPath();
			gc.moveTo((((MoveTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getX())
					*width/scale+iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).x, 
					(((MoveTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getY())
					*height/scale+ iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).y);	
			gc.setStroke(model.getModelPaths().get(model.getModelPaths().size()-1).getStroke());
			gc.setLineWidth(model.getModelPaths().get(model.getModelPaths().size()-1).getStrokeWidth()/scale);			
			//draw a LineTo to the last element in the path
			gc.lineTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getX())
					*width/scale+iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).x, 
					(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getY())
					*height/scale+ iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).y);
			//stroke the bit
			gc.stroke();
		}
		}	
		}
	}
	
	//for drawing the netPath
	@Override
	public void addToNetPath() {
		gc.beginPath();
		synchronized (model.getNetWorkPath()) {
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getNetWorkPath().getElements().size() > 2) {					
		//move to the second last coordinate on the elements list
		gc.moveTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getX())
				*width/scale+model.netPathViewPortXYLocation.x, 
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getY())
				*height/scale+ model.netPathViewPortXYLocation.y);	
		gc.setStroke(model.getNetWorkPath().getStroke());
		gc.setLineWidth(model.getNetWorkPath().getStrokeWidth()/scale);
		
		//draw a LineTo to the last element in the path
		gc.lineTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX())
				*width/scale+model.netPathViewPortXYLocation.x, 
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY())
				*height/scale+ model.netPathViewPortXYLocation.y);		
		lastNetPathCoordinate = new Coordinate(
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX()),
				((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY());
		//stroke the bit
		gc.stroke();
		}
		}
		if (!controller.drawViewPort) {
			paintOverPaths();
		}
	}
	@Override
	public void drawNetPath() {
		synchronized (model.getNetWorkPath()) {		
		if (model.getNetWorkPath()!=null) {		
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getNetWorkPath().getElements().size() > 2) {
			addToNetPath();
		}else if (model.getNetWorkPath().getElements().size() ==2){
			gc.beginPath();
			gc.moveTo((((MoveTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getX())
					*width/scale+model.netPathViewPortXYLocation.x, 
					(((MoveTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getY())
					*height/scale+ model.netPathViewPortXYLocation.y);	
			gc.setStroke(model.getNetWorkPath().getStroke());
			gc.setLineWidth(model.getNetWorkPath().getStrokeWidth()/scale);			
			//draw a LineTo to the last element in the path
			gc.lineTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX())
					*width/scale+model.netPathViewPortXYLocation.x, 
					(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY())
					*height/scale+ model.netPathViewPortXYLocation.y);			
			lastNetPathCoordinate = new Coordinate(
					(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX()),
					((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY());
			//stroke the bit
			gc.stroke();
		}
		}	
		}
		if (!controller.drawViewPort) {
			paintOverPaths();
		}
	}
	
	public Coordinate getLastKnownCoordinate() {
		//get lastPahtCoordinate not to be null!!	
		//System.out.println("net minimap width: "+getNetNormalizedViewPortWidth());
		//System.out.println("net minimap h: "+getNetNormalizedViewPortHeight());
		
		if (lastNetPathCoordinate !=null && isInViewPort(lastNetPathCoordinate)) {				
				return lastNetPathCoordinate;					
		}else {
			return calculateNetViewPortCenter();
		}		
	}		
	
	public double getNetNormalizedViewPortX() {
		return (netMiniMapX/width) *scale;
	}
	public double getNetNormalizedViewPortY() {
		return (netMiniMapY/height) *scale;
	}
	public double getNetNormalizedViewPortWidth() {
		return (iModel.viewPortWidth/width) *scale;
	}
	public double getNetNormalizedViewPortHeight() {
		return (iModel.viewPortHeight/height) *scale;
	}
	
	public boolean isInViewPort(Coordinate p) {
		if (p.x > getNetNormalizedViewPortX()&&
				p.x < getNetNormalizedViewPortX()+getNetNormalizedViewPortWidth()&&
				p.y > getNetNormalizedViewPortY()&&
				p.y < getNetNormalizedViewPortY()+getNetNormalizedViewPortHeight()) {
			return true;
		}else {
			return false;
		}
	}
}

