package test;

import javafx.stage.Stage;
import java.util.concurrent.FutureTask;
import javafx.concurrent.Task; 
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class UpdateTimer extends Thread{
	Text timer;
	long startTime;
	ReadAndObserveStage testStage;
	public boolean done = false;
	
	public UpdateTimer(long startTime, Text timer, ReadAndObserveStage testStage) {
		this.timer = timer;
		this.startTime = startTime;		
		this.testStage = testStage;
	/*	 setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	            @Override
	            public void handle(WorkerStateEvent event) {
	                if (testStage != null) {
	                    testStage.hide();
	                }
	            }
	        });*/

	}
	
	@Override
	public void run() {
		while ((System.currentTimeMillis() - startTime) < 30001) {
			timer.setText(Long.toString((System.currentTimeMillis()-startTime)/1000));
		}
	      timer.setFont(Font.font ("Verdana", 0));
	      timer.setFill(Color.RED); 
	      timer.setText("time is up!!");
	      //log all the info before you close
	      testStage.calculateAwareness();	 
	      testStage.closeStage();
	     // done = true;
	}

/*	@Override
	protected Object call() throws Exception {
		run();
		return null;
	}*/
}
