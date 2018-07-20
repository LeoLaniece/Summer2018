package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Takes a list of sound file names
 * Plays the files one by one when prompted
 * 
 * @author HCI Lab
 *
 */
public class StrokePlayer {
	
	private ArrayList<File> soundFileNames;
	public int index = 0;
	
	
	public StrokePlayer(ArrayList<File> soundFileNames) {
		this.soundFileNames = soundFileNames;
		
	}
	
	public void playNext() {
		if (index < soundFileNames.size()) {
		try {
			File stroke = soundFileNames.get(index);
			index ++;
			//Prepares the clip for playing
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(stroke);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);	
			//places a volume control on the clip
			FloatControl gainControl = 
				    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				//sets volume to maximum
				gainControl.setValue(6.0f); 					
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
		}else {
			//change to next demo task
		}
		
	}
}
