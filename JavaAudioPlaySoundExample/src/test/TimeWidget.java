package test;


import javafx.stage.Stage;
import java.util.concurrent.FutureTask;
import javafx.concurrent.Task; 
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class TimeWidget extends Thread{
	Text timer;
	long startTime;
	ReadAndObserveInstructionStage testStage;
	public boolean done = false;
	
	public TimeWidget(long startTime, Text timer, ReadAndObserveInstructionStage testStage) {
		this.timer = timer;
		this.startTime = startTime;		
		this.testStage = testStage;

	}
	
	@Override
	public void run() {
		while ((System.currentTimeMillis() - startTime) < 30001) {
			timer.setText(Long.toString(30-(System.currentTimeMillis()-startTime)/1000));
		}
			
	      //log all the info before you close
	      //testStage.restoreInstructions();	 	     
	     // done = true;
	}

/*	@Override
	protected Object call() throws Exception {
		run();
		return null;
	}*/
}
