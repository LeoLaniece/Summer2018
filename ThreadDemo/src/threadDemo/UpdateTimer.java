package threadDemo;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UpdateTimer extends Thread{
	Text timer;
	long startTime;
	
	public UpdateTimer(long startTime, Text timer) {
		this.timer = timer;
		this.startTime = startTime;		
	}
	
	@Override
	public void run() {
		while ((System.currentTimeMillis() - startTime) < 6001) {
			timer.setText(Long.toString((System.currentTimeMillis()-startTime)/1000));
		}
	      timer.setFont(Font.font ("Verdana", 20));
	      timer.setFill(Color.RED); 
	      timer.setText("times up!!");
	}
}
