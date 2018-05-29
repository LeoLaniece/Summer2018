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
	   
	   StaggeredSoundThread(String name, Grain2Files p, double strokeDuration) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	      player = p;
	      this.strokeDuration = strokeDuration;
	   }
	   
	   public void run() {
			File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");						
			double duration = strokeDuration/1000;			
			double clipDuration = player.fileLength(f1);
			int howManyClips = (int) (duration/clipDuration);
			if (howManyClips ==0) {
				howManyClips = 1;
			}
			//howManyClips =1;
			System.out.println("howManyStrokes "+howManyClips);
			//set up clip
			try {
			for (int i =0;i<howManyClips;i++) {
				    AStaggeredThread t = new AStaggeredThread("A Stagered sound", player, strokeDuration);
				    t.start();				    
					sleep((long) ((clipDuration*1000)-60));					
			}
			} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            
            
            
						
	   }
	
	
	
	

}
