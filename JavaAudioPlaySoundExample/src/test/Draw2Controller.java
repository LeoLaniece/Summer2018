package test;

import java.io.File;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import javax.sound.sampled.LineUnavailableException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.*;
import javafx.scene.input.KeyEvent; 
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


public class Draw2Controller {
	public final Draw2Controller me = this;
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
	//ArrayList<Double> timeOfChange;
	/**
	 * x = velocity in pixels per ms
	 * y = duration of velocity
	 */
	//ArrayList<Coordinate> velocities;
	InteractionModel iModel;
	public ArrayList<Coordinate> mouseCoordinates;
	public ArrayList<Long> activeTimes;
	//velocityItems
	//Coordinate[] points;
	
	//STATES
	public int READY = 0;
	public int PAN_READY = 1;
	public int NOTREADY =-1;
	public int FREEZE = 2;
	public int READ_AND_OBSERVE = 3;
	public int CLOSE_INSTRUCTIONS = 4;
	public int FREEZE_TEST_TASK =5;
	public int PROMPT_FOR_SHAPE = 6;
	public int state = NOTREADY;
	long time;
	long velocityTime;
	public double clipStaggerIncrement;
	public double clipDuration;
	public MouseTest soundVelocityThread;
	public long startTime;
	public ReadAndObserveStage readAndObserveTrial =null;
	
