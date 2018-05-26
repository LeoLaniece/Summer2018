package test;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AStaggeredThread extends StaggeredSoundThread{

	AStaggeredThread(String name, Grain2Files p, double strokeDuration) {
		super(name, p, strokeDuration);		
	}
	
	@Override
	 public void run() {
			File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.WAV");					
			double duration = strokeDuration/1000;						
			//set up clip
			try {
				
				//progress report
				//looping count needed to be 0, for some reason
				//try to understand why i am getting an error mesage in the fade out loop. 
				
				
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(f1); 
				// Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.loop(1);
         
         
         //
         
	      FloatControl volCtrl;      
	      volCtrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);	    	      	      	      	      	            	      
	      volCtrl.setValue(6f);	      
         clip.start();
         while (clip.getFrameLength() > clip.getFramePosition()) {
        	// volCtrl.setValue(6f);
        	 
        	 if (clip.getFramePosition() < ((clip.getFrameLength()/4))) {
        		 //fade in
        		 float framePos = clip.getFramePosition();
        		 float firstBitLength = clip.getFrameLength()/4;
        		 float volume = 1- framePos/firstBitLength;
        		 volCtrl.setValue(-30f*volume);
        		 System.out.println("fading in! "+-30f*volume);
        	 }        		  
        		     		 
        	         	 
        	 if (clip.getFramePosition() > ((clip.getFrameLength()/4) *2)) {
        		//fade out
        		 float lastClipBit = clip.getFrameLength() - ((clip.getFrameLength()/4) *3);
        		 float clipLeft = clip.getFrameLength() -clip.getFramePosition();
        		 float volume = 1-clipLeft/lastClipBit;
        		 volCtrl.setValue(-30f*volume);
        		 System.out.println("fading out! "+-30f*volume);
        	 }        	 
         }
         System.out.println("sound done");
         
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	   }}
