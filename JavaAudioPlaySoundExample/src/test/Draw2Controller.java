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
	 * Variable which indicate if this drawing application is running a task or not
	 */	
	public boolean taskRunning =false;
	/**
	 * Variable which indicates if this drawing application should display the RadarView for 
	 * the given task
	 */
	public boolean drawViewPort = true;
	
	/**
	 * Creates a controller entity for the drawing application
	 * Handles mouse and key events 
	 * Communicates with the model
	 * Also acts as a place holder for the current state of the drawing application
	 * @param v The drawing application view
	 * @param m The drawing application model
	 * @param r the drawing application radarView
	 * @throws InterruptedException
	 */
	public Draw2Controller(Draw2View v, Draw2Model m, Draw2miniMap r) {
		//initialize necessary variables
		view = v;
		model = m;
		radarView = r;
		 mouseCoordinates=new ArrayList<Coordinate>();
		 File f1 = new File(new File("src\\soundAndImageFiles\\pencilSlow.WAV").getAbsolutePath());
		 //begin thread to calculate the mouse velocity
		 //used to inform the sounds generated
     	soundVelocityThread = new MouseTest();
     	soundVelocityThread.start();
     	startTime = System.currentTimeMillis();
     	//launch the task selection menu 
     	 model.startTaskStage(me);     	
     	 
     	view.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {	
				//for logging purposes
				if (key.getCode()==KeyCode.SPACE) {
					String hash = "Time for completion "+Long.toString(((System.currentTimeMillis() -startTime)/1000));
					// CreateFile x = new	CreateFile(hash);														
				}		
			}     		
     	});
     	//draw paths if state is ready, otherwise prepare to pan the viewPort
	view.c.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {        	
        	state = READY;
        	model.pathToNull();  	        	
        	//if shift is down, the user wants to pan the viewPort
        	if (me.isShiftDown()) {
        		//pan the canvas
        		//new state
        		state = PAN_READY;   
            	x = me.getX();
            	y = me.getY();        		
         		//adjust the server's minimap location
           	    if (state == PAN_READY) {
           		 //START the ViewPortDisplacementSound generator here
           	     if (model.localVPDS == null) {
               		 model.beginLocalViewPortMovementSound();
               	 }
           	  }       	         		
        	}
        	if (state ==READY) {
        	//will be useful in mouseDragged for velocity
        	x = me.getX();
        	y = me.getY();       	         	
        	//begin drawing path and add it to the model database
        	model.startPath(me.getX(),me.getY());      	        	
        	//Mark the location of the path relative to the workspace for sound production 
        	mouseCoordinates.add(new Coordinate(me.getX()/radarView.width,me.getY()/radarView.height));
        	//if the state is SOUNDS_LOCAL paly the path sounds locally.
        	if (superState == SOUNDS_LOCAL) {
			model.playPathInteractively(0,mouseCoordinates.get(mouseCoordinates.size()-1));
        	}
        	}        	        	            		                        
        }
    });
	
    //when the mouse is released stop drawing the path, panning the viewPort and producing sounds
	view.c.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) { 
            	if (state == PAN_READY) {
        		//stop the VPDS generator
              	 if (model.localVPDS != null) {
              		 model.stopLocalVPDS();
              	 }
            	}
            	if (state == READY) {   
            	//stop drawing the paths
            	model.pathToNull();
            	//stop the sounds for local generatiom
            	if (superState == SOUNDS_LOCAL) {
            		model.stopSoundGenerator();
            	}            	
            	model.notifySubscribers();
            	//reset the mouse coordinates for later use
            	mouseCoordinates=new ArrayList<Coordinate>();
            	}            	
                state = NOTREADY;                                        	
            	model.notifySubscribers();
            }
        });		
      
	//When the mouse is dragged update paths, update sound generators and pan the viewPort
	view.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) { 
            	//displace the viewPort, all modelPaths and the background image by 1 pixel
            	if (state == PAN_READY) {            		
            		dx =me.getX()-x;
            		dy = me.getY()-y;
            		x = me.getX();
            		y = me.getY();
            		//move the viewPort 
            		//but only if it is a drag allowed in the radarView bounds 
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
            		  model.notifySubscribers();    		
            		}
                   	if (state == PAN_READY && model.localVPDS!=null) {
                 		 //update sound if the view port sound is already happening
                 		 model.updateLocalVPDSGeneratorLocation(model.radarView.calculateViewPortCenter());
                 	 }
            	}
            	//update the path drawing and sound generator
            	if (state ==READY) {            		            	
            	//record path location for the sound generator
        		mouseCoordinates.add(new Coordinate(me.getX()/radarView.width,me.getY()/radarView.height));            	
        		//only draw paths on the canvas
                if (view.c.getBoundsInLocal().contains(me.getX(), me.getY())) { 
                	//update current path
                	model.strokePath(me.getX(), me.getY());  
                	//if superStae is SOUNDS_LOCAL update the local sound generator
                	if (superState == SOUNDS_LOCAL) {
            		model.updateSoundGeneratorVelocity(soundVelocityThread.getVelocity());
            		model.updateSoundGeneratorPanValue(mouseCoordinates.get(mouseCoordinates.size()-1));
                	}            	
                }
            	}
                }
        });		

	}
	/**
	 * Will set a reference to the application InteractionModel in the controller
	 * @param iM application interactionModel
	 */
	public void setIModel(InteractionModel iM) {
		iModel = iM;
	}
	/**
	 * Will begin executing the Read and Observe study task
	 * will show instructions for the task
	 * 
	 * this task will ask one user to read a short article while maintaining basic awareness of their partner
	 * If the said user notices their partner is active in the workspace, they must hold down a button
	 * the task will last 60 seconds
	 */
	   public void startTask2() {
			state = READ_AND_OBSERVE;
			taskRunning = true;			
			model.notifySubscribers();
			//launch instruction stage
			model.readAndObserveTrial = new ReadAndObserveStage(me, model, iModel, radarView);			
	   }
	   /**
	    * Will begin executing the Freeze test study task
	    * Will show instructions for the task
	    * 
	    * This task will ask both users to trace all the shapes they can find in the workspace together
	    * Every 20 seconds a pop up quiz will interrupt the task
	    * The users must complete the quiz before continuing the task
	    */
	   public void startTask1() {		   		   
			state = FREEZE_TEST_TASK;
			iModel.task = iModel.REAL_FREEZE_TEST;
			taskRunning = true;
			model.notifySubscribers();	
			//launch instructions
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
		   //and different instructions for both users
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
		   //and different instructions for both users
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
		   //and different instructions for both users
		   iModel.task = iModel.ACTIVITY_IDENTIFICATION_TASK;
		   state = READ_AND_OBSERVE;
			taskRunning = true;
			model.notifySubscribers();	
			//launch instructions for the task
			model.readAndObserveTrial = new ReadAndObserveStage(me, model, iModel, radarView);			   
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
		 //and different instructions for both users
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
		 //and different instructions for both users
		   iModel.task = iModel.TOOL_REACTION_TASK;
		   state = FREEZE_TEST_TASK;
			taskRunning = true;
			model.notifySubscribers();			
		   model.launchFreezeTestIntructions(me);		   
	   }	   
}
