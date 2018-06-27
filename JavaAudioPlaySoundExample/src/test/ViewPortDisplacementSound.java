package test;
import java.io.File;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Panner;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;

public class ViewPortDisplacementSound extends Thread{
	
	public boolean displacementInProgress;
	AudioContext ac;
	private double clipDuration;
	float maxVolume = 0.5f;
	long timeSinceLastUpdate = System.currentTimeMillis();
	Envelope gainGlide;
	Envelope wpPitch;
	Envelope wp2Pitch;
	Envelope panGlide;
	
	ViewPortDisplacementSound(){
		this.displacementInProgress = true;
		//File c = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\drag.WAV");
		//clipDuration = fileLength(c)*1000;
		ac = new AudioContext();
		setUpSampleWavePlayer(ac);
	}
	
	@Override
	public void run() {						
			ac = new AudioContext();
			Panner g2 = setUpSampleWavePlayer(ac);					
			ac.out.addInput(g2);
			ac.start();	
	}
	
	public Panner setUpSampleWavePlayer(AudioContext ac) {
		  wpPitch = new Envelope(ac, 130);
		  wp2Pitch = new Envelope(ac, 195);		
		  WavePlayer wp = new WavePlayer(ac, wpPitch, Buffer.SINE);
		  WavePlayer wp2 = new WavePlayer(ac, wp2Pitch, Buffer.SINE);
		  gainGlide = new Envelope(ac, 0f);
		  Gain g = new Gain(ac, 1, gainGlide);
			  gainGlide.addSegment(0.5f, 500);//maxVolume, 500);				      
			  g.addInput(wp);
			  g.addInput(wp2);
			  	panGlide = new Envelope(ac, -1f);
			  	Panner pan = new Panner(ac, panGlide);
			  	pan.addInput(g);
			    return pan;			  			  
	}	
	
	public void updateDisplacementProgress() {			
		while (gainGlide.getValue() > 0) {
			gainGlide.setValue(gainGlide.getValue() -0.05f);
		}		
		ac.stop();				
	}
	
	public void updateVelocity(double v) {
		maxVolume = (float)v;
		if ((System.currentTimeMillis()-timeSinceLastUpdate)> 100) {
			maxVolume = 0f;
		}
		if (maxVolume > 0.5) {
			maxVolume = 0.5f;
		}
		gainGlide.setValue(maxVolume);		
		timeSinceLastUpdate = System.currentTimeMillis();
	}
	
	/** 
	 * will adjust the pitch and the panning values of the wavePlayer based on viewport's
	 * current location
	 * @param viewPortCenter
	 */
	public void updateLocation(Coordinate viewPortCenter) {
		//update pitch
		float fundamental = (float) (200*(1-viewPortCenter.y));
		wpPitch.setValue(fundamental*4);
		wp2Pitch.setValue(fundamental*5);
		
		//update pan value
		//0.5 is the center
		panGlide.setValue((float) ((viewPortCenter.x-0.5)*10));		
	}
	
	public float fileLength(File soundFile) {
	     //calculate the time of the sustained sound file you will be using	      
	      AudioInputStream audioInputStream4;		
			try {
				audioInputStream4 = AudioSystem.getAudioInputStream(soundFile);
						AudioFormat format = audioInputStream4.getFormat();
		      long audioFileLength = soundFile.length();
		      int frameSize = format.getFrameSize();
		      float frameRate = format.getFrameRate();
		      return (audioFileLength / (frameSize * frameRate));
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return 0;		
	}
	
	//for real pre-recorded sounds
	 public void run1() {		
		//progress
		//not getting sustained sound.		
		//change the sound if you want something different
		
		while (displacementInProgress) {				
		ac = new AudioContext();
		Gain g2 = setUpSamplePlayer(ac);					
		ac.out.addInput(g2);
		ac.start();
		try {
			sleep( (long)(clipDuration-(clipDuration*0.9)));
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}		
	}
	
	public Gain setUpSamplePlayer(AudioContext ac) {								
		 // load the source sample from a file
	    Sample sourceSample = null;		
	   // Sample sourceSample2 = null;
	    try
	    {
	      sourceSample = new Sample("C:\\Users\\HCILab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\drag.WAV");
	     // sourceSample2 = new Sample(f2.toString());
	    }
	    catch(Exception e)
	    {
	      /*
	       * If the program exits with an error message,
	       * then it most likely can't find the file
	       * or can't open it. Make sure it is in the
	       * root folder of your project in Eclipse.
	       * Also make sure that it is a 16-bit,
	       * 44.1kHz audio file. These can be created
	       * using Audacity.
	       */
	      System.out.println(e.getMessage());
	      e.printStackTrace();
	      System.exit(1);
	    }
	    gainGlide = new Envelope(ac, 1.0f);
	    GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);			    
 	    // set the grain size to a fixed 10ms
	    gsp.setGrainSize(new Static(ac, 100.0f));	    
	    Gain g = new Gain(ac, 1, gainGlide);
 		gainGlide.addSegment(0f, (float) (clipDuration*0.1));
	  	gainGlide.addSegment(maxVolume, (float) (clipDuration*0.3));
	  	gainGlide.addSegment(maxVolume, (float) (clipDuration*0.3));
	  	gainGlide.addSegment(0f, (float) ((clipDuration*0.3)));  
	  	g.addInput(gsp);	
	  	return g;
	}
	
}
