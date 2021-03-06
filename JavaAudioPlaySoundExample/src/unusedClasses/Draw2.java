package unusedClasses;



import java.lang.reflect.Field;







import java.lang.reflect.Modifier;

import javafx.application.Application;

import javafx.event.ActionEvent;

import javafx.event.EventHandler;

import javafx.geometry.Pos;

import javafx.scene.Cursor;

import javafx.scene.Group;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.control.Slider;

import javafx.scene.control.Tooltip;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.FlowPane;

import javafx.scene.layout.HBox;

import javafx.scene.layout.StackPane;

import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.shape.Line;

import javafx.scene.shape.LineTo;

import javafx.scene.shape.MoveTo;

import javafx.scene.shape.Path;

import javafx.scene.shape.Rectangle;

import javafx.stage.Stage;
//Leo's imports
import sun.audio.*;
import test.Grain2Files;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.*;

import javax.management.timer.Timer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


/*Project requirements notes
 * minimap audio, audio alone?
calculate benefits of each
javafx base networking- between 2 different windows/machines 
send parameters and each machine creates the audio
stream of velocities and locations?

stereo paning- move sound left and right
who in the work space, where are they what are they doing?
distance-volume/panning
 * 
 * idea's for sound incorporated into this project
 * 
 * -make it a supersized workspace
 * -have 2 people complete a maze challenge in situations where they can see each other and were they cannot see each other :)
 * 
 * -change timbre based on the colour(material) selected
 * -have a shape drag and drop method (with pickup and drop sounds)
 * -have communication over network server
 * 
 * //new idea, for interactive sounds, provide a lag.
//calculate length and speed of stroke, then generate the sound.
 * 
 */

//found at https://gist.github.com/jpt1122/6592882
public class Draw2 extends Application {
	double x;
	double y;
	long time; 

	AudioStream audioStream;

    private Path path;

    private Group lineGroup;

    private static final Double DEFAULTSTROKE = 3.0;

    private static final Double MAXSTROKE = 30.0;

    private static final Double MINSTROKE = 1.0;



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Drawing Tool");
        //a type of layout
        final Group root = new Group();
        Scene scene = new Scene(root, 600, 400);
        
