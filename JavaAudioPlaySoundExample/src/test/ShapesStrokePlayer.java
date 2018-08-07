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
public class ShapesStrokePlayer {
	
	private ArrayList<File> soundFileNames;
	public ShapeSoundsDemo stage;
	public int index = 500;
	private int answer = -1;
	public int TRIANGLE = 0;
	public int SQUARE = 1;
	public int SQUIGGLE = 2;
	public int FILLEDCIRCLE = 3;
	
	
	public ShapesStrokePlayer(ArrayList<File> soundFileNames, ShapeSoundsDemo stage) {
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
			ShapesAndToolsSoundDemo x = new ShapesAndToolsSoundDemo(stage.model);
			stage.close();
		}
		
	}
	
	public void setAnswer(String fileName) {
		String triangleFile = new File("src\\soundAndImageFiles\\triangle1.WAV").getAbsolutePath().toString();
		String squareFile = new File("src\\soundAndImageFiles\\square1.WAV").getAbsolutePath().toString();
		String squiggleFile = new File("src\\soundAndImageFiles\\squiggle1.WAV").getAbsolutePath().toString();
		String circleFile = new File("src\\soundAndImageFiles\\circle1.WAV").getAbsolutePath().toString();				
		if (fileName.equals(triangleFile)) {
			answer = TRIANGLE;			
		}
		if (fileName.equals(squareFile)) {
			answer = SQUARE;			
		}
		if (fileName.equals(squiggleFile)) {
			answer = SQUIGGLE;			
		}
		if (fileName.equals(circleFile)) {
			answer = FILLEDCIRCLE;			
		}				
	}
	
	public int getAnswer() {
		return answer;
	}
}