	public Draw2Controller(Draw2View v, Draw2Model m, Draw2miniMap r) throws InterruptedException 
{
		view = v;
		model = m;
		radarView = r;
		//timeOfChange = new ArrayList<>();
		//points = new Coordinate[4];
		 mouseCoordinates=new ArrayList<Coordinate>();
		 File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");	
		 clipStaggerIncrement = calculateStaggerIncrement(f1); 
		 clipDuration= model.player.fileLength(f1)*1000;
     	soundVelocityThread = new MouseTest();
     	soundVelocityThread.start();
     	startTime = System.currentTimeMillis();
		//setPoints();
     	 activeTimes = new ArrayList<>();
     	 model.startTaskStage(me);
     	
     	view.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {	
				FreezeQuiz fr = null;				
				if (key.getText().equals("f")) {
					if (state != FREEZE) {
						System.out.println(key.getText());
						state = FREEZE;
						view.paintOverPaths();
						radarView.paintOverPaths();
						iModel.freezeTestOn();
						//launch a pop up window requesting for the location of the other user and his activity?
						//need to pass in the location of other user's viewPort
						fr = new FreezeQuiz(me, radarView.calculateNetViewPortCenter());						
					}																	
				}
				//for logging purposes
				if (key.getCode()==KeyCode.SPACE) {
					String hash = "Time for completion "+Long.toString(((System.currentTimeMillis() -startTime)/1000));
					// CreateFile x = new	CreateFile(hash);														
				}
				//for the read and observe task
				if (key.getText().equals("r")) {					
					state = READ_AND_OBSERVE;
					model.notifySubscribers();
					readAndObserveTrial = new ReadAndObserveStage(me);					
				}
			}     		
     	});
     	

     	
     	//to zoom in and out
     /*	view.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {	
				if (key.getText().equals("+")) {
					System.out.println("pressed +");
				}				
			}     		
     	});
     	
     	view.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {	
				if (key.getText().equals("-")) {
					System.out.println("pressed -");
				}				
			}     		
     	});*/
     	
     	view.c.setOnScroll(new EventHandler<ScrollEvent>() {
     		@Override
     		public void handle(ScrollEvent scroll) {
     			//deltaY will be 40.0 if i scroll up and -40.0 if i scroll down
     			if (scroll.getDeltaY() == 40.0) {
     				System.out.println("scrolled up");
     				//view.zoomIn();
     			}
     			if (scroll.getDeltaY() == -40.0) {
     				System.out.println("scrolled down");
     			}
     			
     		}
     	});
     	
   //     view.setOnMouseMoved(new EventHandler<MouseEvent>()  {        	
 //       @Override
  //      public void handle(MouseEvent me) {
 //       	view.drawMouseCursor(me.getX(),me.getY());        	
 //       }
 //       });
     	
     	
	
	view.c.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {
        	if (state!=FREEZE) {
        	state = READY;
        	model.pathToNull();
        	
        	//keep thinking about this
        	//thread is possible
        	//special type of event Handler?
        	//objective is to keep calculating velocity, even when the mouse stops draging
        	//current method only updates calculates and adds the velocities during mouse drag events
        	//is there a better way to keep track of the mouse velocity?
        	
        	//like a velocityList coupled with a duration for the velocities?
        	//would assign a duration and velocity at the same time to the velocities array
        	
        	
        	
        	//CalculateVelocityThread thread = new CalculateVelocityThread("thread",me.getX(),me.getY(),view);
        	//thread.start();
        	
        	//setPoints(me.getX(),me.getY());
        	
       // 	if (model.p != null) {        		
      //  		model.p.stop();
       // 	}        	
        	
        	if (me.isShiftDown()) {
        		//pan the canvas
        		//new state
        		state = PAN_READY;   
            	x = me.getX();
            	y = me.getY();
            	//model.beginViewPortMovementSound();
        		
        	}else {
        		state = READY;
        	}
        	if (state ==READY) {
        	//will be useful in mouseDragged for velocity
        	x = me.getX();
        	y = me.getY();        	 
        	
        	//path should be started in the model
        	model.startPath(me.getX(),me.getY());
        	//velocities = new ArrayList<>();        	        	
        	//relativize the mouse coordinates, do same as for start path
        	mouseCoordinates.add(new Coordinate(me.getX()/radarView.width,me.getY()/radarView.height));

        	
			//model.playPathInteractively(0,//soundVelocityThread.getVelocity(),//
			//		mouseCoordinates.get(mouseCoordinates.size()-1),  
			//		clipDuration, clipStaggerIncrement);
        	
        	//view.startPath(me.getX()/view.width, me.getY()/view.height); 
        	//radarView.startPath(me.getX()/view.width, me.getY()/view.height); 
        	}
        	
        	}            		                        
        }
    });
	
        //in the controller
	view.c.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {                     	            	
            	if (state!=FREEZE) {
            	if (state == READY) {
            		//soundVelocityThread.preStop();
            		//soundVelocityThread.stop();
            	//still need sound generated appropriately for the sustained sections of the stroke
            		//-can fix this by using longer recordings from the start so that they loop less?
            		//-they would sound less distorted
            		
            		//still need the sound generated appropriately for the 'silent' portions of the stroke.
            		//-this might mean mixing 3 different sounds
            	
            	
            	//CAPTURES THE SECTIONS OF THE STROKE	            		
            	//implements playFor with filesAndDurations            		            	
            	//model.playStroke(System.currentTimeMillis()-time, velocities);            	            	            
            		
            	//GOOD FOR CAPTURING EDGES OR SHARP STROKES
            	//implements playMixStreamsFor
            		//updatePoints(me.getX(),me.getY());            	
                //	velocities.add(new Coordinate(calculatePointsAverageVelocity(), (double) (System.currentTimeMillis()-velocityTime)/1000));
            	 //           	velocities.forEach(a ->{            	            		
            	 //           		if (a.y > 0.044) {            	            		
            	 //           			a.x = 0;
            	 //           		}
            	//});	            		
            //	model.playPathSound(velocities, System.currentTimeMillis()-time, mouseCoordinates);
            	
            //	model.playStaggeredSoundThreads(System.currentTimeMillis()-time, velocities, mouseCoordinates);
            	
            	
            	distanceTraveled = 0;            	
            	model.pathToNull();
            	//model.stopSoundGenerator();
            	model.notifySubscribers();
            	mouseCoordinates=new ArrayList<Coordinate>();
            	}
            	
                state = NOTREADY;                
            }            	
            	model.notifySubscribers();
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
            		//move the viewPort within its bounds
            		//but only if it is a drag allowed in the view port 
            		if (iModel.viewPortX +dx >=0 &&
            				iModel.viewPortY +dy >=0 &&
            				(iModel.viewPortX +iModel.viewPortWidth + dx) <=radarView.width/7 &&
            				(iModel.viewPortY +iModel.viewPortHeight +dy) <=radarView.height/7) {
            		iModel.viewPortX += dx/7;
            		iModel.viewPortY += dy/7;
            		//drag the paths around to make it seem like we are panning the background
            		for (int a = 0; a < model.getModelPaths().size(); a++) {
            			iModel.modelPathsTranslateByCoordinates.get(a).x -=dx;
            			iModel.modelPathsTranslateByCoordinates.get(a).y-=dy;            			
            			model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);
            			model.getModelPaths().get(a).setTranslateY(iModel.modelPathsTranslateByCoordinates.get(a).y);            			
            		}
            		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
            		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
            		  model.notifySubscribers();    		
            		}            		
            	}
            	
            	if (state ==READY) {            		
            		
                	//updatePoints(me.getX(),me.getY());            	
                	//velocities.add(new Coordinate(calculatePointsAverageVelocity(), (double) (System.currentTimeMillis()-velocityTime)/1000));                	
                	
            	//set up relativized view coordinates
            	double viewx = me.getX()/view.width;
            	double viewy = me.getY()/view.height;            	
        		//just for consisitency
        		mouseCoordinates.add(new Coordinate(me.getX()/radarView.width,me.getY()/radarView.height));
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
            //		if (model.pathAngleCalculationCoordinatesUpdateCount>2) {
            //			if (model.currentPathAngle < 90) {
            //				velocities.get(velocities.size()-1).x = 10;
            //			}
            	//	}
            		
            	//	model.updateSoundGeneratorVelocity(soundVelocityThread.getVelocity());
            	//	model.updateSoundGeneratorPanValue(mouseCoordinates.get(mouseCoordinates.size()-1));
            	//	model.updateSoundGeneratorPathAngle();
            	//	time = System.currentTimeMillis();
            		
            			//model.playPathInteractively(soundVelocityThread.getVelocity(), //velocities.get(velocities.size()-1).x
            			//		mouseCoordinates.get(mouseCoordinates.size()-1), model.currentPathAngle, 
            			///		clipDuration);
            			
            			//System.out.println("velocity "+soundVelocityThread.getVelocity());            					
            			
            		
            		
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
	/**
	 * initializes the points for velocity calculation of given stroke
	 * x,y = mousePressed location
	 * 
	 
	public void setPoints(double x, double y) {
		for (int i =0;i<4;i++) {
			points[i] = new Coordinate(x,y);
			points[i].time = System.currentTimeMillis();
		}
	}*/
	/**
	 * update the points to have the 4 previous mouse locations
	 * @param x
	 * @param y
	 
	public void updatePoints(double x, double y) {

		long startTime = System.currentTimeMillis();		
		for (int i =3;i>0;) {
			if (System.currentTimeMillis()-startTime >5) {
				points[i].x = points[i-1].x;
			points[i].y = points[i-1].y;
			points[i].time = System.currentTimeMillis();

				startTime = System.currentTimeMillis();
				i--;
			}			
		}
		while (System.currentTimeMillis()-startTime <5) {
			//wait
		}
		points[0].x = x;
		points[0].y = y;
		points[0].time = System.currentTimeMillis();			
	}*/
	/**
	 * calculate given velocity between 2 coordinates
	 * a should be earlier in time than b
	 * @param a
	 * @param b
	 * @param startTime
	 * @return
	
	public double calculateVelocity(Coordinate a, Coordinate b) {
		double distanceTraveled = Math.abs(Math.sqrt(Math.pow((b.x-a.x), 2)+Math.pow((b.y-a.y), 2)));
		return distanceTraveled/(a.time-b.time);
	} */
	//last 4 points, take the average of the 3 velocities
	//functions: 	
	//double (pixels per ms) calculatePointsAverageVelocity()
	/**
	 * will return the average velocity between the 4 previous mouse locations
	 * @return 
	 
	public double calculatePointsAverageVelocity() {	

		double average =0;
		for (int i =0;i<3;i++) {
			average+=calculateVelocity(points[0], points[i+1]);
		}
		return average/3;
		
	}*/
	public void setIModel(InteractionModel iM) {
		iModel = iM;
	}
	   /**
	    * will calculate the stagger increment for a submitted file 
	    */
	   public double calculateStaggerIncrement(File f) {
		   double clipDuration = model.player.fileLength(f)*1000;
			double clipOverlapDuration = 0.8;//0.865;
			return (clipDuration)-(clipDuration*clipOverlapDuration);			
	   }
	   
	   public void startTask3() {
			state = READ_AND_OBSERVE;
			model.notifySubscribers();
			readAndObserveTrial = new ReadAndObserveStage(me);			
	   }
	   
	   public void startTask2() {		   		   
			state = FREEZE_TEST_TASK;
			model.notifySubscribers();
			
		   model.launchFreezeTestIntructions(me);
		  // FreezeQuiz fr = null;
		  // state = FREEZE;
			//view.paintOverPaths();
		//	radarView.paintOverPaths();
			//iModel.freezeTestOn();
			//launch a pop up window requesting for the location of the other user and his activity?
			//need to pass in the location of other user's viewPort
			//fr = new FreezeQuiz(me, radarView.calculateNetViewPortCenter());	
	   }
}