        //my own test for a bit
        Canvas can=new Canvas(600,400);
        GraphicsContext gc = can.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, 600, 400);        
        
		gc.beginPath();						
			gc.moveTo(0, 0);
			gc.setFill(Color.BLACK);
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(8);
			gc.lineTo(300,300);		
		
		gc.stroke();
		gc.closePath();
        
        root.getChildren().add(can);
        
        
        //should be in the model?
        //LEO'S SOUND CODE
        //test for GRAIN2FILES
         File soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
         File soundFile2 = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
         Grain2Files player = new Grain2Files(soundFile, soundFile2);
         player.changeFrequency(0);
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
            
       
        

        
        



         
        // A group to hold all the drawn path elements
        lineGroup = new Group();


        //model should get a reference to these items 
        // Build the slider, label, and button and their VBox layout container
        
        Button btnClear = new Button();

        btnClear.setText("Clear");

        btnClear.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                lineGroup.getChildren().removeAll(lineGroup.getChildren());                
                //play eraser sound
               clip2.start();
                clip2.setMicrosecondPosition(0);                              
            }
        });


        //model should get a reference to these
        Slider strokeSlider = new Slider(MINSTROKE, MAXSTROKE, DEFAULTSTROKE);
        Label labelStroke = new Label("Stroke Width");
        VBox utilBox = new VBox(10);
        utilBox.setAlignment(Pos.TOP_CENTER);
        utilBox.getChildren().addAll(btnClear, labelStroke, strokeSlider);



        // Build the sample line and its layout container
        // should be in the model, represented in the view
        final Line sampleLine = new Line(0, 0, 140, 0);
        sampleLine.strokeWidthProperty().bind(strokeSlider.valueProperty());
        sampleLine.setStroke(Color.BLUE);
        
        StackPane stackpane = new StackPane();

        stackpane.setPrefHeight(MAXSTROKE);

        stackpane.setPrefWidth(sampleLine.getEndX() + MAXSTROKE);

        stackpane.setAlignment(Pos.CENTER);

        stackpane.getChildren().add(sampleLine);



        // Build the color label and its layout container

        final Label colorLabel = new Label("color: blue");

        StackPane stackpane2 = new StackPane();

        stackpane2.setPrefHeight(MAXSTROKE);

        stackpane2.setPrefWidth(sampleLine.getEndX() + MAXSTROKE);

        stackpane2.setAlignment(Pos.CENTER_LEFT);

        stackpane2.getChildren().add(colorLabel);



        // Build the HBox layout container for the two stackpane's

        HBox lineBox = new HBox(20);

        lineBox.setAlignment(Pos.CENTER);

        lineBox.getChildren().addAll(stackpane, stackpane2);



        // Build the color picker and use a flowpane

        FlowPane flow = new FlowPane();

        flow.setVgap(2);

        flow.setHgap(2);

        flow.setPrefWrapLength(400);

        // Get the declared fields for the Color class

        Field[] colorFields = Color.class.getDeclaredFields();

        for (Field fieldname : colorFields) {

            int mods = fieldname.getModifiers();



            // Only look at the field if it's public, static, and NOT 'TRANSPARENT'

            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)

                    && !(fieldname.getName().equals("TRANSPARENT"))) {

                try {



                    Color c = Color.web(fieldname.getName());

                    // Make a rectangle with that field name's color

                    final Rectangle r = new Rectangle(15, 15, c);

                    // Configure the rectangle

                    r.setCursor(Cursor.HAND);

                    Tooltip t = new Tooltip(fieldname.getName().toLowerCase());

                    Tooltip.install(r, t);

                    r.setUserData(t.getText());

                    r.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override

                        public void handle(MouseEvent me) {

                            sampleLine.setStroke(r.getFill());

                            colorLabel.setText("color: " + ((String) r.getUserData()));

                        }

                    });

                    flow.getChildren().add(r);

                } catch (IllegalArgumentException e) {

                    // just ignore it if for some reason we can't make

                    // a color

                }



            }

        }



        // Build the layout container for the VBox utility box and the flowpane

        HBox toolBox = new HBox(10);

        toolBox.setAlignment(Pos.TOP_CENTER);

        toolBox.getChildren().addAll(utilBox, flow);


        //should be in the view
        // Build the canvas

        final Rectangle canvas = new Rectangle(scene.getWidth() - 20, scene.getHeight() - 230);

        canvas.setCursor(Cursor.CROSSHAIR);

        canvas.setFill(Color.LIGHTGRAY);
        //in the controller
        
        
        canvas.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
            @Override
            public void handle(MouseEvent me) {

            	//will be useful in mouseDragged for velocity
            	x = me.getX();
            	y = me.getY();
            	time = System.currentTimeMillis();

                path = new Path();

                path.setMouseTransparent(true);

                path.setStrokeWidth(sampleLine.getStrokeWidth());

                path.setStroke(sampleLine.getStroke());

                lineGroup.getChildren().add(path);

                path.getElements().add(new MoveTo(me.getSceneX(), me.getSceneY()));
                
                //play drawing sound
            	//clip.loop(Clip.LOOP_CONTINUOUSLY);
                
			
                
                

            }

        });


        //in the controller
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override

            public void handle(MouseEvent me) {
            	
            	//stop the sound
            	 clip.stop();
            	 clip2.stop();
            	 
            	 //calculate speed and distance of the stroke
            	double distance = 0;
             	double dx = 0;
             	double dy = 0;
             	dx = me.getX();
        		dy = me.getY();
        		distance = Math.sqrt(Math.pow((dx-x), 2)+Math.pow((dy-y), 2));
        		long strokeTime = (System.currentTimeMillis() -time);
        		System.out.println("distance = " +distance);
        		System.out.println("strokeTime = " +strokeTime);
        		double strokeVelocity = ((distance*1000)/strokeTime)/1000;
        		System.out.println("stroke velocity (pixels per millisecond= " +strokeVelocity);
        		//use the stroke velocity to create the intensity of the sound, less than 1pixel a second is slow, more is fast
        		//use the strokeTime to calculate length of the sound you want to generate 
        		//implement that function for tommorow. 
            	 
        		//intensity should not be too hard, try implementing the length of the sound
        		//is there a function to determine the length in time of an audio file? yes
        		//create and array of the exact same audio file (size of array calculated by the required length)
        		//use this array to 'extend' the sound file to match the length of the stroke
        		
        		
			

           path = null;



            }

        });


        //int the controller
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override

            public void handle(MouseEvent me) {
            	
            	//CALCULATE DRAG VELOCITY
            	//every 100 ms calculate a curent mouse x mouse y
            	//also figure out distance from previous x and y
            	//if the distance is bigger than 300pixels, play a 'fast' sound
            	//else play slow sound
            	double distance = 0;
            	double dx = 0;
            	double dy = 0;
            	
            	//works but doesn't sound smooth, choppy when you have to start and restart the soundDataLine every ms. 
            	if (System.currentTimeMillis() -time >= 100) {
            		//time = System.currentTimeMillis();
            		//dx = me.getX();
            		//dy = me.getY();
            		//distance = Math.sqrt(Math.pow((dx-x), 2)+Math.pow((dy-y), 2));
            		//x = dx;
            		//y = dy;
            		//System.out.println("distance = " +distance);
            		//player.changeFrequency(distance/1000);
            		if (distance <= 100) {
            			//player.changeFrequency(0);
            		}else {
            			
            		}
                    
            		
            	}
            	//play sound synthesis
            	
            	

                // keep lines within rectangle

                if (canvas.getBoundsInLocal().contains(me.getX(), me.getY())) {

                    path.getElements().add(new LineTo(me.getSceneX(), me.getSceneY()));

                }



            }

        });



        // Build the VBox container for the lineBox, canvas, and toolBox
        VBox vb = new VBox(20);
        vb.setLayoutX(10);
        vb.setLayoutY(20);
        vb.getChildren().addAll(lineBox, canvas, toolBox);
        //root.getChildren().addAll(vb, lineGroup);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}