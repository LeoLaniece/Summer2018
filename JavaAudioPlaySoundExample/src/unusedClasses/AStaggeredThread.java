package unusedClasses;

import java.io.File;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Panner;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;
import test.StaggeredSoundThread;
import net.beadsproject.beads.ugens.SamplePlayer;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AStaggeredThread extends StaggeredSoundThread{

	//be aware that strokeDuration is actually the clip duratio
	public File f;
	public float maxVolume;
	float panValue;
	
	AStaggeredThread(String name, Grain2Files p, double cDuration, File f, float maxVol, ArrayList<Coordinate> velocities, float panValue) {
		super(name, p, cDuration, velocities, null);	
		this.f = f;
		maxVolume = maxVol;
		this.panValue =panValue;
	}
	
	@Override
	 public void run() {
			//File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");					
			//double duration = strokeDuration/1000;						
			
		    // instantiate the AudioContext
		    AudioContext ac = new AudioContext();
		    
		    // load the source sample from a file
		    Sample sourceSample = null;
		    
		    
		    try
		    {
		      sourceSample = new Sample(f.toString());
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
		    Envelope gainGlide = new Envelope(ac, 0.0f);		    
		    
		    // instantiate a GranularSamplePlayer
		    GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);
		    //WavePlayer gsp = new WavePlayer(ac, freqEnv, Buffer.SINE);
		    
		    // tell gsp to loop the file
		 //   gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
		    
		    // set the grain size to a fixed 10ms
		    gsp.setGrainSize(new Static(ac, 100.0f));
		    
		    Panner p = new Panner(ac, panGlide);

		    
		    
			  Gain g = new Gain(ac, 1, gainGlide);
			  		gainGlide.addSegment(0f, (float) (strokeDuration*0.1));
				  	gainGlide.addSegment(maxVolume, (float) (strokeDuration*0.3));
				  	gainGlide.addSegment(maxVolume, (float) (strokeDuration*0.3));
				  	gainGlide.addSegment(0f, (float) ((strokeDuration*0.3)));
			  

			  g.addInput(p);
			  p.addInput(gsp);
		    
		    // connect gsp to ac
		    ac.out.addInput(g);
		   // ac.out.addInput(p);
		    
		    // begin audio processing
		    ac.start();									
			
			
			//set up clip
			//try {
				
				//progress report
				//everything works, but the volume control does not act quickly enough to produce an envelope on the sound clip. 
				
				
				
				/*
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(f1); 
				// Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.loop(0);
         
         
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
        		 volCtrl.setValue(-20f*volume);
        		// System.out.println("fading in! "+-30f*volume);
        	 }        		  
        		     		 
        	         	 
        	 if (clip.getFramePosition() > ((clip.getFrameLength()/4) *2)) {
        		//fade out
        		 float lastClipBit = clip.getFrameLength() - ((clip.getFrameLength()/4) *2);
        		 float clipLeft = clip.getFrameLength() -clip.getFramePosition();
        		 float volume = 1-clipLeft/lastClipBit;
        		 //System.out.println("fading out! "+-30f*volume);
        		 volCtrl.setValue(-20f*volume);
        		 
        	 }        	 
         }
         System.out.println("sound done");
         
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
	   }}
