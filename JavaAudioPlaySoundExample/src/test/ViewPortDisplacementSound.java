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
	ViewPortDisplacementSound(){
		this.displacementInProgress = true;
	}

	@Override
	 public void run() {
		
		//change the sound if you want something different
		ac = new AudioContext();
		WavePlayer wp = new WavePlayer(ac, 60*6, Buffer.SINE);
		WavePlayer wp2 = new WavePlayer(ac, 60, Buffer.SINE);
		Gain g2 = new Gain(ac, 1, 0.5f);				
		g2.addInput(wp);
		g2.addInput(wp2);
		System.out.println("made it to the thread player");
		ac.out.addInput(g2);
		ac.start();
		while (displacementInProgress) {						
		    	//change nothing	    		    
		}
		ac.stop();		
		System.out.println("ac stopped");
	}
	
	public void updateDisplacementProgress() {
		displacementInProgress = false;
		ac.stop();
	}
	
}
