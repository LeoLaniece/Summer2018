package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundThread extends Thread{
	   private Thread t;
	   private String threadName;
	   public Grain2Files player;
	   public double strokeDuration;
	   public ArrayList<Coordinate> velocities;
	   public ArrayList<Float> panValues;
	   
	   SoundThread(String name, Grain2Files p, double strokeDuration, ArrayList<Coordinate> velocities, ArrayList<Float> panValues) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	      player = p;
	      this.strokeDuration = strokeDuration;
	      this.velocities = velocities;
	      this.panValues= panValues;
	   }
	   
	   public void run() {
			File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
			File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
			File f3 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\silence.WAV");
			//velocity = 3 so each file is 50%
			//duration is 2 seconds	
			
			
			 double duration = strokeDuration/1000;
			//now that i have separate loop counts can actually try to do % of each file.
			//double duration = 0.5;
			//silence only needs to be played when f1 = 100%
			 //could just use a clip to play the silence? 
			 
			//i need the duration to accurately reflect how long each file will be played for
			 // make a function for this.
			 //function will take the mix percentages
			 //will return an array with the duration in seconds for 3 sound files
			 ArrayList<Coordinate> mixPercentages = player.determineMixPercentageFor3Files(velocities);
			 double[] durations = player.calculateDurationsFor3Files(mixPercentages, velocities, duration);
			 
			 
			int loopCount = player.calculateLoopCount(f1, durations[0]);
			int loopCount2 = player.calculateLoopCount(f2, durations[1]);
			int loopCount3 = player.calculateLoopCount(f3, durations[2]);
			AudioInputStream[] audioInputStreams = player.stretchFileFor(f1, durations[0]);
			AudioInputStream[] audioInputStreams2 = player.stretchFileFor(f2, durations[1]);
			AudioInputStream[] audioInputStreams3 = player.stretchFileFor(f3, durations[2]);
			

			player.mixStreamsFor(audioInputStreams, loopCount, audioInputStreams2, loopCount2,
					audioInputStreams3, loopCount3, 
					mixPercentages, duration, panValues);
			
			//test for panning the sound
			//player.playFor(f3, duration, panValues);
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
