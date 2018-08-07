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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CorrectionStageShapesAndTools extends Stage{
	
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
	public int PENCIL = 0;
	public int METAL = 1;
	public int CHALK = 2;
	public int ERASER = 3;
	public int answer = -1;
	public Button pencilTriangle;
	public Button pencilSquare;
	public Button pencilSquiggle;
	public Button pencilCircle;
	public Button metalTriangle;
	public Button metalSquare;
	public Button metalSquiggle;
	public Button metalCircle;
	public Button chalkTriangle;
	public Button chalkSquare;
	public Button chalkSquiggle;
	public Button chalkCircle;
	public Button eraserTriangle;
	public Button eraserSquare;
	public Button eraserSquiggle;
	public Button eraserCircle;	
	/**
     * Label containing the image of a square
     * To be aligned by the Metal tool for quick reference 
     * during the Freeze Test tasks
     */
    Label square2;
    /**
     * Label containing the image of a triangle
     * To be aligned by the Pencil tool for quick reference 
     * during the Freeze Test tasks
     */
    Label triangle2;
    /**
     * Label containing the image of a squiggle
     * To be aligned by the Chalk tool for quick reference 
     * during the Freeze Test tasks
     */
    Label squiggle2;
    /**
     * Label containing the image of a circle
     * To be aligned by the Eraser tool for quick reference 
     * during the Freeze Test tasks
     */
    Label circle2;
	/**
     * Label containing the image of a square
     * To be aligned by the Metal tool for quick reference 
     * during the Freeze Test tasks
     */
    Label square;
    /**
     * Label containing the image of a triangle
     * To be aligned by the Pencil tool for quick reference 
     * during the Freeze Test tasks
     */
    Label triangle;
    /**
     * Label containing the image of a squiggle
     * To be aligned by the Chalk tool for quick reference 
     * during the Freeze Test tasks
     */
    Label squiggle;
    /**
     * Label containing the image of a circle
     * To be aligned by the Eraser tool for quick reference 
     * during the Freeze Test tasks
     */
    Label circle;
	
	
	public CorrectionStageShapesAndTools(int[] selectedAnswer, int[] correctAnswer){		
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
		//initialize labels
		//ADD this to an HBox beside the button.
        File square = new File(new File("src\\soundAndImageFiles\\basic-square-outline.JPG").getAbsolutePath());       
        Image img = new Image(square.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv1 = new ImageView();
        iv1.setImage(img);
        this.square = new Label("", iv1);
        ImageView iv10 = new ImageView();
        iv10.setImage(img);
        this.square2 = new Label("", iv10);
        
        //ADD this to an HBox beside the button.
        File triangle = new File(new File("src\\soundAndImageFiles\\triangle-outline.png").getAbsolutePath());        
        Image img2 = new Image(triangle.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv2 = new ImageView();
        iv2.setImage(img2);
        this.triangle = new Label("", iv2);
        ImageView iv20 = new ImageView();
        iv20.setImage(img2);
        this.triangle2 = new Label("", iv20);
        
        //ADD this to an HBox beside the button.
        File squiggle = new File(new File("src\\soundAndImageFiles\\squiggle.JPG").getAbsolutePath());       
        Image img3 = new Image(squiggle.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv3 = new ImageView();
        iv3.setImage(img3);
        this.squiggle = new Label("", iv3);
        ImageView iv30 = new ImageView();
        iv30.setImage(img3);
        this.squiggle2 = new Label("", iv30);
        
        //ADD this to an HBox beside the button.
        File circle = new File(new File("src\\soundAndImageFiles\\circle.png").getAbsolutePath());        
        Image img4 = new Image(circle.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv4 = new ImageView();
        iv4.setImage(img4);
        this.circle = new Label("", iv4);
        ImageView iv40 = new ImageView();
        iv40.setImage(img4);
        this.circle2 = new Label("", iv40);
        
        
        
		////////////////////////////////////////////////////////////
		//initialize buttons		    	    	
    	pencilTriangle = new Button("PencilTriangle");
    	//add pencil picture
        File pencil = new File(new File("src\\soundAndImageFiles\\pencil.png").getAbsolutePath());       
        Image image = new Image(pencil.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv12 = new ImageView();
        iv12.setImage(image);
        pencilTriangle.setGraphic(iv12);                
        pencilTriangle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\triangle1.WAV").getAbsolutePath()));
            }
        });    	
    	
    	pencilSquare = new Button("PencilSquare");
    	//add pencil picture
        //File pencil = new File(new File("src\\soundAndImageFiles\\pencil.png").getAbsolutePath());       
       // Image image = new Image(pencil.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv12 = new ImageView();
        iv12.setImage(image);
        pencilSquare.setGraphic(iv12);                
        pencilSquare.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\square1.WAV").getAbsolutePath()));
            }
        }); 
        
    	pencilSquiggle = new Button("PencilSquiggle");
    	//add pencil picture
        //File pencil = new File(new File("src\\soundAndImageFiles\\pencil.png").getAbsolutePath());       
       // Image image = new Image(pencil.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv12 = new ImageView();
        iv12.setImage(image);
        pencilSquiggle.setGraphic(iv12);                
        pencilSquiggle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\squiggle1.WAV").getAbsolutePath()));
            }
        }); 
        
    	pencilCircle = new Button("PencilCircle");
    	//add pencil picture
        //File pencil = new File(new File("src\\soundAndImageFiles\\pencil.png").getAbsolutePath());       
       // Image image = new Image(pencil.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv12 = new ImageView();
        iv12.setImage(image);
        pencilCircle.setGraphic(iv12);                
        pencilCircle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\circle1.WAV").getAbsolutePath()));
            }
        });        
    	
        
    	metalTriangle = new Button("MetalTriangle");
    	//add metal picture    	
    	File f2 = new File(new File("src\\soundAndImageFiles\\nail.png").getAbsolutePath());        
        Image nail = new Image(f2.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv32 = new ImageView();
        iv32.setImage(nail);
        metalTriangle.setGraphic(iv32);
        metalTriangle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\metalTriangle.WAV").getAbsolutePath()));
            }
        });  
        
        metalSquare = new Button("MetalSquare");
        //add metal picture    	
    	//File f2 = new File(new File("src\\soundAndImageFiles\\nail.png").getAbsolutePath());        
        //Image nail = new Image(f2.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv32 = new ImageView();
        iv32.setImage(nail);
        metalSquare.setGraphic(iv32);
        metalSquare.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\metalSquare.WAV").getAbsolutePath()));
            }
        }); 
        
    	metalSquiggle = new Button("MetalSquiggle");
    	//add metal picture    	
    	//File f2 = new File(new File("src\\soundAndImageFiles\\nail.png").getAbsolutePath());        
        //Image nail = new Image(f2.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv32 = new ImageView();
        iv32.setImage(nail);
        metalSquiggle.setGraphic(iv32);
        metalSquiggle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\metalSquiggle.WAV").getAbsolutePath()));
            }
        });  
        
        metalCircle = new Button("MetalCircle");
        //add metal picture    	
    	//File f2 = new File(new File("src\\soundAndImageFiles\\nail.png").getAbsolutePath());        
        //Image nail = new Image(f2.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv32 = new ImageView();
        iv32.setImage(nail);
        metalCircle.setGraphic(iv32);
        metalCircle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	playClip(new File(new File("src\\soundAndImageFiles\\metalCircle.WAV").getAbsolutePath()));
            }
        }); 
        
        
    	chalkTriangle = new Button("ChalkTriangle");
    	//add eraser picture
    	File c = new File(new File("src\\soundAndImageFiles\\chalk.png").getAbsolutePath());        
        Image chalk = new Image(c.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv42 = new ImageView();
        iv42.setImage(chalk);
        chalkTriangle.setGraphic(iv42);
        chalkTriangle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\chalkTriangle.WAV").getAbsolutePath()));
            }
        });   
        
    	chalkSquare = new Button("ChalkSquare");
    	//add eraser picture
    	//File c = new File(new File("src\\soundAndImageFiles\\chalk.png").getAbsolutePath());        
        //Image chalk = new Image(c.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv42 = new ImageView();
        iv42.setImage(chalk);
        chalkSquare.setGraphic(iv42);
        chalkSquare.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\chalkSquare.WAV").getAbsolutePath()));
            }
        }); 
        
    	chalkSquiggle = new Button("ChalkSquiggle");
    	//add eraser picture
    	//File c = new File(new File("src\\soundAndImageFiles\\chalk.png").getAbsolutePath());        
        //Image chalk = new Image(c.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv42 = new ImageView();
        iv42.setImage(chalk);
        chalkSquiggle.setGraphic(iv42);
        chalkSquiggle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\chalkSquiggle.WAV").getAbsolutePath()));
            }
        });   
        
    	chalkCircle = new Button("ChalkCircle");
    	//add eraser picture
    	//File c = new File(new File("src\\soundAndImageFiles\\chalk.png").getAbsolutePath());        
        //Image chalk = new Image(c.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv42 = new ImageView();
        iv42.setImage(chalk);
        chalkCircle.setGraphic(iv42);
        chalkCircle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\chalkCircle.WAV").getAbsolutePath()));
            }
        });
        
        
    	eraserTriangle = new Button("EraserTriangle");
    	//add eraser picture
    	File f = new File(new File("src\\soundAndImageFiles\\eraser.png").getAbsolutePath());       
        Image eraser = new Image(f.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv22 = new ImageView();
        iv22.setImage(eraser);
        eraserTriangle.setGraphic(iv22);
        eraserTriangle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\eraserTriangle.WAV").getAbsolutePath()));
            }
        }); 
        
    	eraserSquare = new Button("EraserSquare");
    	//add eraser picture
    	//File f = new File(new File("src\\soundAndImageFiles\\eraser.png").getAbsolutePath());       
        //Image eraser = new Image(f.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv22 = new ImageView();
        iv22.setImage(eraser);
        eraserSquare.setGraphic(iv22);
        eraserSquare.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\eraserSquare.WAV").getAbsolutePath()));
            }
        });
        
    	eraserSquiggle = new Button("EraserSquiggle");
    	//add eraser picture
    	//File f = new File(new File("src\\soundAndImageFiles\\eraser.png").getAbsolutePath());       
        //Image eraser = new Image(f.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv22 = new ImageView();
        iv22.setImage(eraser);
        eraserSquiggle.setGraphic(iv22);
        eraserSquiggle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\eraserSquiggle.WAV").getAbsolutePath()));
            }
        }); 
        
    	eraserCircle = new Button("EraserCircle");
    	//add eraser picture
    	//File f = new File(new File("src\\soundAndImageFiles\\eraser.png").getAbsolutePath());       
        //Image eraser = new Image(f.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        //ImageView iv22 = new ImageView();
        iv22.setImage(eraser);
        eraserCircle.setGraphic(iv22);
        eraserCircle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	playClip(new File(new File("src\\soundAndImageFiles\\eraserCircle.WAV").getAbsolutePath()));
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
	
	public void setButtons(int[] selectedAnswer, int[] correctAnswer) {
		//show 2 buttons,
		//show selected button which plays the sounds associated
		//show correct button with sounds associated		
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		root.getChildren().add(buttons);
		
		//selected answer button
		if (selectedAnswer[0] == PENCIL) {			
			if (selectedAnswer[1] == TRIANGLE) {
				buttons.getChildren().add(pencilTriangle);
				buttons.getChildren().add(triangle);
			}
			if (selectedAnswer[1] == SQUARE) {
				buttons.getChildren().add(pencilSquare);
				buttons.getChildren().add(square);
			}
			if (selectedAnswer[1] == SQUIGGLE) {
				buttons.getChildren().add(pencilSquiggle);
				buttons.getChildren().add(squiggle);
			}
			if (selectedAnswer[1] == FILLEDCIRCLE) {
				buttons.getChildren().add(pencilCircle);
				buttons.getChildren().add(circle);
			}			
		}
		
		if (selectedAnswer[0] == METAL) {
			if (selectedAnswer[1] == TRIANGLE) {
				buttons.getChildren().add(metalTriangle);
				buttons.getChildren().add(triangle);
			}
			if (selectedAnswer[1] == SQUARE) {
				buttons.getChildren().add(metalSquare);
				buttons.getChildren().add(square);
			}
			if (selectedAnswer[1] == SQUIGGLE) {
				buttons.getChildren().add(metalSquiggle);
				buttons.getChildren().add(squiggle);
			}
			if (selectedAnswer[1] == FILLEDCIRCLE) {
				buttons.getChildren().add(metalCircle);
				buttons.getChildren().add(circle);
			}
		} 
		
		if (selectedAnswer[0] == CHALK) {
			if (selectedAnswer[1] == TRIANGLE) {
				buttons.getChildren().add(chalkTriangle);
				buttons.getChildren().add(triangle);
			}
			if (selectedAnswer[1] == SQUARE) {
				buttons.getChildren().add(chalkSquare);
				buttons.getChildren().add(square);
			}
			if (selectedAnswer[1] == SQUIGGLE) {
				buttons.getChildren().add(chalkSquiggle);
				buttons.getChildren().add(squiggle);
			}
			if (selectedAnswer[1] == FILLEDCIRCLE) {
				buttons.getChildren().add(chalkCircle);
				buttons.getChildren().add(circle);
			}
		}
		
		if (selectedAnswer[0] == ERASER) {
			if (selectedAnswer[1] == TRIANGLE) {
				buttons.getChildren().add(eraserTriangle);
				buttons.getChildren().add(triangle);
			}
			if (selectedAnswer[1] == SQUARE) {
				buttons.getChildren().add(eraserSquare);
				buttons.getChildren().add(square);
			}
			if (selectedAnswer[1] == SQUIGGLE) {
				buttons.getChildren().add(eraserSquiggle);
				buttons.getChildren().add(squiggle);
			}
			if (selectedAnswer[1] == FILLEDCIRCLE) {
				buttons.getChildren().add(eraserCircle);
				buttons.getChildren().add(circle);
			}
		}
		
		//correct answer button
		if (correctAnswer[0] == PENCIL) {			
			if (correctAnswer[1] == TRIANGLE) {
				buttons.getChildren().add(pencilTriangle);
				buttons.getChildren().add(triangle2);
			}
			if (correctAnswer[1] == SQUARE) {
				buttons.getChildren().add(pencilSquare);
				buttons.getChildren().add(square2);
			}
			if (correctAnswer[1] == SQUIGGLE) {
				buttons.getChildren().add(pencilSquiggle);
				buttons.getChildren().add(squiggle2);
			}
			if (correctAnswer[1] == FILLEDCIRCLE) {
				buttons.getChildren().add(pencilCircle);
				buttons.getChildren().add(circle2);
			}			
		}
		
		if (correctAnswer[0] == METAL) {
			if (correctAnswer[1] == TRIANGLE) {
				buttons.getChildren().add(metalTriangle);
				buttons.getChildren().add(triangle2);
			}
			if (correctAnswer[1] == SQUARE) {
				buttons.getChildren().add(metalSquare);
				buttons.getChildren().add(square2);
			}
			if (correctAnswer[1] == SQUIGGLE) {
				buttons.getChildren().add(metalSquiggle);
				buttons.getChildren().add(squiggle2);
			}
			if (correctAnswer[1] == FILLEDCIRCLE) {
				buttons.getChildren().add(metalCircle);
				buttons.getChildren().add(circle2);
			}
		} 
		
		if (correctAnswer[0] == CHALK) {
			if (correctAnswer[1] == TRIANGLE) {
				buttons.getChildren().add(chalkTriangle);
				buttons.getChildren().add(triangle2);
			}
			if (correctAnswer[1] == SQUARE) {
				buttons.getChildren().add(chalkSquare);
				buttons.getChildren().add(square2);
			}
			if (correctAnswer[1] == SQUIGGLE) {
				buttons.getChildren().add(chalkSquiggle);
				buttons.getChildren().add(squiggle2);
			}
			if (correctAnswer[1] == FILLEDCIRCLE) {
				buttons.getChildren().add(chalkCircle);
				buttons.getChildren().add(circle2);
			}
		}
		
		if (correctAnswer[0] == ERASER) {
			if (correctAnswer[1] == TRIANGLE) {
				buttons.getChildren().add(eraserTriangle);
				buttons.getChildren().add(triangle2);
			}
			if (correctAnswer[1] == SQUARE) {
				buttons.getChildren().add(eraserSquare);
				buttons.getChildren().add(square2);
			}
			if (correctAnswer[1] == SQUIGGLE) {
				buttons.getChildren().add(eraserSquiggle);
				buttons.getChildren().add(squiggle2);
			}
			if (correctAnswer[1] == FILLEDCIRCLE) {
				buttons.getChildren().add(eraserCircle);
				buttons.getChildren().add(circle2);
			}
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


