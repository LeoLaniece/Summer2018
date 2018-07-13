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

/**
 * This class plays the drawing sounds given the path velocity, location and timbre
 * 
 * The drawing sound is produced by playing an ultra short of a stroke sound ("clip")
 * The clip is played in a customized ADSR envelope
 * The clip begins silent, rises to maximum volume, sustains and then returns to silence
 * The maximum volume is determined from the stroke velocity.
 * A high velocity will produce a loud volume and vice-versa
 * 
 * This sound generator plays multiple, slightly staggered clips at all times.
 * Because the clips are fading in and out of volume at all times, 
 * the result is very close to the sound of a 'real world' sustained stroke
 * 
 * The sound will also be stereo panned relative to the path location in the workspace
 * If the path is drawn on the left side of the workspace, it will sound louder in the left speaker.
 * @author Léo Lanièce
 *
 */
public class AnInteractiveStaggeredSoundGenerator extends Thread{
	
	public String name;
	double velocity;
	float panValue;
	double clipDuration;	
	public boolean mouseReleased = false;
	double clipStaggerIncrement;
	public long timeSinceLastUpdate =0;
	File sustainFile; 
	AudioContext ac = null;
	
	/**
	 * Takes a stroke velocity, panValue and clipDuration to produce a sound for you!
	 * Will also play a short clip to mark the beginning of the stroke
	 * @param name
	 * @param velocities
	 * @param panValue
	 * @param clipDuration
	 * @param clipStaggerIncrement
	 * @param sustainFile
	 * @param impactFile
	 */
	public AnInteractiveStaggeredSoundGenerator(String name,  double velocities, float panValue 
			, double clipDuration, double clipStaggerIncrement, File sustainFile, File impactFile) {							
		this.name = name;
		velocity =velocities;
		this.panValue =panValue;
		this.clipDuration = clipDuration;				
		this.clipStaggerIncrement =clipStaggerIncrement;
		this.sustainFile = sustainFile;			
		try {
			//Prepares the clip for playing
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(impactFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);	
			//places a volume control on the clip
			FloatControl gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				//sets volume to maximum
				gainControl.setValue(6.0f); 		
				//sets a pan value control on the clip
				FloatControl panControl = 
					    (FloatControl) clip.getControl(FloatControl.Type.PAN);
				if (panValue > 0) {
					panControl.setValue(1);
				}
				if (panValue <= 0) {
					panControl.setValue(-1);
				}			
				//plays the clip
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
		//will play staggered clips until the mouse is released
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
	
	/**
	 * will update the thread to notify an end to the staggered clips
	 * @param x
	 */
	public void setMouseReleased(boolean x) {
		mouseReleased = x;
	}
	
	/**
	 * Will prepare the next clip for playing
	 * @param ac
	 * @return
	 */
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
	    
	    //control reference for panning the clip
	    Envelope panGlide = new Envelope(ac, panValue);	    		    	    
	    //control reference for adjusting the volume of the clip
	    Envelope gainGlide = new Envelope(ac, 1.0f);	    	    
	    // instantiate a GranularSamplePlayer
	    GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);			    	    
	    // set the grain size to a fixed 10ms
	    gsp.setGrainSize(new Static(ac, 100.0f));	
	    
	    //Create the customized ADSR envelope
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
	
	/**
	 * Update the clip velocity
	 * @param velocity
	 */
	public void setVelocity(double velocity) {
		this.velocity =velocity;
		timeSinceLastUpdate = System.currentTimeMillis();
	}
	/**
	 * update the clip stereo panning
	 * @param panValue
	 */
	public void setPanValue(float panValue) {
		this.panValue = panValue;
	}
	/**
	 * Will stop the sound generator
	 */
	public void closeSoundGenerator() {
		if (ac != null) {
			ac.stop();
			ac = null;
		}
		
	}
}
