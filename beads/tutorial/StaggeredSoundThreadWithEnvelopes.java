import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.SamplePlayer;

/**
 * should play a sound surrounded by an envelope.
 * @author HCI Lab
 *
 */
public class StaggeredSoundThreadWithEnvelopes {
	
	public static void main(String args[]) {
    // instantiate the AudioContext
    AudioContext ac = new AudioContext();
    
    // load the source sample from a file
    Sample sourceSample = null;
    try
    {
      sourceSample = new Sample("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");
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
    
    //set up an envelope and try to do your thing.
    Envelope freqEnv = new Envelope(ac, 500);
    
    // create a Glide to control the gain - give it 5000ms ramp time
    Envelope gainGlide = new Envelope(ac, 0.0f);

    

    
    // instantiate a GranularSamplePlayer
    GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);
    
    //set the glide to the gsp
   // gsp.setPitch(freqEnv);
    //doesn't quite work yet?
    //freqEnv.addSegment(100, 1000);
  //  gainGlide.add//addSegment(1000, 1000);
    // tell gsp to loop the file
    gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
    
    // set the grain size to a fixed 10ms
    gsp.setGrainSize(new Static(ac, 100.0f));
    
    // tell gsp to behave somewhat randomly
  //  gsp.setRandomness(new Static(ac, 1000.0f));
    
    //need gain control here!
	  Gain g = new Gain(ac, 1, gainGlide);
	  for (int i = 0; i<100;i++) {
		  	  gainGlide.addSegment(1.0f, 100);
		  	  gainGlide.addSegment(0f, 100);
	  }

	  g.addInput(gsp);
    
    // connect gsp to ac
    ac.out.addInput(g);
    
    // begin audio processing
    ac.start();


}
}


