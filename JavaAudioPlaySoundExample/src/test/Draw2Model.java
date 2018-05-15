package test;

import java.io.File;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Draw2Model {
	//the model listeners
	ArrayList<modelListener> modelListeners;
	Group lineGroup;
	
    public Path path;  
    public static final Double DEFAULTSTROKE = 3.0;
    public static final Double MAXSTROKE = 30.0;
    public static final Double MINSTROKE = 1.0;
    Button btnClear;
    Slider strokeSlider;
    Label labelStroke;
    final Line sampleLine; 
    final Label colorLabel;
    public Grain2Files player;
    public ArrayList<Path> modelPaths;
    public ArrayList<Coordinate> modelPathsCoordinates;
    public ArrayList<Coordinate> modelPathsTranslateByCoordinates;
    
    public Draw2Model() {
    	modelListeners = new ArrayList<>();
    	modelPaths = new ArrayList<>();
    	modelPathsCoordinates = new ArrayList<>();
    	modelPathsTranslateByCoordinates = new ArrayList<>();
    	path = null;
    	lineGroup = new Group();
    	
    	
    	btnClear = new Button("Clear");
    	btnClear.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {                
                notifySubscribers();
            }
        });    	
    	
    	strokeSlider = new Slider(MINSTROKE, MAXSTROKE, DEFAULTSTROKE);
    	labelStroke = new Label("Stroke Width");
    	sampleLine = new Line(0, 0, 140, 0);
        sampleLine.strokeWidthProperty().bind(strokeSlider.valueProperty());
        sampleLine.setStroke(Color.BLUE);        
        colorLabel = new Label("color: blue");
        
        //set up for sound things
        File soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");
        File soundFile2 = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
        player = new Grain2Files(soundFile, soundFile2);
        player.changeFrequency(0);
        
    }
    public void addSubscriber(modelListener ml) {
    	modelListeners.add(ml);
    }
    public void notifySubscribers() {
    	modelListeners.forEach(a->a.modelChanged());
    }
	//this bit should call a method in the model, just pass in the coordinates.
	public void startPath(double x, double y) {
    path = new Path();
   
    path.setSmooth(true);
    path.setStrokeWidth(sampleLine.getStrokeWidth());
    path.setStroke(sampleLine.getStroke());
    path.getElements().add(new MoveTo(x, y));
    modelPathsCoordinates.add(new Coordinate(x,y));    
    modelPathsTranslateByCoordinates.add(new Coordinate(0,0));
    
    lineGroup.getChildren().add(path);
    modelPaths.add(path);    
    path.toFront();
   // notifySubscribers();    
	}
	
	public void strokePath(double x, double y) {
		path.getElements().add(new LineTo(x, y));
	}
	public void pathToNull() {
		path = null;
	}
	
	public void calculateStroke(double distanceTraveled, long startTime) {
		//figure out how to calculate distance traveled.
		
	long strokeTime = (System.currentTimeMillis() -startTime);
	double strokeVelocity = ((distanceTraveled*1000)/strokeTime)/1000;
	System.out.println("Stroke velocity "+strokeVelocity);
	//works pretty well, but cannot draw while the play back occurs. Would a thread work?
	player.play2(strokeTime, strokeVelocity);
	//play sound based on the calculated time and velocity
	
	
	}
    
    /*
     * //should be in the model?
        //LEO'S SOUND CODE
        //test for GRAIN2FILES
         File soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");
         File soundFile2 = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.WAV");
         Grain2Files player = new Grain2Files(soundFile, soundFile2);
         player.changeFrequency(1.0);
         //implementation, needs to be able to loop. Done
         //loop based on interaction.
         

        
     
            // audio for the scratch
            File url = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\scratchLoud3.WAV");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
           // clip.loop(2);
            
            //audio prep for the eraser
            File file = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\eraser.WAV");
            AudioInputStream audioIn2 = AudioSystem.getAudioInputStream(file);
            // Get a sound clip resource.
            Clip clip2 = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip2.open(audioIn2);
     */
}
