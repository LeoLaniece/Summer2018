package test;

import java.io.File;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Panner;
import net.beadsproject.beads.ugens.Static;

public class AnInteractiveStaggeredSoundGenerator extends Thread{
	
	public String name;
	double velocity;
	float panValue;
	double clipDuration;
	
	//can eliminate this
	double pathAngle;
	
	
	public boolean mouseReleased = false;
	double clipStaggerIncrement;
	public long timeSinceLastUpdate =0;
	File sustainFile; 
	AudioContext ac = null;
	
	/**
	 * takes the stroke velocity panValue and clip duration to produce a sound for you!
	 * @param name
	 * @param velocities
	 * @param panValuedouble
	 * @param clipDuration
	 */
	public AnInteractiveStaggeredSoundGenerator(String name,  double velocities, float panValue 
			, double clipDuration, double clipStaggerIncrement, File sustainFile, File impactFile) {							
		this.name = name;
		velocity =velocities;
		this.panValue =panValue;
		this.clipDuration = clipDuration;
		
		//eliminate this
		this.pathAngle =180;
		
		
		this.clipStaggerIncrement =clipStaggerIncrement;
		this.sustainFile = sustainFile;			
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(impactFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);	
			FloatControl gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(6.0f); 		
				//ALSO SET the pan value here
				FloatControl panControl = 
					    (FloatControl) clip.getControl(FloatControl.Type.PAN);
				if (panValue > 0) {
					panControl.setValue(1);
				}
				if (panValue <= 0) {
					panControl.setValue(-1);
				}										
			clip.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	 public void run() {
		ac = new AudioContext();
		Panner g;
		while (!mouseReleased) {			
			g = setUpSamplePlayer(ac);		  
		    ac.out.addInput(g);
		    ac.start();	
		    try {
				sleep( (long)(clipStaggerIncrement));											
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}				    								
	}
	
	public void setMouseReleased(boolean x) {
		mouseReleased = x;
	}
	
	public Panner setUpSamplePlayer(AudioContext ac) {				
		//will silence the sound generator when the user stops moving his mouse cursor, but does not release the press.
		if (System.currentTimeMillis()-timeSinceLastUpdate > 100) {
			velocity =0;
		}
		
		float maxVolume = (float) ((velocity));					    	    
	    // load the source sample from a file
	    Sample sourceSample = null;		
	    try
	    {
	      sourceSample = new Sample(sustainFile.toString());
	    }
	    catch(Exception e)
	    {
	      /*
	       * If the program exits with an error message,
	       * then it most likely can't find the file
	       * or can't open it. Make sure it is in the
	       * root folder of your project in Eclipse.
	       * Also make sure that it is a 16-bit,
	       * 44.1kHz audio file.
	       */
	      System.out.println(e.getMessage());
	      e.printStackTrace();
	      System.exit(1);
	    }	    	    	    
	    Envelope panGlide = new Envelope(ac, panValue);	    		    
	    // create a Glide to control the gain - give it 5000ms ramp time
	    Envelope gainGlide = new Envelope(ac, 1.0f);		    
	    
	    // instantiate a GranularSamplePlayer
	    GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);			    
	    
	    // set the grain size to a fixed 10ms
	    gsp.setGrainSize(new Static(ac, 100.0f));	    
	    Panner p = new Panner(ac, panGlide);		    	    
		  Gain g = new Gain(ac, 1, gainGlide);
		  		gainGlide.addSegment(0f, (float) (clipDuration*0.1));
			  	gainGlide.addSegment(maxVolume, (float) (clipDuration*0.3));
			  	gainGlide.addSegment(maxVolume, (float) (clipDuration*0.3));
			  	gainGlide.addSegment(0f, (float) ((clipDuration*0.3)));		  
		  g.addInput(gsp);
		  p.addInput(g);
		  return p;
	}
	public void setVelocity(double velocity) {
		this.velocity =velocity;
		timeSinceLastUpdate = System.currentTimeMillis();
	}
	public void setPanValue(float panValue) {
		this.panValue = panValue;
	}
	public void setAngle(double pathAngle) {
		//this.pathAngle = pathAngle;
	}
	public void closeSoundGenerator() {
		if (ac != null) {
			ac.stop();
			ac = null;
		}
		
	}
}
