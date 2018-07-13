package test;

import java.io.File;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javax.sound.sampled.LineUnavailableException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent; 
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Controller in MVC architecture for the drawing application
 * Handles mouse and key events
 * Passes on events triggered from the task selection menu
 * @author HCI Lab
 *
 */
public class Draw2Controller {
	/**
	 * Reference to current class, to use in encapsulated statements
	 */
	public final Draw2Controller me = this;
	/**
	 * a reference to the View of the drawing application
	 */
	public Draw2View view;
	/**
	 * a reference to the Model of the drawing application
	 */
	public Draw2Model model;
	/**
	 * a reference to the RadarView of the drawing application
	 */
	public Draw2miniMap radarView;
	/**
	 * Variable to store Mouse x event locations when necessary
	 */
	double x;
	/**
	 * Variable to store Mouse y event locations when necessary
	 */
	double y;            	
	/**
	 * Variable to store Mouse x event locations when necessary
	 */
	double dx = 0;
	/**
	 * Variable to store Mouse x event locations when necessary
	 */
	double dy = 0;
	/**
	 * a reference to the Interaction Model of the drawing application
	 */
	InteractionModel iModel;
	/**
	 * ArrayList of mouse coordinates used to draw paths and generate sounds
	 */
	public ArrayList<Coordinate> mouseCoordinates;
	
	//superStates		
	/**
	 * superState which indicates that any sound information should be sent over a network
	 */
	public int SOUNDS_OVER_NETWORK = 0;
	/**
	 * superState which indicates that any sound information should be sent to local sound generators
	 */
	public int SOUNDS_LOCAL = 1;
	/**
	 * Variable which indicates the current superState of this drawing application
	 */
	public int superState = SOUNDS_OVER_NETWORK;
				
	//STATES
	/**
	 * state which indicates the user is able to draw paths on the canvas
	 */
	public int READY = 0;	
	/**
	 * state which indicates the user is able to displace their location on the canvas
	 */
	public int PAN_READY = 1;
	/**
	 * state which indicates the user is not able to draw paths on the canvas
	 */
	public int NOTREADY =-1;
	/**
	 * state which indicates the user is currently executing a Read and Observe study task (used to inform network)
	 */
	public int READ_AND_OBSERVE = 3;
	/**
	 * state which indicates the user has completed a task (used to inform network)
	 */
	public int CLOSE_INSTRUCTIONS = 4;
	/**
	 * state which indicates the user is currently executing a Freeze test study task (used to inform network)
	 */
	public int FREEZE_TEST_TASK =5;
	/**
	 * state which indicates the user is currently executing a Freeze test questionnaire a
	 * and that their partner also need to complete a questionnaire (used to inform network)
	 */
	public int PROMPT_FOR_SHAPE = 6;
	/**
	 * state which indicates the user is has completed a Freeze test questionnaire
	 * and that their partner's questionnaire should close (used to inform network)
	 */
	public int CLOSE_PROMPT_FOR_SHAPE = 9;
	/**
	 * state which indicates the user has finished reading the instructions for the current task
	 * and that their partner's instructions should close (used to inform network)
	 */
	public int READY_TO_BEGIN_TASK = 7;
	/**
	 * state which indicates the user has selected a drawing timbre
	 * and that their partner's should play the appropriate impact clip (used to inform network)
	 */
	public int PLAY_IMPACT = 8;
	/**
	 * state which indicates the user is completing a Freeze test questionnaire
	 * and that their partner should wait before persuing their task (used to inform network)
	 */
	public int PAUSE_UNTIL_QUIZ_COMPLETE = 10;
	/**
	 * Variable which indicates the current state of this drawing application
	 */
	public int state = NOTREADY;
	/**
	 * Variable which indicates the thread which is constantly calculating the mouse velocity
	 * Call the getVelocity() from this thread to get the current mouse velocity
	 */
	public MouseTest soundVelocityThread;
	/**
	 * A variable used to record how long it has taken the user to press spacebar since the beginning of this
	 * drawing application
	 */
	public long startTime;
	/**
	 * 
	 */
	public ReadAndObserveStage readAndObserveTrial =null;
	public boolean taskRunning =false;
	public boolean drawViewPort = true;
	
