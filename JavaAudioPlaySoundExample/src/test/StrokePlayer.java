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
	public SoundDemo stage;
	public int index = 500;
	private int answer = -1;
	public int PENCIL = 0;
	public int METAL = 1;
	public int CHALK = 2;
	public int ERASER = 3;
	
	
	public StrokePlayer(ArrayList<File> soundFileNames, SoundDemo stage) {
		this.soundFileNames = soundFileNames;
		this.stage = stage;
	}
	
	public void playNext() {
		if (index < soundFileNames.size()) {
		try {
			File stroke = soundFileNames.get(index);
			setAnswer(soundFileNames.get(index).toString());
			index++;
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
			ShapeSoundsDemo x = new ShapeSoundsDemo(stage.model);
			stage.close();
		}
		
	}
	
	public void setAnswer(String fileName) {
		String pencilFile = new File("src\\soundAndImageFiles\\pencilSelectionAndStroke.WAV").getAbsolutePath().toString();
		String metalFile = new File("src\\soundAndImageFiles\\metalSelectionAndStroke.WAV").getAbsolutePath().toString();
		String chalkFile = new File("src\\soundAndImageFiles\\chalkSelectionAndStroke.WAV").getAbsolutePath().toString();
		String eraserFile = new File("src\\soundAndImageFiles\\eraserSelectionAndStroke.WAV").getAbsolutePath().toString();				
		if (fileName.equals(pencilFile)) {
			answer = PENCIL;			
		}
		if (fileName.equals(metalFile)) {
			answer = METAL;			
		}
		if (fileName.equals(chalkFile)) {
			answer = CHALK;			
		}
		if (fileName.equals(eraserFile)) {
			answer = ERASER;			
		}				
	}
	
	public int getAnswer() {
		return answer;
	}
}
