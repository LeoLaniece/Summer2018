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

public class CorrectionStage extends Stage{
	
	private Stage me;
	private VBox root;
	private Text instructions;
	private StrokePlayer strokePlayer;
	private ArrayList<File> toolClips;
	private ArrayList<Integer> toolClipsID;
	public int PENCIL = 0;
	public int METAL = 1;
	public int CHALK = 2;
	public int ERASER = 3;
	public int answer = -1;
	public Button btnPencil;
	public Button btnMetal;
	public Button btnChalk;
	public Button btnEraser;
	
	
	
	public CorrectionStage(int selectedAnswer, int correctAnswer){		
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
		btnPencil = new Button("Pencil");
    	//add pencil picture
        File pencil = new File(new File("src\\soundAndImageFiles\\pencil.png").getAbsolutePath());       
        Image image = new Image(pencil.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        btnPencil.setGraphic(iv1);                
    	btnPencil.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\pencilSelectionAndStroke.WAV").getAbsolutePath()));
            }
        });    	
    	
    	
    	btnMetal = new Button("Nail");
    	//add metal picture    	
    	File f2 = new File(new File("src\\soundAndImageFiles\\nail.png").getAbsolutePath());        
        Image nail = new Image(f2.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv3 = new ImageView();
        iv3.setImage(nail);
        btnMetal.setGraphic(iv3);
    	btnMetal.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\metalSelectionAndStroke.WAV").getAbsolutePath()));
            }
        });    	
    	btnChalk = new Button("Chalk");
    	//add eraser picture
    	File c = new File(new File("src\\soundAndImageFiles\\chalk.png").getAbsolutePath());        
        Image chalk = new Image(c.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv4 = new ImageView();
        iv4.setImage(chalk);
        btnChalk.setGraphic(iv4);
    	btnChalk.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\chalkSelectionAndStroke.WAV").getAbsolutePath()));
            }
        });    	
    	btnEraser = new Button("Eraser");
    	//add eraser picture
    	File f = new File(new File("src\\soundAndImageFiles\\eraser.png").getAbsolutePath());       
        Image eraser = new Image(f.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv2 = new ImageView();
        iv2.setImage(eraser);
        btnEraser.setGraphic(iv2);
    	btnEraser.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\eraserSelectionAndStroke.WAV").getAbsolutePath()));
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
		if (selectedAnswer == PENCIL) {
			buttons.getChildren().add(btnPencil);
		}
		if (selectedAnswer == METAL) {
			buttons.getChildren().add(btnMetal);
		}
		if (selectedAnswer == CHALK) {
			buttons.getChildren().add(btnChalk);
		}
		if (selectedAnswer == ERASER) {
			buttons.getChildren().add(btnEraser);
		}
		
		if (correctAnswer == PENCIL) {
			buttons.getChildren().add(btnPencil);
		}
		if (correctAnswer == METAL) {
			buttons.getChildren().add(btnMetal);
		}
		if (correctAnswer == CHALK) {
			buttons.getChildren().add(btnChalk);
		}
		if (correctAnswer == ERASER) {
			buttons.getChildren().add(btnEraser);
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
