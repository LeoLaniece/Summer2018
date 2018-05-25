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
			File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");					
			double duration = strokeDuration/1000;			
			
			//set up clip
         AudioInputStream audioIn;
			try {
				
				audioIn = AudioSystem.getAudioInputStream(f1); 
				// Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
        // clip.loop(2);
         sleep(100);
         
	      FloatControl volCtrl;
	    //  Mixer mixer = AudioSystem.getMixer(null);	      
	      volCtrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);	    	      	      	      	      	            	      
	      volCtrl.setValue(0);
         clip.start();
         //13184
         System.out.println("clip frames "+clip.getFrameLength());
         while (clip.isActive()) {
        	 if (clip.getFramePosition() < ((clip.getFrameLength()/4))) {
        		 volCtrl.setValue(-80f);
        	 }else {
        		 volCtrl.setValue(0f);
        	 }
        	 
        	 if (clip.getFramePosition() < ((clip.getFrameLength()/4) *3)) {
        		 volCtrl.setValue(-80f);
        	 }
        	 
         }

         clip.close();
         
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
	   }}
