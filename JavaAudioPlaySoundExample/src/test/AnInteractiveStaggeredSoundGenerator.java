package test;

import java.io.File;

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
	double pathAngle;
	public boolean mouseReleased = false;
	double clipStaggerIncrement;
	public long timeSinceLastUpdate =0;
	
	/**
	 * takes the stroke velocity panValue and clip duration to produce a sound for you!
	 * @param name
	 * @param velocities
	 * @param panValuedouble
	 * @param clipDuration
	 */
	public AnInteractiveStaggeredSoundGenerator(String name,  double velocities, float panValue, 
			double pathAngle, double clipDuration, double clipStaggerIncrement) {							
		this.name = name;
		velocity =velocities;
		this.panValue =panValue;
		this.clipDuration = clipDuration;
		this.pathAngle =pathAngle;
		this.clipStaggerIncrement =clipStaggerIncrement;
	}
	
	@Override
	 public void run() {
		AudioContext ac = new AudioContext();
		Gain g;
		while (!mouseReleased) {			
			g = setUpSamplePlayer(ac);		  
		    ac.out.addInput(g);
		    ac.start();	
		    try {
				sleep( (long)(clipStaggerIncrement));							
				g = setUpSamplePlayer(ac);
				ac.out.addInput(g);
				 ac.start();	
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}				    								
	}
	
	public void setMouseReleased(boolean x) {
		mouseReleased = x;
	}
	
	public Gain setUpSamplePlayer(AudioContext ac) {
		File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");	
		//File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.WAV");
		if (timeSinceLastUpdate -System.currentTimeMillis() > 100) {
			velocity =0;
		}
		
		float maxVolume = (float) ((velocity));	
		if (pathAngle <90&&velocity!=0) {
			//maxVolume = 10;
		}
		System.out.println("velocity "+velocity);
		System.out.println("maxVolume "+maxVolume);
	    // instantiate the AudioContext
	    
	    
	    // load the source sample from a file
	    Sample sourceSample = null;		
	   // Sample sourceSample2 = null;
	    try
	    {
	      sourceSample = new Sample(f1.toString());
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
	    
	    Envelope panGlide = new Envelope(ac, panValue);
	    		    
	    // create a Glide to control the gain - give it 5000ms ramp time
	    Envelope gainGlide = new Envelope(ac, 1.0f);		    
	    
	    // instantiate a GranularSamplePlayer
	    GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);			    
	    //GranularSamplePlayer gsp2 = new GranularSamplePlayer(ac, sourceSample2);
	    
	    // set the grain size to a fixed 10ms
	    gsp.setGrainSize(new Static(ac, 100.0f));
	    //gsp2.setGrainSize(new Static(ac, 100.0f));
	    
	    Panner p = new Panner(ac, panGlide);			    
	    
		  Gain g = new Gain(ac, 1, gainGlide);
		  		gainGlide.addSegment(0f, (float) (clipDuration*0.1));
			  	gainGlide.addSegment(maxVolume, (float) (clipDuration*0.3));
			  	gainGlide.addSegment(maxVolume, (float) (clipDuration*0.3));
			  	gainGlide.addSegment(0f, (float) ((clipDuration*0.3)));
		  

		  g.addInput(p);
		  p.addInput(gsp);
		  return g;
	}
	public void setVelocity(double velocity) {
		this.velocity =velocity;
		timeSinceLastUpdate = System.currentTimeMillis();
	}
}
