package threadDemo;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ThreadDemo2 extends Thread {
	   private Thread t;
	   private String threadName;
	   
	   ThreadDemo2( String name) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	   }
	   
	   public void run() {
	      System.out.println("Running " +  threadName );
	      try {	
	    	  //make this use the granular synthesis
	            // audio for the scratch
	            File url = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.WAV");
	            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	            // Get a sound clip resource.
	            Clip clip = AudioSystem.getClip();
	            // Open audio clip and load samples from the audio input stream.
	            clip.open(audioIn);
	            
	    	  //what i do here will alternate with the other thread 
	    	  //as long as there is sleep time.
	         for(int i = 1; i > 0; i--) {
	        	 //try getting this to play sound	        	 
	        	 clip.start();
	        	 clip.setMicrosecondPosition(0);
	            System.out.println("Thread: " + threadName + ", " + i);
	            // Let the thread sleep for a while.
	            //Thread.sleep(0);
	         }
	      } catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	

