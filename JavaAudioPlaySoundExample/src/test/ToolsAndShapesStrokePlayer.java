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
public class ToolsAndShapesStrokePlayer {
	
	private ArrayList<File> soundFileNames;
	private ArrayList<Coordinate> toolAndShapeClipsID;
	public ShapesAndToolsSoundDemo stage;
	public int index = 0;
	private int[] answer = new int[2];
	public int TRIANGLE = 0;
	public int SQUARE = 1;
	public int SQUIGGLE = 2;
	public int FILLEDCIRCLE = 3;
	
	
	public ToolsAndShapesStrokePlayer(ArrayList<File> soundFileNames, ArrayList<Coordinate> toolAndShapeClipsID, ShapesAndToolsSoundDemo stage) {
		this.soundFileNames = soundFileNames;	
		this.toolAndShapeClipsID = toolAndShapeClipsID;
		this.stage = stage;
	}
	
	public void playNext() {
		if (index < soundFileNames.size()) {
		try {
			File stroke = soundFileNames.get(index);
			setAnswer(index);
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
			stage.model.showTaskStage();
			stage.close();			
		}		
	}
	
	public void setAnswer(int indx) {
		answer[0] = (int) toolAndShapeClipsID.get(indx).x;
		answer[1] = (int) toolAndShapeClipsID.get(indx).y;
	}
	
	public int[] getAnswer() {
		return answer;
	}
}