	public Draw2Controller(Draw2View v, Draw2Model m, Draw2miniMap r) throws InterruptedException 
{
		view = v;
		model = m;
		radarView = r;
		//timeOfChange = new ArrayList<>();
		//points = new Coordinate[4];
		 mouseCoordinates=new ArrayList<Coordinate>();
		 File f1 = new File(new File("src\\soundAndImageFiles\\pencilSlow.WAV").getAbsolutePath());
     	soundVelocityThread = new MouseTest();
     	soundVelocityThread.start();
     	startTime = System.currentTimeMillis();
		//setPoints();
     	 model.startTaskStage(me);
     	
     	view.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {	
				//for logging purposes
				if (key.getCode()==KeyCode.SPACE) {
					String hash = "Time for completion "+Long.toString(((System.currentTimeMillis() -startTime)/1000));
					// CreateFile x = new	CreateFile(hash);														
				}
				//for the read and observe task
				if (key.getText().equals("r")) {					
					state = READ_AND_OBSERVE;
					model.notifySubscribers();
					//readAndObserveTrial = new ReadAndObserveStage(me);					
				}
				
				//pan the minimap if there is asdw key press
				//and if we are in READANDOBSERVE state
				if (state == READ_AND_OBSERVE) {
				if (key.getText().equals("s")) {
					System.out.println("key s pressed!");
            		dy = 3;           		            		           		            		        		            		
            		//move the viewPort within its bounds
            		//but only if it is a drag allowed in the view port 
            		if (iModel.viewPortY +dy >=0 &&            				
            				(iModel.viewPortY +iModel.viewPortHeight +dy) <=radarView.height/7) {            		
            		iModel.viewPortY += dy/7;
            		//drag the paths around to make it seem like we are panning the background
            		for (int a = 0; a < model.getModelPaths().size(); a++) {            			
            			iModel.modelPathsTranslateByCoordinates.get(a).y-=dy;            			
            			//model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);
            			model.getModelPaths().get(a).setTranslateY(iModel.modelPathsTranslateByCoordinates.get(a).y);            			
            		}
            		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
            		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
            		  model.notifySubscribers();    		
            		}  
				}
				if (key.getText().equals("w")) {
					System.out.println("key w pressed!");
            		dy = -3;           		            		           		            		        		            		
            		//move the viewPort within its bounds
            		//but only if it is a drag allowed in the view port 
            		if (iModel.viewPortY +dy >=0 &&            				
            				(iModel.viewPortY +iModel.viewPortHeight +dy) <=radarView.height/7) {            		
            		iModel.viewPortY += dy/7;
            		//drag the paths around to make it seem like we are panning the background
            		for (int a = 0; a < model.getModelPaths().size(); a++) {            			
            			iModel.modelPathsTranslateByCoordinates.get(a).y-=dy;            			
            			//model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);
            			model.getModelPaths().get(a).setTranslateY(iModel.modelPathsTranslateByCoordinates.get(a).y);            			
            		}
            		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
            		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
            		  model.notifySubscribers();    		
            		} 
				}
				if (key.getText().equals("d")) {
					System.out.println("key d pressed!");
					dx = 3;            		            		           		            		        		            		
            		//move the viewPort within its bounds
            		//but only if it is a drag allowed in the view port 
            		if (iModel.viewPortX +dx >=0 &&            				
            				(iModel.viewPortX +iModel.viewPortWidth + dx) <=radarView.width/7) 
            				 {
            		iModel.viewPortX += dx/7;            		
            		//drag the paths around to make it seem like we are panning the background
            		for (int a = 0; a < model.getModelPaths().size(); a++) {
            			iModel.modelPathsTranslateByCoordinates.get(a).x -=dx;            			           			
            			model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);            			           			
            		}
            		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
            		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
            		  model.notifySubscribers();    		
            		}            		
				}
				if (key.getText().equals("a")) {
					System.out.println("key a pressed!");
					dx = -3;            		            		           		            		        		            		
            		//move the viewPort within its bounds
            		//but only if it is a drag allowed in the view port 
            		if (iModel.viewPortX +dx >=0 &&            				
            				(iModel.viewPortX +iModel.viewPortWidth + dx) <=radarView.width/7) 
            				 {
            		iModel.viewPortX += dx/7;            		
            		//drag the paths around to make it seem like we are panning the background
            		for (int a = 0; a < model.getModelPaths().size(); a++) {
            			iModel.modelPathsTranslateByCoordinates.get(a).x -=dx;            			           			
            			model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);            			           			
            		}
            		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
            		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
            		  model.notifySubscribers();    		
            		}            		
				}
				}
			}     		
     	});

	view.c.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {        	
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

        	if (superState == SOUNDS_LOCAL) {
			model.playPathInteractively(0,//soundVelocityThread.getVelocity(),//
					mouseCoordinates.get(mouseCoordinates.size()-1)  
					);
        	}
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
            	
    
            		
                    	
            	model.pathToNull();
            	if (superState == SOUNDS_LOCAL) {
            		model.stopSoundGenerator();
            	}
            	
            	model.notifySubscribers();
            	mouseCoordinates=new ArrayList<Coordinate>();
            	}
            	
                state = NOTREADY;                
                        	
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

                if (view.c.getBoundsInLocal().contains(me.getX()
                		//+model.path.getStrokeWidth()
                		, me.getY()
                		//+model.path.getStrokeWidth()
                		)) {
                	
                	//create new path in the controller
                	
                	model.strokePath(me.getX(), me.getY());                	
            //		if (model.pathAngleCalculationCoordinatesUpdateCount>2) {
            //			if (model.currentPathAngle < 90) {
            //				velocities.get(velocities.size()-1).x = 10;
            //			}
            	//	}
                	if (superState == SOUNDS_LOCAL) {
            		model.updateSoundGeneratorVelocity(soundVelocityThread.getVelocity());
            		model.updateSoundGeneratorPanValue(mouseCoordinates.get(mouseCoordinates.size()-1));
                	}
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
	   
	   public void startTask2() {
			state = READ_AND_OBSERVE;
			taskRunning = true;
			//view.foc
			model.notifySubscribers();
			//send in iModel, model and radarView
			readAndObserveTrial = new ReadAndObserveStage(me, model, iModel, radarView);			
	   }
	   
	   public void startTask1() {		   		   
			state = FREEZE_TEST_TASK;
			iModel.task = iModel.REAL_FREEZE_TEST;
			taskRunning = true;
			model.notifySubscribers();			
		   model.launchFreezeTestIntructions(me);
	   }
	   /**
	    * will begin a location identification task
	    * User 2 will be prompted to locate User 1 every 15 seconds
	    * User 1 will just be tracing the shapes in the workspace
	    * User 2 will just observe the workspace
	    */
	   public void startTask0() {
		   //launch a partial version of FreezeTestTask with a different timer
		   //different instructions for User 1
		   iModel.task = iModel.LOCATION_IDENTIFICATION_TASK;
		   state = FREEZE_TEST_TASK;
			taskRunning = true;
			model.notifySubscribers();			
		   model.launchFreezeTestIntructions(me);		   
	   }
	   /**
	    * will begin a tool identification task
	    * User 2 will be prompted to identify which tool was previously in use by User 1 every 15 seconds
	    * User 1 will just be tracing the shapes in the workspace, but! Circles = eraser, square = pencil, triangle = metal, squiggle = chalk
	    * User 2 will just observe the workspace
	    */
	   public void startTask4() {
		   //launch a partial version of FreezeTestTask with a different timer
		   //different instructions for User 1
		   iModel.task = iModel.TOOL_IDENTIFICATION_TASK;
		   state = FREEZE_TEST_TASK;
			taskRunning = true;
			model.notifySubscribers();			
		   model.launchFreezeTestIntructions(me);		   
	   }
	   /**
	    * will begin an activity identification task
	    * User 2 is tasked to hold down buttons based on the other user's activity
	    * User 2 makes use of the 'drawing' and 'panning' buttons
	    * User 1 will just be tracing the shapes in the workspace, Circles = eraser, square = pencil, triangle = metal, squiggle = chalk
	    * User 2 will observe the workspace while pressing the buttons
	    */
	   public void startTask5() {
		   //launch a partial version of FreezeTestTask with a different timer
		   //different instructions for User 1
		   iModel.task = iModel.ACTIVITY_IDENTIFICATION_TASK;
		   state = READ_AND_OBSERVE;
			taskRunning = true;
			model.notifySubscribers();			
			readAndObserveTrial = new ReadAndObserveStage(me, model, iModel, radarView);			   
	   }
	   /**
	    * will begin a shape identification task
	    * User 2 will be prompted to identify which shape was previously drawn by User 1 every 15 seconds
	    * User 1 will just be tracing the shapes in the workspace, but! Circles = eraser, square = pencil, triangle = metal, squiggle = chalk
	    * User 1 will also be asked which shape he has completed tracing every 15 seconds
	    * User 2 will just observe the workspace
	    */
	   public void startTask6() {
		   //launch a partial version of FreezeTestTask with a different timer
		   //different instructions for User 1
		   iModel.task = iModel.SHAPE_DETECTION_TASK;
		   state = FREEZE_TEST_TASK;
			taskRunning = true;
			model.notifySubscribers();			
		   model.launchFreezeTestIntructions(me);		   
	   }
	   /**
	    * will begin a tool identification task
	    * Both user's will be tracing the shapes in the workspace 
	    * User 2 will only switch tool when they notice User 1 switch tool
	    */
	   public void startTask7() {
		   //launch a partial version of FreezeTestTask with a different timer interval
		   //different instructions for User 1
		   iModel.task = iModel.TOOL_REACTION_TASK;
		   state = FREEZE_TEST_TASK;
			taskRunning = true;
			model.notifySubscribers();			
		   model.launchFreezeTestIntructions(me);		   
	   }
	   
}
