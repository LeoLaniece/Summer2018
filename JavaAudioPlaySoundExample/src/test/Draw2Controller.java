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
		
	//when shift key is down, pan the canvas to new area's
		
	
	view.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {
        	System.out.println("x = "+me.getX());
        	System.out.println("y = "+me.getY());
        	
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
        	
        	//view.startPath(me.getX()/view.width, me.getY()/view.height); 
        	//radarView.startPath(me.getX()/view.width, me.getY()/view.height); 
        	}
        	
                        		                        
        }
    });
	
        //in the controller
	view.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {            	
            	 //calculate speed and distance of the stroke(in the model)
            	
            	//would be informative to reproduce the edges in the stroke
            	//every time the stroke changes direction (+,+ -> -,-)
            	
            	//model.calculateStroke(distanceTraveled,time);
            	distanceTraveled = 0;
            	//timeOfChange.forEach(a -> System.out.println("change in direction at "+a));
            	//timeOfChange.removeAll(timeOfChange);
            	model.pathToNull();
                state = READY;
                
            }
        });
      
	view.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {  
            	
            	
            	
            	if (state == PAN_READY) {
            		dx =me.getX()-x;
            		dy = me.getY()-y;
            		x = me.getX();
            		y = me.getY();
            		//drag the paths around to make it seem like we are panning the background
            		for (int a = 0; a < model.modelPaths.size(); a++) {
            			model.modelPathsTranslateByCoordinates.get(a).x -=dx;
            			model.modelPathsTranslateByCoordinates.get(a).y-=dy;            			
            			model.modelPaths.get(a).setTranslateX(model.modelPathsTranslateByCoordinates.get(a).x);
            			model.modelPaths.get(a).setTranslateY(model.modelPathsTranslateByCoordinates.get(a).y);
            			
            		}
            	}
            	
            	if (state ==READY) {
            	//set up relativized view coordinates
            	double viewx = me.getX()/view.width;
            	double viewy = me.getY()/view.height;            	
            	
            	//calculate distance travel
            	//calculate the velocity interactively
            	//last 4 point, take the average of the 3 velocities
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
	
	
//fix this up!
	/*
	view.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case UP:    goNorth = true; break;
                case DOWN:  goSouth = true; break;
                case LEFT:  goWest  = true; break;
                case RIGHT: goEast  = true; break;
                case SHIFT: running = true; break;
            }
        }
    });*/
	}

}
