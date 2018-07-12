package test;

import javafx.stage.Stage;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.concurrent.Task; 
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class FreezeTestTimer extends Thread{
	//Text timer;
	long startTime;	
	public boolean done = false;
	private Draw2Controller controller;
	public boolean inloop = true;
	public FreezeTestInstructions stage;
	public int timeIncrement = 10000;
	public FreezeTestTimer me = this;
	public FreezeQuiz fr = null;
	
	public FreezeTestTimer(Draw2Controller con, FreezeTestInstructions stage) {
		//this.timer = timer;
		this.startTime = System.currentTimeMillis();	
		controller = con;		
		this.stage = stage;
		if (controller.iModel.task == controller.iModel.LOCATION_IDENTIFICATION_TASK
				|| controller.iModel.task == controller.iModel.TOOL_IDENTIFICATION_TASK
				|| controller.iModel.task == controller.iModel.SHAPE_DETECTION_TASK) {
			timeIncrement = 10000;
		}
		if (controller.iModel.task == controller.iModel.TOOL_REACTION_TASK) {
			timeIncrement = Integer.MAX_VALUE;
		}
		//this.start();
		
	}
	
	@Override
	public void run() {		
		
		while ((System.currentTimeMillis() - startTime) < 59999) {
			//timer.setText((Long.toString(60-(System.currentTimeMillis()-startTime)/1000)));			
			if ((System.currentTimeMillis() - startTime) != 0 && (System.currentTimeMillis() - startTime)%timeIncrement == 0 ) {
				startTime-=10;
				//   controller.state = controller.FREEZE;
				//   controller.view.paintOverPaths();
				//   controller.radarView.paintOverPaths();
				//   controller.iModel.freezeTestOn();
					//launch a pop up window requesting for the location of the other user and his activity?
					//need to pass in the location of other user's viewPort
					Platform.runLater(new Runnable() {
					    @Override
					        public void run() {	
					    	if ((System.currentTimeMillis() - startTime)> timeIncrement) {					    		
					    	fr = new FreezeQuiz(controller, controller.radarView.getLastKnownCoordinate(), me);					    	
					    	}					    	
					    }
					});
					if (!(controller.iModel.task == controller.iModel.LOCATION_IDENTIFICATION_TASK)
							&&!(controller.iModel.task == controller.iModel.TOOL_IDENTIFICATION_TASK)) {
						controller.state = controller.PROMPT_FOR_SHAPE;							
					}else {
						controller.state = controller.PAUSE_UNTIL_QUIZ_COMPLETE;
					}
					controller.model.notifySubscribers();
			}
		}
	      stage.closeStage();
	      
	      //now close freezetestinstructions and show the task selection menu again.

	}
}
