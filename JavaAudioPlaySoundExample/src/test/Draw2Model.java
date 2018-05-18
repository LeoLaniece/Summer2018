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
    InteractionModel iModel;
    PlayPathSound p = null;
    drawPath dP = null;
    
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
            	modelPathsCoordinates = new ArrayList<>();  
                iModel.modelPathsTranslateByCoordinates =new ArrayList<>(); 
                iModel.viewPortXYLocation =new ArrayList<>();                
                modelPaths =new ArrayList<>();         
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
    iModel.modelPathsTranslateByCoordinates.add(new Coordinate(0,0));    
    iModel.viewPortXYLocation.add(new Coordinate(iModel.viewPortX, iModel.viewPortY));
    lineGroup.getChildren().add(path);
    modelPaths.add(path);          
	}
	

	
	public void strokePath(double x, double y) {
		dP = new drawPath(modelPaths, x,y);
		//path.getElements().add(new LineTo(x, y));
		notifySubscribers();
	}
	public void pathToNull() {
		dP = null;
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
	
	public void playPathSound(ArrayList<Double> v, double d) {
		SoundThread t = new SoundThread("sound",player,d,v);
		t.start();
	}
	
	public void play() {
		File f = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\strokeChange.WAV");
		player.playFor(f, 0.5);
	}
	
	public void playStroke(long time, ArrayList<Double> velocities) {
		File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");
		File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
		ArrayList<FileAndDuration> filesAndDurations = new ArrayList<>();		
		filesAndDurations =player.determineStrokesAndSustain(f1, f2, time, velocities);
		filesAndDurations.forEach(f ->{
			player.playFor(f.file, f.duration/1000);
			System.out.println("file "+ f.file.getName());
			System.out.println("duration "+f.duration);			
		});
		System.out.println("total time = "+time);
	}

	/**
	 * most realistic sound so far!
	 * now try to implement the velocity calculations 
	 * ideally, you would adjust the mixing of the 2 files as the sound is manufactured to capture the velocity of the moment
	 * need an array of %? need to change % when we reach certain parts of the recording, need to make sure none of the arrays run out
	 * firsts, calculate percentages -> returns percentages and for what portion of the duration it is valid
	 * use determineMixPercent, pass it into mixStreamsFor
	 * @param duration1
	 */
	public void playTest(double duration1, ArrayList<Double> velocities) {
		File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");
		File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
		//velocity = 3 so each file is 50%
		//duration is 2 seconds	
		 double duration = duration1/2000;
		//now that i have separate loop counts can actually try to do % of each file.
		//double duration = 0.5;
		int loopCount = player.calculateLoopCount(f1, duration);
		int loopCount2 = player.calculateLoopCount(f2, duration);
		AudioInputStream[] audioInputStreams = player.stretchFileFor(f1, duration);
		AudioInputStream[] audioInputStreams2 = player.stretchFileFor(f2, duration);
		ArrayList<Double> mixPercentages = player.determineMixPercentage(velocities);
		//still working on this function, right now the issue seems to be the mixing, mixing is not occuring.
		//probably involves the for loops. Try a few things
		//don't use for loops
		//switch the current state to value the second file more
		//works!!!
		player.mixStreamsFor(audioInputStreams, loopCount, audioInputStreams2, loopCount2, mixPercentages, duration*1000*2);
		//player.playStretchedFor(audioInputStreams2, loopCount2);
	}
	
	
	public void playMix(ArrayList<Double> velocities, double duration) {
		File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");
		File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
		System.out.println("duration "+duration);
		long t = System.currentTimeMillis();
		ArrayList<Double> mixPercentages = player.determineMixPercentage(velocities);
		double velocityDuration = duration/velocities.size();
		//it works! but doesn't sound quite there yet, also quite slow computationally.
		mixPercentages.forEach(p ->{
			player.playMixFor(f1, f2, p, velocityDuration);
		});
		
		System.out.println("play time = "+(System.currentTimeMillis()-t));
	}
	
	/**
	 * play sound while drawing
	 * @param iM
	 */
	public void threadTest(double duration) {
		
	}
	
	public void setIModel(InteractionModel iM) {
		iModel = iM;
	}
    

}
