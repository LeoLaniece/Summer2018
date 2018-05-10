package test;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;

public class Draw2Model {
	//the model listeners
	ArrayList<modelListener> modelListeners;
	
	
    public Path path;  
    public static final Double DEFAULTSTROKE = 3.0;
    public static final Double MAXSTROKE = 30.0;
    public static final Double MINSTROKE = 1.0;
    Button btnClear;
    Slider strokeSlider;
    Label labelStroke;
    final Line sampleLine; 
    final Label colorLabel;
    
    public Draw2Model() {
    	modelListeners = new ArrayList<>();
    	
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
        
    }
    public void addSubscriber(modelListener ml) {
    	modelListeners.add(ml);
    }
    public void notifySubscribers() {
    	modelListeners.forEach(a->a.modelChanged());
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