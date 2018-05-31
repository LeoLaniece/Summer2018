package test;

import java.io.File;
import java.util.ArrayList;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Panner;
import net.beadsproject.beads.ugens.Static;

public class AnInteractiveStaggeredThread extends Thread{
	//be aware that strokeDuration is actually the clip duratio
		public String name;
		double velocity;
		float panValue;
		double clipDuration;
		double pathAngle;
		
		/**
		 * takes the stroke velocity panValue and clip duration to produce a sound for you!
		 * @param name
		 * @param velocities
		 * @param panValuedouble
		 * @param clipDuration
		 */
		AnInteractiveStaggeredThread(String name,  double velocities, float panValue, double pathAngle, double clipDuration) {							
			this.name = name;
			velocity =velocities;
			this.panValue =panValue;
			this.clipDuration = clipDuration;
			this.pathAngle =pathAngle;
		}
		
		@Override
		 public void run() {
				File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");					
				float maxVolume = (float) (1 *(velocity));	
				if (pathAngle <90) {
					maxVolume = 10;
				}
				System.out.println("velocity "+velocity);
				System.out.println("pathAngle "+pathAngle);
				System.out.println("max volume "+maxVolume);
			    // instantiate the AudioContext
			    AudioContext ac = new AudioContext();
			    
			    // load the source sample from a file
			    Sample sourceSample = null;			    			    
			    try
			    {
			      sourceSample = new Sample(f1.toString());
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
			    
			    // set the grain size to a fixed 10ms
			    gsp.setGrainSize(new Static(ac, 100.0f));
			    
			    Panner p = new Panner(ac, panGlide);			    
			    
				  Gain g = new Gain(ac, 1, gainGlide);
				  		gainGlide.addSegment(0f, (float) (clipDuration*0.1));
					  	gainGlide.addSegment(maxVolume, (float) (clipDuration*0.3));
					  	gainGlide.addSegment(maxVolume, (float) (clipDuration*0.3));
					  	gainGlide.addSegment(0f, (float) ((clipDuration*0.3)));
				  

				  g.addInput(p);
				  p.addInput(gsp);
			    
			    // connect gsp to ac
			    ac.out.addInput(g);
			    //ac.out.addInput(p);
			    
			    // begin audio processing
			    ac.start();									
		}
}
