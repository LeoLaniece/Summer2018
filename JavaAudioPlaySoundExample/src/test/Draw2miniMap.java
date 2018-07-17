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
		synchronized (getGC()) {
			getGC().setLineWidth(5);
			getGC().setStroke(Color.CADETBLUE);
			getGC().strokeRect(iModel.viewPortX, iModel.viewPortY, iModel.viewPortWidth,iModel.viewPortHeight);
		}
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
		    	synchronized (getGC()) {
		    		getGC().drawImage(getImage(), 0, 0, width/scale, height/scale);		
		drawModelPaths();
		drawViewPort();	
		hasNetMiniMap = true;
		getGC().setLineWidth(1);
		getGC().setStroke(Color.YELLOW);
		getGC().strokeRect(x, y, iModel.viewPortWidth, iModel.viewPortHeight);
		netMiniMapX =x;
		netMiniMapY =y;
		if (controller.drawViewPort) {
		//do nothing
	}else {
		//paint over everything
		getGC().setFill(Color.WHITE);
		getGC().fillRect(0, 0, c.getHeight(), c.getWidth());
		getGC().setLineWidth(1);
		getGC().setStroke(Color.BLACK);
		getGC().strokeRect(0, 0, c.getHeight(), c.getWidth());
	}
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
					//getGC is access to the canvas graphics context 2d, only one thread may use it at a time
					synchronized (getGC()) {
						for (int i=0; i<model.getModelPaths().size();i++) {	
							getGC().beginPath();
							//each path begins with a MoveTo element 
							getGC().moveTo(model.modelPathsCoordinates.get(i).x*width/scale + iModel.viewPortXYLocation.get(i).x,
									model.modelPathsCoordinates.get(i).y*height/scale+ iModel.viewPortXYLocation.get(i).y );											
							getGC().setStroke(model.getModelPaths().get(i).getStroke());
							getGC().setLineWidth(model.getModelPaths().get(i).getStrokeWidth()/scale);
							if (model.getModelPaths().get(i).getElements().size()>0) {
							final double viewPortOffSetX = iModel.viewPortXYLocation.get(i).x;
							final double viewPortOffSetY = iModel.viewPortXYLocation.get(i).y;		
							//go through all LineTo elements of each path and draw them
							for (int a = 0; a<model.getModelPaths().get(i).getElements().size(); a++) {
								if (model.getModelPaths().get(i).getElements().get(a) instanceof LineTo) {
									getGC().lineTo(((LineTo) model.getModelPaths().get(i).getElements().get(a)).getX()
										*width/scale +viewPortOffSetX, 
										((LineTo) model.getModelPaths().get(i).getElements().get(a)).getY()
										*height/scale +viewPortOffSetY);	
								}
							}															
							}							
							getGC().stroke();
							}	
					}
						}		
	}
	/**
	 * Paint over the entire radarView
	 */
	@Override
	public void paintOverPaths() {
		synchronized (getGC()) {
		getGC().setFill(Color.WHITE);
		getGC().fillRect(0, 0, c.getHeight(), c.getWidth());
		getGC().setLineWidth(1);
		getGC().setStroke(Color.BLACK);
		getGC().strokeRect(0, 0, c.getHeight(), c.getWidth());
	}
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
		    	synchronized (getGC()) {
		//if the user is panning the viewPort, draw eveything in the new location
		if (controller.state == controller.PAN_READY) {
			getGC().drawImage(getImage(), 0, 0, width/scale, height/scale);
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
		getGC().setLineWidth(1);
		getGC().setStroke(Color.BLACK);
		getGC().strokeRect(0, 0, c.getHeight(), c.getWidth());
		drawViewPort();
		//if the task requires the radarView to be covered, cover it
		if (!controller.drawViewPort) {		
			getGC().setFill(Color.WHITE);
			getGC().fillRect(0, 0, c.getHeight(), c.getWidth());
			getGC().setLineWidth(1);
			getGC().setStroke(Color.BLACK);
			getGC().strokeRect(0, 0, c.getHeight(), c.getWidth());
		    	}
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
			synchronized (getGC()) {
				getGC().drawImage(getImage(), 0, 0, width/scale, height/scale);
			}
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
			Coordinate p = new Coordinate((((0+(iModel.viewPortWidth/2)))*7)/width, 
					(((0+(iModel.viewPortHeight/2)))*7)/height);			
			return p;
		}
	}
	
	/**
	 * normalize given coordinate
	 */
	public Coordinate normalizeViewPortValues(double x, double y) {
		return new Coordinate(x*7/width,y*7/height);
	}
	
	/**
	 * calculate the coordinate of the center of the netWork viewport
	 * @return coordinate network viewPort center location
	 */
	public Coordinate calculateNormalizedNetViewPortCenter() {
		//if the network viewPort is active
		if (hasNetMiniMap) {
			Coordinate p =new Coordinate((((netMiniMapX+(iModel.viewPortWidth/2)))*7)/width, 
				(((netMiniMapY+(iModel.viewPortHeight/2)))*7)/height);				
		return p;
		}else {
			Coordinate p = new Coordinate((((0+(iModel.viewPortWidth/2)))*7)/width, 
					(((0+(iModel.viewPortHeight/2)))*7)/height);			
			return p;
		}
	}
	
	
	/**
	 * calculate the coordinate of the center of the local viewport
	 * @return coordinate network viewPort center location
	 */
	public Coordinate calculateViewPortCenter() {
			Coordinate p = new Coordinate((((iModel.viewPortX+(iModel.viewPortWidth/2)))*7)/width, 
					(((iModel.viewPortY+(iModel.viewPortHeight/2)))*7)/height);			
			return p;
		}
	
	@Override
	/**
	 * Adds a single segment to the current path
	 */
	public void addToPath() {		
		synchronized (model.getModelPaths()) {
			synchronized (getGC()) {				
				//if the current path has more than 2 elements
		if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() > 2) {	
			getGC().beginPath();
			//MoveTo the second last in the list
			getGC().moveTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getX())
				*width/scale+iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).x, 
				(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getY())
				*height/scale+ iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).y);	
			getGC().setStroke(model.getModelPaths().get(model.getModelPaths().size()-1).getStroke());
			getGC().setLineWidth(model.getModelPaths().get(model.getModelPaths().size()-1).getStrokeWidth()/scale);		
		//draw a LineTo to the last element in the path
			getGC().lineTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getX())
				*width/scale+iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).x, 
				(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getY())
				*height/scale+ iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).y);		
		
			getGC().stroke();
		}
		}
		}
	}
	@Override
	/**
	 * Determines which course of action is appropriate based on  
	 * the number of elements in the current path.
	 * 
	 * Adds a single segment to the current path
	 * 
	 */
	public void drawPath() {
		synchronized (model.getModelPaths()) {	
			synchronized (getGC()) {
				//if the path exists
		if (model.getModelPaths().size()>0) {
			//if the path has at more than 2 elements
		if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() > 2) {
			addToPath();
			//if the path has exactly 2 elements
		}else if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() ==2){
			getGC().beginPath();
			getGC().moveTo((((MoveTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getX())
					*width/scale+iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).x, 
					(((MoveTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getY())
					*height/scale+ iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).y);	
			getGC().setStroke(model.getModelPaths().get(model.getModelPaths().size()-1).getStroke());
			getGC().setLineWidth(model.getModelPaths().get(model.getModelPaths().size()-1).getStrokeWidth()/scale);			
			//draw a LineTo to the last element in the path
			getGC().lineTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getX())
					*width/scale+iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).x, 
					(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getY())
					*height/scale+ iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1).y);
			//stroke the bit
			getGC().stroke();
		}
		}	
		}
		}
	}
	
	
	@Override
	/**
	 * Adds a single segment to the current network path
	 */
	public void addToNetPath() {		
		synchronized (model.getNetWorkPath()) {
			synchronized (getGC()) {
			getGC().beginPath();		
			//if the current path has more than 2 elements
		if (model.getNetWorkPath().getElements().size() > 2) {					
			//MoveTo the second last in the list
			getGC().moveTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getX())
				*width/scale+model.netPathViewPortXYLocation.x, 
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getY())
				*height/scale+ model.netPathViewPortXYLocation.y);	
			getGC().setStroke(model.getNetWorkPath().getStroke());
			getGC().setLineWidth(model.getNetWorkPath().getStrokeWidth()/scale);
		
		//draw a LineTo to the last element in the path
			getGC().lineTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX())
				*width/scale+model.netPathViewPortXYLocation.x, 
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY())
				*height/scale+ model.netPathViewPortXYLocation.y);		
		lastNetPathCoordinate = new Coordinate(
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX()),
				((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY());
		//stroke the bit
		getGC().stroke();
		}
		}
		}
		if (!controller.drawViewPort) {
			paintOverPaths();
		}
	}
	
	@Override
	/**
	 * Determines which course of action is appropriate based on  
	 * the number of elements in the current network path.
	 * 
	 * Adds a single segment to the current network path
	 * 
	 */
	public void drawNetPath() {
		synchronized (model.getNetWorkPath()) {
		//if the path exists
		if (model.getNetWorkPath()!=null) {	
			//if the current path has more than 2 elements
		if (model.getNetWorkPath().getElements().size() > 2) {
			addToNetPath();
			//if the current path has exactly 2 elements in it
		}else if (model.getNetWorkPath().getElements().size() ==2){
			synchronized (getGC()) {
				getGC().beginPath();
				//MoveTo the second last element in the list
				getGC().moveTo((((MoveTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getX())
					*width/scale+model.netPathViewPortXYLocation.x, 
					(((MoveTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getY())
					*height/scale+ model.netPathViewPortXYLocation.y);	
				getGC().setStroke(model.getNetWorkPath().getStroke());
				getGC().setLineWidth(model.getNetWorkPath().getStrokeWidth()/scale);			
			//draw a LineTo to the last element in the path
				getGC().lineTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX())
					*width/scale+model.netPathViewPortXYLocation.x, 
					(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY())
					*height/scale+ model.netPathViewPortXYLocation.y);			
			lastNetPathCoordinate = new Coordinate(
					(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX()),
					((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY());
			//stroke the bit
			getGC().stroke();
		}
		}
		}	
		}
		if (!controller.drawViewPort) {
			paintOverPaths();
		}
	}
	
	/**
	 * Will return the last recorded coordinate of the current network path
	 * If the network path does not exist, this function will return
	 * the netViewPort center coordinate
	 * @return Coordinate, last recorded location from network activity
	 */
	public Coordinate getLastKnownCoordinate() {		
		if (lastNetPathCoordinate !=null && isInViewPort(lastNetPathCoordinate)) {				
				return lastNetPathCoordinate;					
		}else {
			return calculateNetViewPortCenter();
		}		
	}		
	
	/**
	 * Will return a value between 0 and 1 which 
	 * indicates the x-axis location of the Network's viewPort top left corner
	 * within the bounds of the radarView
	 * @return double, normalized x-axis location in context of the radarView
	 */
	public double getNetNormalizedViewPortX() {
		return (netMiniMapX/width) *scale;
	}
	
	/**
	 * Will return a value between 0 and 1 which 
	 * indicates the y-axis location of the Network's viewPort top left corner
	 * within the bounds of the radarView
	 * @return double, normalized y-axis location in context of the radarView
	 */
	public double getNetNormalizedViewPortY() {
		return (netMiniMapY/height) *scale;
	}
	
	/**
	 * Will return a value between 0 and 1 which 
	 * indicates the width of the Network's viewPort 
	 * within the bounds of the radarView
	 * @return double, normalized width size in context of the radarView
	 */
	public double getNetNormalizedViewPortWidth() {
		return (iModel.viewPortWidth/width) *scale;
	}
	
	/**
	 * Will return a value between 0 and 1 which 
	 * indicates the height of the Network's viewPort 
	 * within the bounds of the radarView
	 * @return double, normalized height size in context of the radarView
	 */
	public double getNetNormalizedViewPortHeight() {
		return (iModel.viewPortHeight/height) *scale;
	}
	
	/**
	 * Will return a boolean value
	 * Will return true if the coordinate 'p' is within the bounds of the current viewPort
	 * Will return false otherwise
	 * @param p, A Coordinate value, the x and y values must be between 0 and 1
	 * @return boolean value
	 */
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

