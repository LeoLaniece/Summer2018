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
	public int timeIncrement = 30000;
	
	public FreezeTestTimer(long startTime, Draw2Controller con, FreezeTestInstructions stage) {
		//this.timer = timer;
		this.startTime = startTime;	
		controller = con;		
		this.stage = stage;
		if (controller.iModel.task == controller.iModel.LOCATION_IDENTIFICATION_TASK
				|| controller.iModel.task == controller.iModel.TOOL_IDENTIFICATION_TASK) {
			timeIncrement = 15000;
		}
	}
	
	@Override
	public void run() {
		while ((System.currentTimeMillis() - startTime) < 59999) {
			//timer.setText((Long.toString(60-(System.currentTimeMillis()-startTime)/1000)));
			if ((System.currentTimeMillis() - startTime)%timeIncrement == 0 ) {
				//System.out.println("modulus 10000!!");
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
					    	FreezeQuiz fr ;	
					    	fr = new FreezeQuiz(controller, controller.radarView.calculateNetViewPortCenter());
					    	}					    	
					    }
					});
					if (!(controller.iModel.task == controller.iModel.LOCATION_IDENTIFICATION_TASK)
							&&!(controller.iModel.task == controller.iModel.TOOL_IDENTIFICATION_TASK)) {
						controller.state = controller.PROMPT_FOR_SHAPE;	
						controller.model.notifySubscribers();
					}					
			}
		}
	      stage.closeStage();
	      
	      //now close freezetestinstructions and show the task selection menu again.

	}
}
