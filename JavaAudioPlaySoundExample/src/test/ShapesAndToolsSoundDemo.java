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
 * 
 * this class currently executes the tool identification task
 * @author HCI Lab
 *
 */
//should be renamed to toolSounds demo
public class ShapesAndToolsSoundDemo extends Stage{
	public Draw2Model model;
	private Stage me;
	private HBox root;
	private Text instructions;
	private ToolsAndShapesStrokePlayer strokePlayer;
	private ArrayList<File> toolAndShapeClips;
	private ArrayList<Coordinate> toolAndShapeClipsID;
	//private ArrayList<Integer> shapeClipsID;
	public int TRIANGLE = 0;
	public int SQUARE = 1;
	public int SQUIGGLE = 2;
	public int FILLEDCIRCLE = 3;
	public int PENCIL = 0;
	public int METAL = 1;
	public int CHALK = 2;
	public int ERASER = 3;
	public int[] answer = new int[2];
	
	public ShapesAndToolsSoundDemo(Draw2Model m){
		answer[0]=-1;
		answer[1]=-1;
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
	    setInstructions();
	    strokePlayer = new ToolsAndShapesStrokePlayer(toolAndShapeClips, toolAndShapeClipsID, this);
	}
	
	public void setInstructions() {
		root.getChildren().clear();
		instructions = new Text("Your training task is about to begin"+"\n"+
		  	      "Listen to the drawing sound"+"\n"+"select which shape was drawn in the sound"+"\n"+
				"And with which tool the shape drawn"+"\n"+
		  	     /* "Only trace each shape once!"+"\n"+
		  	      "Work with the other user to achieve this goal"+"\n"+"\n"+
		  		  "Trace the triangles with the pencil"+"\n"+
		  		  "Trace the squares with the nail"+"\n"+
		  		  "Trace the squiggles with the chalk"+"\n"+
		  		  "Erase the circles with the eraser"+"\n"+		  
		  		  "Do not worry if you end up " +"\n"+
		  		  "tracing the wrong shape with the wrong tool"+"\n"+
		  		  "You can move around in the workspace by holding down the" +"\n"+
		  		  "SHIFT key and dragging with the mouse cursor"+"\n"+"\n"+		  	    		  
		  	    		  "When prompted to stop, please answer all questions in the pop up window"+"\n"+*/
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
		//show tools
		//after tool is selected, show shapes
		//after shape is selected correct or not?		
		root.getChildren().clear();		
		strokePlayer.playNext();
		instructions = new Text("Select which tool was utilized");				
		
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
            	answer[0] = PENCIL;
            	showShapes();
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
            	answer[0] = METAL;
            	showShapes(); 
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
            	answer[0] = CHALK;
            	showShapes();  
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
            	answer[0] = ERASER;
            	showShapes(); 
            }
        });    	    		    
    	VBox vbox = new VBox();    	
    	HBox hbox = new HBox();
    	hbox.getChildren().addAll(btnPencil,btnMetal,btnChalk,btnEraser); 
    	vbox.getChildren().addAll(instructions,hbox);  
    	vbox.setAlignment(Pos.CENTER);
    	root.getChildren().addAll(vbox); 		 			
	}
	
	public void showShapes() {
		root.getChildren().clear();		
		instructions = new Text("Select which shape was drawn");
		
		Button btnTriangle = new Button("Triangle");
    	//add pencil picture
        File triangle = new File(new File("src\\soundAndImageFiles\\triangle-outline.png").getAbsolutePath());       
        Image image = new Image(triangle.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv1 = new ImageView();
        iv1.setImage(image);
        btnTriangle.setGraphic(iv1);                
    	btnTriangle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) { 
            	answer[1] = TRIANGLE;
            	//launch incorrect answer stage 
            	//or change text to correct and add continue button
            	if (answer[0] == strokePlayer.getAnswer()[0]&&
            			answer[1] == strokePlayer.getAnswer()[1]) {
            		showCorrectMessage();
            	}else {
            		setInstructions();
            		launchCorrectionStage(answer,strokePlayer.getAnswer());            		
            	}           	            	
            }
        });    	
    	
    	
    	Button btnSquare = new Button("Square");
    	//add metal picture    	
    	File f2 = new File(new File("src\\soundAndImageFiles\\basic-square-outline.JPG").getAbsolutePath());         
        Image nail = new Image(f2.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv3 = new ImageView();
        iv3.setImage(nail);
        btnSquare.setGraphic(iv3);
    	btnSquare.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	answer[1] = SQUARE;
            	//launch incorrect answer stage 
            	//or change text to correct and add continue button
            	if (answer[0] == strokePlayer.getAnswer()[0]&&
            			answer[1] == strokePlayer.getAnswer()[1]) {
            		showCorrectMessage();
            	}else {
            		setInstructions();
            		launchCorrectionStage(answer,strokePlayer.getAnswer());            		
            	}           	            	
            }
        });    	
    	
    	Button btnSquiggle = new Button("Squiggle");    	
    	File c = new File(new File("src\\soundAndImageFiles\\squiggle.JPG").getAbsolutePath());              
        Image chalk = new Image(c.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv4 = new ImageView();
        iv4.setImage(chalk);
        btnSquiggle.setGraphic(iv4);
    	btnSquiggle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	answer[1] = SQUIGGLE;
            	//launch incorrect answer stage 
            	//or change text to correct and add continue button
            	if (answer[0] == strokePlayer.getAnswer()[0]&&
            			answer[1] == strokePlayer.getAnswer()[1]) {
            		showCorrectMessage();
            	}else {
            		setInstructions();
            		launchCorrectionStage(answer,strokePlayer.getAnswer());            		
            	}           	            	
            }
        });   
    	
    	Button btnCircle = new Button("Filled-Circle");
    	//add eraser picture
    	File f = new File(new File("src\\soundAndImageFiles\\circle.png").getAbsolutePath());          
        Image eraser = new Image(f.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv2 = new ImageView();
        iv2.setImage(eraser);
        btnCircle.setGraphic(iv2);
    	btnCircle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	answer[1] = FILLEDCIRCLE;
            	//launch incorrect answer stage 
            	//or change text to correct and add continue button
            	if (answer[0] == strokePlayer.getAnswer()[0]&&
            			answer[1] == strokePlayer.getAnswer()[1]) {
            		showCorrectMessage();
            	}else {
            		setInstructions();
            		launchCorrectionStage(answer,strokePlayer.getAnswer());            		
            	}           	            	
            }
        });    	    		    
    	VBox vbox = new VBox();    	
    	HBox hbox = new HBox();
    	hbox.getChildren().addAll(btnTriangle,btnSquare,btnSquiggle,btnCircle); 
    	vbox.getChildren().addAll(instructions,hbox);  
    	vbox.setAlignment(Pos.CENTER);
    	root.getChildren().addAll(vbox); 
	}
	
	public void showCorrectMessage() {
		root.getChildren().clear();
		instructions = new Text("Correct!");
		Button okay = new Button("Okay"); 
		root.getChildren().addAll(instructions,okay);
    	okay.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	setInstructions();
            }
        });    	    		    
	}
	
	public void initializeClips() {
		initializeToolClips();
	}
	public void initializeToolClips() {
		toolAndShapeClips = new ArrayList<>();
		toolAndShapeClipsID = new ArrayList<>();
		
		//Block 1
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\triangle1.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(PENCIL,TRIANGLE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\metalSquare.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(METAL,SQUARE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\chalkSquiggle.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(CHALK,SQUIGGLE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\eraserCircle.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(ERASER,FILLEDCIRCLE));		
		
		//Block 2
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\triangle1.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(PENCIL,TRIANGLE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\metalSquare.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(METAL,SQUARE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\chalkSquiggle.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(CHALK,SQUIGGLE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\eraserCircle.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(ERASER,FILLEDCIRCLE));	

		//Block 3
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\triangle1.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(PENCIL,TRIANGLE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\metalSquare.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(METAL,SQUARE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\chalkSquiggle.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(CHALK,SQUIGGLE));
		toolAndShapeClips.add(new File(new File("src\\soundAndImageFiles\\eraserCircle.WAV").getAbsolutePath())); 
		toolAndShapeClipsID.add(new Coordinate(ERASER,FILLEDCIRCLE));	
				
				
	}
	
	public void launchCorrectionStage(int[] selectedAnswer, int[] correctAnswer) {
		CorrectionStageShapesAndTools correctionStage = new CorrectionStageShapesAndTools(selectedAnswer, correctAnswer);
	}
	
}


