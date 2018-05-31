

import java.io.File;
import sun.audio.AudioStream;
import java.io.FileInputStream;
import java.io.InputStream;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.Noise;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;

public class Lesson01_AudioContext {

	public static void main(String[] args) {
		
		InputStream in;
		try {
			in = new FileInputStream(
					new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.aif"));
			AudioStream audioS = new AudioStream(in);
		}
		catch(Exception e){}
		
		
		

		// instantiate the AudioContext
	   /* AudioContext ac = new AudioContext();

		MakeSound ms = new MakeSound();
		String yourDesktopPath = System.getProperty("user.home") + "\\Desktop\\";
		String fileName = yourDesktopPath + "1234.aif";
		//ms.playSound(fileName);
		Sample sourceSample = null;
		try
		{
			sourceSample = new Sample(fileName);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		 // instantiate a GranularSamplePlayer
	    GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);
	    
	    // tell gsp to loop the file
	    gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
	    
	    // set the grain size to a fixed 10ms
	    gsp.setGrainSize(new Static(ac, 100.0f));
	    
	    // tell gsp to behave somewhat randomly
	    gsp.setRandomness(new Static(ac, 1000.0f));
	    
	    // connect gsp to ac
	    ac.out.addInput(gsp);
	    
	    // begin audio processing
	    ac.start();
		
		//THINGS DONE:
		
		 * INSTALLED THE MICROPHONE TO COMPUTER
		 * successfully played back custom recording using JAVA.sound
		 * custom recording needs to be stored as a WAV file in order to play back
		 * record two scratchy sounds(loud, soft)
		 * 1)sract-soft
		 * 2)scratch-loud
		 * 
		 * THINGS TO DO		 
		 * use granular synthesis to create an medium scratch sound 
		 * 
		 * 
		 */
		
		//File x = new File();
		/*
		AudioContext ac;

		ac = new AudioContext();		
		WavePlayer wp = new WavePlayer(ac,440.0f,Buffer.SINE);
		Gain g = new Gain(ac, 1, 0.1f);
		g.addInput(wp);
		ac.out.addInput(g);
		/*
		 * Finally, start things running.
		 
		ac.start();*/
	}

}
