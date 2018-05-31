package test;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import sun.audio.*;

import java.io.*;

import javax.management.timer.Timer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StaggeredSoundThread extends Thread{
	   private Thread t;
	   private String threadName;
	   public Grain2Files player;
	   public double strokeDuration;
	   public ArrayList<Coordinate> velocities;
	   public ArrayList<Float> panValues;
	   
	   
	   StaggeredSoundThread(String name, Grain2Files p, double strokeDuration, ArrayList<Coordinate> velocities, ArrayList<Float> panValues) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	      player = p;
	      this.strokeDuration = strokeDuration;
	      this.velocities =velocities;
	      this.panValues =panValues;
	   }
	   
	   
	   //next, implement panning and the corners
	   
	   
	   public void run() {
		   fixVelocityCount();
			File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");		
			File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
			double duration = strokeDuration;			
			double clipDuration = player.fileLength(f1)*1000;
			double file2ClipDuration = player.fileLength(f2)*1000;
			double clipOverlapDuration = 0.8;//0.865;
			double clipOverlapIncrement = 1-clipOverlapDuration;
			
			double clipStart = (clipDuration)-(clipDuration*clipOverlapDuration);
			double clipEnd = (clipDuration)-(clipDuration*clipOverlapIncrement);	
			
			int howManyClips = (int) ((duration-clipEnd)/clipStart);
			if (howManyClips ==0) {
				howManyClips = 1;
			}
						
			double staggerIncrementForFile1 = calculateStaggerIncrement(f1);
			double staggerIncrementForFile2 = calculateStaggerIncrement(f2);
			
			//set up clip
			try {
				//percentages of each file
				int gate =0;
				int file1Count =0;
				int totalCount =0;
				float maxVolume = 0.2f;
				int velocityIncrement = velocities.size()/howManyClips;

				int velocityIndex =0;
				velocities.get(0).x =1;
				
				if (panValues.size() ==0) {
					panValues.add(0f);
				}
				int panValueIndex =0;
				int panValueIncrement = panValues.size()/howManyClips;
				Float panValue = 0f;
				
			for (int i =0;i<howManyClips;i++) {
				//get max volume to equal a quadratic curve from ratio of velocity between 10-0 and 1.0-0.0
				//ask a math person how to do this?
				maxVolume = (float) (1 *(velocities.get(velocityIndex).x));
				System.out.println("velocity "+velocities.get(velocityIndex).x);
				System.out.println("maxVol "+maxVolume);
				velocityIndex+=velocityIncrement;
				
				panValue = panValues.get(panValueIndex);
				 panValueIndex += panValueIncrement;
				
				if (gate ==0) {
					AStaggeredThread t = new AStaggeredThread("A Stagered sound", player, clipDuration, f1, maxVolume,velocities, panValue);
				    t.start();				    
					sleep((long) staggerIncrementForFile1 );	
				}else {
					AStaggeredThread t = new AStaggeredThread("A Stagered sound", player, file2ClipDuration, f2, maxVolume,velocities, panValue);
				    t.start();				    
					sleep((long) staggerIncrementForFile2);	
				}
				totalCount+=1;
				
				//percentage of mixing really doesn't do much
				//but ill leave it here for now.
	              if (file1Count/totalCount <= 1) {//mixPercentages.get(mixPercentagesIncrement)) {
	            	  gate = 0;
	              }else {
	           	   gate = 1;
	              }	              
	              if (gate == 0) {
	            	  file1Count += 1;            	   
	              }								
			}												
			} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                                    						
	   }
	
	   /**
	    * will calculate the stagger increment for a submited file 
	    */
	   public double calculateStaggerIncrement(File f) {
		   double clipDuration = player.fileLength(f)*1000;
			double clipOverlapDuration = 0.865;//0.8;
			return (clipDuration)-(clipDuration*clipOverlapDuration);			
	   }
	   
	   public void fixVelocityCount() {
		   ArrayList<Coordinate> v = new ArrayList<>();
		   //for each velocity, copy, if x is 0, divide y by 0.043 and add that many x= 0's
		   for (int i =0; i< velocities.size();i++) {
			   if (velocities.get(i).x ==0) {
				   int addTo = (int) (velocities.get(i).y/0.043);
				   for (int a =0; a<addTo;a++) {
					   v.add(new Coordinate(0,0.043));
				   }
			   }else {
				   v.add(new Coordinate(velocities.get(i).x,velocities.get(i).y));
			   }			   			   
		   }
		   
		   velocities = v;
	   }
	
	
	

}
