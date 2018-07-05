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

	}
	
	@Override
	public void run() {
		while ((System.currentTimeMillis() - startTime) < 60001) {
			timer.setText(Long.toString(60-(System.currentTimeMillis()-startTime)/1000));
		}
	      //log all the info before you close
	      testStage.calculateAwareness();	 
	      testStage.closeStage();
	}

}
