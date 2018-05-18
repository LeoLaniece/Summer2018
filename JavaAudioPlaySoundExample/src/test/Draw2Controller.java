package test;







import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javafx.scene.input.MouseEvent;

import javax.sound.sampled.LineUnavailableException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Draw2Controller {
	public Draw2View view;
	public Draw2Model model;
	public Draw2miniMap radarView;
	double distanceTraveled = 0;
	double x;
	double y;            	
	double dx = 0;
	double dy = 0;
	boolean xDirection = false;
	boolean yDirection = false;
	ArrayList<Double> timeOfChange;
	ArrayList<Double> velocities;
	InteractionModel iModel;
	//velocityItems
	Coordinate[] points;
	
	//STATES
	int READY = 0;
	int PAN_READY = 1;
	int PANNING =2;
	int state = READY;
	long time;
	
	public Draw2Controller(Draw2View v, Draw2Model m, Draw2miniMap r) throws InterruptedException 
{
		view = v;
		model = m;
		radarView = r;
		timeOfChange = new ArrayList<>();
		points = new Coordinate[4];
		//setPoints();
		
	//when shift key is down, pan the canvas to new area's
		
	
	view.c.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {
        	System.out.println("x = "+me.getX());
        	System.out.println("y = "+me.getY());
        	setPoints(me.getX(),me.getY());
        	if (model.p != null) {
        		System.out.println("mousepressed");
        		model.p.stop();
        	}
        	
        	
        	if (me.isShiftDown()) {
        		//pan the canvas
        		//new state
        		state = PAN_READY;   
            	x = me.getX();
            	y = me.getY();
        		
        	}else {
        		state = READY;
        	}
        	if (state ==READY) {
        	//will be useful in mouseDragged for velocity
        	x = me.getX();
        	y = me.getY();
        	time = System.currentTimeMillis();   
        	//pat should be started in the model
        	model.startPath(me.getX(),me.getY());
        	velocities = new ArrayList<>();
        	
        	//view.startPath(me.getX()/view.width, me.getY()/view.height); 
        	//radarView.startPath(me.getX()/view.width, me.getY()/view.height); 
        	}
        	
                        		                        
        }
    });
	
        //in the controller
	view.c.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {                     	            	
            	if (state == READY) {
            	 //calculate speed and distance of the stroke(in the model)	
            	//model.calculateStroke(distanceTraveled,time);
            		
            	//sounds the closest to a realistic stroke sound
            	//model.playStroke(System.currentTimeMillis()-time, velocities);
            	//does not capture the stroke.
            	//could try to fix this by:
            	//adding more different kinds of strokes at different speeds?
            	//trying to stretch the strokes based on the percentages !!!
            	//this would be a different kind of mixing
            	//not by grain, instead stretch the files so that they match the duration
            	//then mix by grain (no file is played in full more than once)            	
            	//model.playMix(velocities, System.currentTimeMillis()-time);
            		
            		//currently developing this function            		
            		//model.playTest((System.currentTimeMillis()-time),velocities);
            		
            		//for threading tests
            		model.playPathSound(velocities, System.currentTimeMillis()-time);
            	distanceTraveled = 0;
            	
            	model.pathToNull();
            	}
                state = READY;
                
            }
        });
      
	view.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {  
            	
            	updatePoints(me.getX(),me.getY());            	
            	velocities.add(calculatePointsAverageVelocity());
            	
            	if (state == PAN_READY) {
            		dx =me.getX()-x;
            		dy = me.getY()-y;
            		x = me.getX();
            		y = me.getY();
            		
            		           		
            		
        		            		
            		//move the viewPort within its bounds
            		//but only if it is a drag allowed in the view port 
            		if (iModel.viewPortX +dx >=0 &&
            				iModel.viewPortY +dy >=0&&
            				(iModel.viewPortX +iModel.viewPortHeight + dx) <=1000/7 &&
            				(iModel.viewPortY +iModel.viewPortWidth +dy) <=1000/7) {
            		iModel.viewPortX += dx/7;
            		iModel.viewPortY += dy/7;
            		//drag the paths around to make it seem like we are panning the background
            		for (int a = 0; a < model.modelPaths.size(); a++) {
            			iModel.modelPathsTranslateByCoordinates.get(a).x -=dx;
            			iModel.modelPathsTranslateByCoordinates.get(a).y-=dy;            			
            			model.modelPaths.get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);
            			model.modelPaths.get(a).setTranslateY(iModel.modelPathsTranslateByCoordinates.get(a).y);            			
            		}            		
            		  model.notifySubscribers();    		
            		}            		
            	}
            	
            	if (state ==READY) {
            	//set up relativized view coordinates
            	double viewx = me.getX()/view.width;
            	double viewy = me.getY()/view.height;            	
            	
            	//calculate distance travel
            	//calculate the velocity interactively
            	//last 4 points, take the average of the 3 velocities
            	//functions: 
            	//setPoints()
            	//updatePoints(double x, double y)
            	//calculateVelocity(Coordinate 1, Coordinate 2)
            	//double (pixels per ms) calculatePointsAverageVelocity()
            	
            	//use microseconds
        		dx = me.getX();
        		dy = me.getY();
            	distanceTraveled += Math.abs(Math.sqrt(Math.pow((dx-x), 2)+Math.pow((dy-y), 2)));
            	
            	//calculate stroke direction
            	//set the direction travelled, calculate if there is a change in direction, set the new direction travelled
            	//the function works, but it is very sensitive
            	//calculate change in direction 
            	//if xDirection is different by at least 10 pixels than current direction, mark the time.
            	if ((x <= dx) != xDirection) {
            		//timeOfChange.add((double)System.currentTimeMillis()-time);
            	}
            	if((y <= dy) != yDirection) {
            		timeOfChange.add((double)System.currentTimeMillis()-time);
            	}            
            	if (x <= dx) {
            		xDirection = true;
            	}else {
            		xDirection = false;
            	}
            	
            	//for some reason, when i move the cursor to from right to left
            	//there are more changes in direction than left to right
            	//try calculating direction when the mouse acceleration changes
            	
            	if(y < dy) {
            		yDirection = true;
            	}else if (y>dy){
            		yDirection = false;
            	}
            	//use the events in timeOfChange to generate swipe sounds at those times in the actual sound recording.
            	//could do this by running a thread at the same time as the strokePlayback 
            	//Objective: produce a sound recording which produces swipes at the given times in a sound recording.
            	//do this by mixing silence and the swipe. 
            	
            	
            	
            	x = dx;
            	y = dy;
            	// keep lines within rectangle

                if (view.c.getBoundsInLocal().contains(me.getX()+model.path.getStrokeWidth(), me.getY()+model.path.getStrokeWidth())) {
                	
                	//create new path in the controller
                	
                	model.strokePath(me.getX(), me.getY());
                	//view.strokePath(viewx, viewy); 
                	//view.startPath(viewx, viewy);
                
                //draw in the radarView               
                //	radarView.strokePath(viewx, viewy); 
                //	radarView.startPath(viewx, viewy);
                }
            	}
                }
        });		

	}
	
	//velocity calculation functions
	//need to test these
	/**
	 * initializes the points for velocity calculation of given stroke
	 * x,y = mousePressed location
	 * 
	 */
	public void setPoints(double x, double y) {
		for (int i =0;i<4;i++) {
			points[i] = new Coordinate(x,y);
			points[i].time = System.currentTimeMillis();
		}
	}
	/**
	 * update the points to have the 4 previous mouse locations
	 * @param x
	 * @param y
	 */
	public void updatePoints(double x, double y) {

		long startTime = System.currentTimeMillis();		
		for (int i =3;i>0;) {
			if (System.currentTimeMillis()-startTime >10) {
				points[i].x = points[i-1].x;
			points[i].y = points[i-1].y;
			points[i].time = System.currentTimeMillis();

				startTime = System.currentTimeMillis();
				i--;
			}			
		}
		while (System.currentTimeMillis()-startTime <10) {
			//wait
		}
		points[0].x = x;
		points[0].y = y;
		points[0].time = System.currentTimeMillis();

			

	}
	/**
	 * calculate given velocity between 2 coordinates
	 * a should be earlier in time than b
	 * @param a
	 * @param b
	 * @param startTime
	 * @return
	 */
	public double calculateVelocity(Coordinate a, Coordinate b) {
		double distanceTraveled = Math.abs(Math.sqrt(Math.pow((b.x-a.x), 2)+Math.pow((b.y-a.y), 2)));
		return distanceTraveled/(a.time-b.time);
	}
	//last 4 points, take the average of the 3 velocities
	//functions: 	
	//double (pixels per ms) calculatePointsAverageVelocity()
	/**
	 * will return the average velocity between the 4 previous mouse locations
	 * @return 
	 */
	public double calculatePointsAverageVelocity() {		
		double average =0;
		for (int i =0;i<3;i++) {
			average+=calculateVelocity(points[0], points[i+1]);
		}
		return average/3;
		
	}
	public void setIModel(InteractionModel iM) {
		iModel = iM;
	}
}
