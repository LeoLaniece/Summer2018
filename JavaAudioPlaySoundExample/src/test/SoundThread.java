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
	   public ArrayList<Double> velocities;
	   
	   SoundThread(String name, Grain2Files p, double strokeDuration, ArrayList<Double> velocities) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	      player = p;
	      this.strokeDuration = strokeDuration;
	      this.velocities = velocities;
	   }
	   
	   public void run() {
			File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");
			File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
			//velocity = 3 so each file is 50%
			//duration is 2 seconds	
			 double duration = strokeDuration/2000;
			//now that i have separate loop counts can actually try to do % of each file.
			//double duration = 0.5;
			int loopCount = player.calculateLoopCount(f1, duration);
			int loopCount2 = player.calculateLoopCount(f2, duration);
			AudioInputStream[] audioInputStreams = player.stretchFileFor(f1, duration);
			AudioInputStream[] audioInputStreams2 = player.stretchFileFor(f2, duration);
			ArrayList<Double> mixPercentages = player.determineMixPercentage(velocities);
			//still working on this function, right now the issue seems to be the mixing, mixing is not occuring.
			//probably involves the for loops. Try a few things
			//don't use for loops
			//switch the current state to value the second file more
			//works!!!
			player.mixStreamsFor(audioInputStreams, loopCount, audioInputStreams2, loopCount2, mixPercentages, duration*1000*2);
			//player.playStretchedFor(audioInputStreams2, loopCount2);
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
