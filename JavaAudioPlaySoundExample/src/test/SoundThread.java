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
			File f3 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");

			
			
			 double duration = strokeDuration/1000;
			 //if the duration is less than the silence duration, play a tap sound.
			 //implement this for next time, also change the loop setter to always set to at least 1 
			 

			 ArrayList<Coordinate> mixPercentages = player.determineMixPercentage(velocities);
			 double[] durations = player.calculateDurationsFor2Files(mixPercentages, velocities, duration);
			 
			 
			// double dur = player.calculateLoopCount(f3, duration);
			 
			int loopCount = player.calculateLoopCount(f1, durations[0]);
			int loopCount2 = player.calculateLoopCount(f2, durations[1]);
		//	int loopCount3 = player.calculateLoopCount(f3, durations[2]);
			AudioInputStream[] audioInputStreams = player.stretchFileFor(f1, durations[0]);
			AudioInputStream[] audioInputStreams2 = player.stretchFileFor(f2, durations[1]);
		//	AudioInputStream[] audioInputStreams3 = player.stretchFileFor(f3, durations[2]);
			
			//progress report,
			//get muting to happen when mixing 2 files
			//seems like the problem is that one of the audio streams runs out before the length of the recording is complete
			//this leads to odd behavior, keep investigating.
			//one of the loops reaches 0 well before the others.

			player.mixStreamsFor2(audioInputStreams, loopCount, audioInputStreams2, loopCount2,
				//	audioInputStreams3, loopCount3, 
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
