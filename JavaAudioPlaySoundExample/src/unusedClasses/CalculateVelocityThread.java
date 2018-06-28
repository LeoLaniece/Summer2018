package unusedClasses;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import test.Coordinate;
import test.Draw2View;

public class CalculateVelocityThread extends Thread{
	   private Thread t;
	   private String threadName;
	   public Coordinate[] points;
	   private Draw2View view;
	   public ArrayList<Double> velocities;
	   boolean mouseReleased = false;
	   
	   CalculateVelocityThread(String name, double x, double y, Draw2View v) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	      points = new Coordinate[4];
	      velocities = new ArrayList<>();
	     setPoints(x,y);
	     view =v;
	   }
	   
	   public void run() {
		   long startTime = System.currentTimeMillis();
		   
			view.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent me) {  
		        	while (!mouseReleased) {		        
		        		//try to fix this thread(use inturruptions?
		        		System.out.println("mousePressed");
		        		if (System.currentTimeMillis()-startTime > 100) {
		        			updatePoints(me.getX(),me.getY());
		        		velocities.add(calculatePointsAverageVelocity());
		        		
		        		}
		        		
		        		
		        		view.c.setOnMouseReleased(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent me) {  
	            	mouseReleased = true;
	            	System.out.println("mouseReleased");
	            }
			});
		        		
		        	}
		        }
		   });
			
			
		   
	   }
	   
	   public void start () {
	      System.out.println("Starting " +  threadName );
	      
	      if (t == null) {
	         t = new Thread (this, threadName);
	         System.out.println("Starting " +  threadName );
	         t.start();
	      }
	   }
	   
		//velocity calculation functions
		//need to test these
		/**
		 * initializes the points for velocity calculation of given stroke
		 * x,y = mousePressed location
		 * 
		 */
		public void setPoints(double x, double y) {
			for (int i =0;i<4;i++) {
				points[i] = new Coordinate(x,y);
				points[i].time = System.currentTimeMillis();
			}
		}
		/**
		 * update the points to have the 4 previous mouse locations
		 * @param x
		 * @param y
		 */
		public void updatePoints(double x, double y) {

			long startTime = System.currentTimeMillis();		
			for (int i =3;i>0;) {
				if (System.currentTimeMillis()-startTime >10) {
					points[i].x = points[i-1].x;
				points[i].y = points[i-1].y;
				points[i].time = System.currentTimeMillis();

					startTime = System.currentTimeMillis();
					i--;
				}			
			}
			while (System.currentTimeMillis()-startTime <10) {
				//wait
			}
			points[0].x = x;
			points[0].y = y;
			points[0].time = System.currentTimeMillis();

				

		}
		/**
		 * calculate given velocity between 2 coordinates
		 * a should be earlier in time than b
		 * @param a
		 * @param b
		 * @param startTime
		 * @return
		 */
		public double calculateVelocity(Coordinate a, Coordinate b) {
			double distanceTraveled = Math.abs(Math.sqrt(Math.pow((b.x-a.x), 2)+Math.pow((b.y-a.y), 2)));
			return distanceTraveled/(a.time-b.time);
		}
		//last 4 points, take the average of the 3 velocities
		//functions: 	
		//double (pixels per ms) calculatePointsAverageVelocity()
		/**
		 * will return the average velocity between the 4 previous mouse locations
		 * @return 
		 */
		public double calculatePointsAverageVelocity() {		
			double average =0;
			for (int i =0;i<3;i++) {
				average+=calculateVelocity(points[0], points[i+1]);
			}
			return average/3;
			
		}
	   
	   
}
