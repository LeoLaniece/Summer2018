package threadDemo;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ThreadDemo extends Thread {
	   private Thread t;
	   private String threadName;
	   public GraphicsContext gc;
	   public Canvas c;
	   
	   ThreadDemo(String name, GraphicsContext gc, Canvas c) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	      this.gc = gc;
	      this.c= c;
	   }
	   
	   public void run() {
	      System.out.println("Running " +  threadName );
	      //what i do here will alternate with the other thread 
		  //as long as there is sleep time.
		 //for(int i = 4; i > 0; i--) {
			 //try getting this to pick up on a mouse event
			 //how to put this into drawing app
			 //make your main thread run on the controller
			 //start a new thread to play sounds when nessesary.
			 
			 c.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
			        @Override
			        public void handle(MouseEvent me) {
			        	gc.setStroke(Color.BLACK);
			        	gc.strokeRect(me.getX(), me.getY(), 100, 100);
			        	//also play sound. 
			        	ThreadDemo2 T2 = new ThreadDemo2("Thread-2");
			  	        T2.start();
			        }
			        });
			 

			 

			 
		    System.out.println("Thread: " + threadName + ", " );
		    // Let the thread sleep for a while.
		    //Thread.sleep(0);
	//	 }
	      System.out.println("Thread " +  threadName + " exiting.");
	   }
	   
	   public void start () {
	      System.out.println("Starting " +  threadName );
	      
	      if (t == null) {
	         t = new Thread (this, threadName);
	         System.out.println("Starting " +  threadName );
	         t.start();
	      }
	   }
}
	
