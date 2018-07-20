package test;

import javafx.application.Application;
import javafx.scene.control.ToggleButton ;
import javafx.scene.control.Toggle;
import javafx.scene.control.RadioButton ;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font ;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos ;

import java.io.File;
import java.lang.reflect.Field;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode ;
import javafx.scene.shape.LineTo; 
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Scene;
import javafx.scene.control.TextField ;
/**
 * class should execute everything that is needed for the calibration tasks
 * 
 * progress report
 * have recorded all sounds for the tasks
 * Almost ready to try out the first task
 * need to finish initializing all the tool buttons
 * need to set up system of comparison vs the sound played
 * need to set up a 'correct or not' window with an okay button
 * 
 * Do the shape identification task
 * Do the tool and shape identification task.
 * @author HCI Lab
 *
 */
public class SoundDemo extends Stage{
	private Draw2Model model;
	private Stage me;
	private HBox root;
	private StrokePlayer strokePlayer;
	private ArrayList<File> toolClips;
	private ArrayList<Integer> toolClipsID;
	public int PENCIL = 0;
	public int METAL = 1;
	public int CHALK = 2;
	public int ERASER = 3;
	
	public SoundDemo(Draw2Model m){
		model = m;
		me = this;
		initializeClips();
		setTitle("Sound Demo");
        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
        int SceneHeight = javaServer.LaunchServer.SceneHeight;
	    root = new HBox();	        
	    Scene scene = new Scene(root, SceneWidth,SceneHeight);	    
	    setScene(scene);
	    setX(0);
	    setY(0);
	    show();  
	    Text instructions = new Text("FreezeTest is about to start"+"\n"+
	  	      "Until promted to stop, please trace the all the shapes"+"\n"+"you can find in the workspace"+"\n"+
	  	      "Only trace each shape once!"+"\n"+
	  	      "Work with the other user to achieve this goal"+"\n"+"\n"+
	  		  "Trace the triangles with the pencil"+"\n"+
	  		  "Trace the squares with the nail"+"\n"+
	  		  "Trace the squiggles with the chalk"+"\n"+
	  		  "Erase the circles with the eraser"+"\n"+		  
	  		  "Do not worry if you end up " +"\n"+
	  		  "tracing the wrong shape with the wrong tool"+"\n"+
	  		  "You can move around in the workspace by holding down the" +"\n"+
	  		  "SHIFT key and dragging with the mouse cursor"+"\n"+"\n"+		  	    		  
	  	    		  "When prompted to stop, please answer all questions in the pop up window"+"\n"+
	  	    		  "please press the ready button when you are ready to begin");
	    root.getChildren().add(instructions);
	    root.setAlignment(Pos.CENTER);
	    
	    Button okay1 = new Button("Okay");
	      okay1.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {	        	      	   	        	   
	        	   launchStrokesTask();	        	   
	           }
	      });
	      root.getChildren().add(okay1);
	}
	
	public void launchStrokesTask() {
		root.getChildren().clear();
		StrokePlayer strokePlayer = new StrokePlayer(toolClips);
		strokePlayer.playNext();
		
		Button btnPencil = new Button("Pencil");
    	//add pencil picture
        File pencil = new File(new File("src\\soundAndImageFiles\\pencil.png").getAbsolutePath());       
        Image image = new Image(pencil.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        btnPencil.setGraphic(iv1);        
        
    	btnPencil.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	
            }
        });    	
    	Button btnMetal = new Button("Nail");
    	//add metal picture    	
    	File f2 = new File(new File("src\\soundAndImageFiles\\nail.png").getAbsolutePath());        
        Image nail = new Image(f2.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv3 = new ImageView();
        iv3.setImage(nail);
        btnMetal.setGraphic(iv3);
    	btnMetal.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	
            }
        });    	
    	Button btnChalk = new Button("Chalk");
    	//add eraser picture
    	File c = new File(new File("src\\soundAndImageFiles\\chalk.png").getAbsolutePath());        
        Image chalk = new Image(c.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv4 = new ImageView();
        iv4.setImage(chalk);
        btnChalk.setGraphic(iv4);
    	btnChalk.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	
            }
        });    	
    	Button btnEraser = new Button("Eraser");
    	//add eraser picture
    	File f = new File(new File("src\\soundAndImageFiles\\eraser.png").getAbsolutePath());       
        Image eraser = new Image(f.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv2 = new ImageView();
        iv2.setImage(eraser);
        btnEraser.setGraphic(iv2);
    	btnEraser.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	
            }
        });    	    	
	    
    	root.getChildren().addAll(btnPencil,btnMetal,btnChalk,btnEraser);    			
	}
	
	public void initializeClips() {
		initializeToolClips();
	}
	public void initializeToolClips() {
		toolClips = new ArrayList<>();
		toolClipsID = new ArrayList<>();
		//Block 1
		toolClips.add(new File(new File("src\\soundAndImageFiles\\pencilSelectionAndStroke.WAV").getAbsolutePath())); 
		toolClipsID.add(PENCIL);
		toolClips.add(new File(new File("src\\soundAndImageFiles\\metalSelectionAndStroke.WAV").getAbsolutePath())); 
		toolClipsID.add(METAL);
		toolClips.add(new File(new File("src\\soundAndImageFiles\\chalkSelectionAndStroke.WAV").getAbsolutePath())); 
		toolClipsID.add(CHALK);
		toolClips.add(new File(new File("src\\soundAndImageFiles\\eraserSelectionAndStroke.WAV").getAbsolutePath())); 
		toolClipsID.add(ERASER);
		//Block 2
		toolClips.add(new File(new File("src\\soundAndImageFiles\\metalSelectionAndStroke.WAV").getAbsolutePath())); 
		toolClipsID.add(METAL);
				toolClips.add(new File(new File("src\\soundAndImageFiles\\pencilSelectionAndStroke.WAV").getAbsolutePath())); 
				toolClipsID.add(PENCIL);
				toolClips.add(new File(new File("src\\soundAndImageFiles\\eraserSelectionAndStroke.WAV").getAbsolutePath())); 
				toolClipsID.add(ERASER);
				toolClips.add(new File(new File("src\\soundAndImageFiles\\chalkSelectionAndStroke.WAV").getAbsolutePath())); 
				toolClipsID.add(CHALK);

				//Block 3
				toolClips.add(new File(new File("src\\soundAndImageFiles\\pencilSelectionAndStroke.WAV").getAbsolutePath())); 
				toolClipsID.add(PENCIL);
				toolClips.add(new File(new File("src\\soundAndImageFiles\\eraserSelectionAndStroke.WAV").getAbsolutePath())); 
				toolClipsID.add(ERASER);
				toolClips.add(new File(new File("src\\soundAndImageFiles\\chalkSelectionAndStroke.WAV").getAbsolutePath())); 
				toolClipsID.add(CHALK);
				toolClips.add(new File(new File("src\\soundAndImageFiles\\metalSelectionAndStroke.WAV").getAbsolutePath())); 
				toolClipsID.add(METAL);								
	}
}
