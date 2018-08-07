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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CorrectionStageShapes extends Stage{
	
	private Stage me;
	private VBox root;
	private Text instructions;
	private StrokePlayer strokePlayer;
	private ArrayList<File> toolClips;
	private ArrayList<Integer> toolClipsID;
	public int TRIANGLE = 0;
	public int SQUARE = 1;
	public int SQUIGGLE = 2;
	public int FILLEDCIRCLE = 3;
	public int answer = -1;
	public Button btnTriangle;
	public Button btnSquare;
	public Button btnSquiggle;
	public Button btnCircle;
	
	
	
	public CorrectionStageShapes(int selectedAnswer, int correctAnswer){		
		me = this;		
		setTitle("Correction Stage");
        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
        int SceneHeight = javaServer.LaunchServer.SceneHeight;
	    root = new VBox();	        
	    Scene scene = new Scene(root, SceneWidth,SceneHeight);	    
	    setScene(scene);
	    setX(0);
	    setY(0);
	    show();  
	    setInstructions();
	    initializeButtons();
	    setButtons(selectedAnswer, correctAnswer);
	}
	
	public void setInstructions() {
		instructions = new Text("You selected an incorrect answer"+"\n"+
	"Press the button to the left to hear the sounds associated with the tool you selected"+"\n"+
				"The button to the right is the tool associated with the correct answer");
		root.getChildren().add(instructions);
	}
	
	public void initializeButtons() {
		btnTriangle = new Button("Triangle");
    	//add pencil picture
        File triangle = new File(new File("src\\soundAndImageFiles\\triangle-outline.png").getAbsolutePath());       
        Image image = new Image(triangle.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        btnTriangle.setGraphic(iv1);                
    	btnTriangle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\triangle1.WAV").getAbsolutePath()));
            }
        });    	
    	
    	
    	btnSquare = new Button("Square");
    	//add metal picture    	
    	File f2 = new File(new File("src\\soundAndImageFiles\\basic-square-outline.JPG").getAbsolutePath());         
        Image nail = new Image(f2.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv3 = new ImageView();
        iv3.setImage(nail);
        btnSquare.setGraphic(iv3);
    	btnSquare.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\square1.WAV").getAbsolutePath()));
            }
        });    	
    	
    	btnSquiggle = new Button("Squiggle");    	
    	File c = new File(new File("src\\soundAndImageFiles\\squiggle.JPG").getAbsolutePath());              
        Image chalk = new Image(c.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv4 = new ImageView();
        iv4.setImage(chalk);
        btnSquiggle.setGraphic(iv4);
    	btnSquiggle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\squiggle1.WAV").getAbsolutePath()));
            }
        });   
    	
    	btnCircle = new Button("Filled-Circle");
    	//add eraser picture
    	File f = new File(new File("src\\soundAndImageFiles\\circle.png").getAbsolutePath());          
        Image eraser = new Image(f.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv2 = new ImageView();
        iv2.setImage(eraser);
        btnCircle.setGraphic(iv2);
    	btnCircle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\circle1.WAV").getAbsolutePath()));
            }
        });    	    		    
	}
	
	public void playClip(File file) {
		try {						
			//Prepares the clip for playing
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
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
	}
	
	public void setButtons(int selectedAnswer, int correctAnswer) {
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		root.getChildren().add(buttons);
		if (selectedAnswer == TRIANGLE) {
			buttons.getChildren().add(btnTriangle);
		}
		if (selectedAnswer == SQUARE) {
			buttons.getChildren().add(btnSquare);
		}
		if (selectedAnswer == SQUIGGLE) {
			buttons.getChildren().add(btnSquiggle);
		}
		if (selectedAnswer == FILLEDCIRCLE) {
			buttons.getChildren().add(btnCircle);
		}
		
		if (correctAnswer == TRIANGLE) {
			buttons.getChildren().add(btnTriangle);
		}
		if (correctAnswer == SQUARE) {
			buttons.getChildren().add(btnSquare);
		}
		if (correctAnswer == SQUIGGLE) {
			buttons.getChildren().add(btnSquiggle);
		}
		if (correctAnswer == FILLEDCIRCLE) {
			buttons.getChildren().add(btnCircle);
		}
		
		Button okay = new Button("Okay");
		root.getChildren().add(okay);
		okay.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	me.close();
            }            	
        });
		root.setAlignment(Pos.CENTER);
	}

}

