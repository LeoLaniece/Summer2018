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
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import java.io.IOException;
public class ToneDrawing {
	
	public ToneDrawing(double strokeDuration) {
	
	AudioContext ac = new AudioContext();

	Envelope freqEnv = new Envelope(ac, 440);
    
    // create a Glide to control the gain - give it 5000ms ramp time
    Envelope gainGlide = new Envelope(ac, 0.0f);		    
    
    // instantiate a GranularSamplePlayer
   // GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);
    WavePlayer gsp = new WavePlayer(ac, freqEnv, Buffer.SINE);
    
    //declare a panner to change the panning!
    //try to change the volume based on the velocity
    
    // tell gsp to loop the file
 //   gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
    
    // set the grain size to a fixed 10ms
  //  gsp.setGrainSize(new Static(ac, 100.0f));
    
	  Gain g = new Gain(ac, 1, gainGlide);
	  	//	gainGlide.addSegment(0f, 0f);
		//  	gainGlide.addSegment(1.0f, (float) (strokeDuration/4));
		//  	gainGlide.addSegment(1f, (float) (strokeDuration/2));
		//  	gainGlide.addSegment(0f, (float) ((strokeDuration/4)-20));
	  

	  g.addInput(gsp);
    
    // connect gsp to ac
    ac.out.addInput(g);
    
    // begin audio processing
    ac.start();	
    long start = System.currentTimeMillis();
    while (System.currentTimeMillis()-start < strokeDuration) {
    	//wait
    }
    ac.stop();
    
}}
