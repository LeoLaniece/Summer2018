

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.WavePlayer;

public class Lesson08_Granulation {

	public static void main(String[] args) {

		AudioContext ac;

		ac = new AudioContext();
		/*
		 * In lesson 4 we played back samples. This example is almost the same
		 * but uses GranularSamplePlayer instead of SamplePlayer. See some of
		 * the controls below.
		 * 
		 * 
		 * yourDesktopPath + "1234.aif";
		 */
		//String audioFile = "audio/1234.aif";
		String yourDesktopPath = System.getProperty("user.home") + "\\Desktop\\";
		String fileName = "C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.aif";
		String audioFile = fileName;
		System.out.println(fileName);
		
		//sound 1 soft scratch
		GranularSamplePlayer player = new GranularSamplePlayer(ac,
				SampleManager.sample(audioFile));				
		/*
		 * Have some fun with the controls.
		 */
		
		//i want to play both sound 1 and sound 2 at the same time
		//i want the grains from sound 1 to play in between grains of sound 2
		//i want to generate a new sound of medium level scratching
		//i want to only loop a very small portion of each WAV file, like 1 second.
		
		// loop the sample at its end points
		player.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
		player.start(2000f);
		
		//player.getLoopStartUGen().setValue(2000);
		player.setLoopPointsFraction(0.2f, 0.3f);
		//player.getLoopEndUGen().setValue(
			//	(float)SampleManager.sample(audioFile).getLength());
		
		// control the rate of grain firing
		Envelope grainIntervalEnvelope = new Envelope(ac, 100);
		//1rst arg = time in ms between each grain firing.
		//2nd arg = how long until the program sets into the 1rst arg
		grainIntervalEnvelope.addSegment(1000, 0);
		player.setGrainInterval(grainIntervalEnvelope);
		
		// control the playback rate
		Envelope rateEnvelope = new Envelope(ac, 1);
		//1rst arg = the speed of the play sample by the end of time
		//second arg = how long in ms the action will last for
		rateEnvelope.addSegment(1, 5000);
		rateEnvelope.addSegment(1, 0);
		//rateEnvelope.addSegment(0, 2000);
		//rateEnvelope.addSegment(0.1f, 2000);
		player.setRate(rateEnvelope);
		
		// a bit of noise can be nice
		player.getRandomnessUGen().setValue(0.01f);
		
		//sound 2 the loudSCratch sound
		//String yourDesktopPath = System.getProperty("user.home") + "\\Desktop\\";
		String newFileName = "C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\scratch-loud.WAV";
		//String audioFile = newFileName;
		System.out.println(newFileName);
		
		//sound 1 soft scratch
		GranularSamplePlayer player2 = new GranularSamplePlayer(ac,
				SampleManager.sample(newFileName));				
		/*
		 * Have some fun with the controls.
		 */
		
		// loop the sample at its end points
		player2.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
		//player.getLoopStartUGen().setValue(2000);
		player2.setLoopPointsFraction(0.5f, 1.0f);
		//player.getLoopEndUGen().setValue(
			//	(float)SampleManager.sample(audioFile).getLength());
		
		// control the rate of grain firing
		Envelope grainIntervalEnvelope2 = new Envelope(ac, 100);
		//1rst arg = time in ms between each grain firing.
		grainIntervalEnvelope2.addSegment(10, 10000);
		player2.setGrainInterval(grainIntervalEnvelope2);
		
		// control the playback rate
		Envelope rateEnvelope2 = new Envelope(ac, 1);
		//1rst arg = the speed of the play sample by the end of time
		//second arg = how long in ms the action will last for
		rateEnvelope2.addSegment(1, 5000);
		rateEnvelope2.addSegment(1, 0);
		//rateEnvelope.addSegment(0, 2000);
		//rateEnvelope.addSegment(0.1f, 2000);
		player2.setRate(rateEnvelope2);
		
		// a bit of noise can be nice
		player2.getRandomnessUGen().setValue(0.01f);//setValue(0.01f);
		
		/*
		 * And as before...
		 */
		Gain g = new Gain(ac, 2, 0.8f);
		g.addInput(player2);
		g.addInput(player);
		ac.out.addInput(g);
		ac.start();

	}

}
