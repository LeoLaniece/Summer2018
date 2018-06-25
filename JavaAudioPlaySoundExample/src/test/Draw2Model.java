package test;

import java.io.File;




import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import javaServer.ClientListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Draw2Model {
	//the model listeners
	ArrayList<modelListener> modelListeners;
	Group lineGroup;
	ClientListener pathActivityListener;
	
    public Path path;  
    public Path netWorkPath = null;
    public Coordinate currentPathCoordinate;
    public static final Double DEFAULTSTROKE = 3.0;
    public static final Double MAXSTROKE = 30.0;
    public static final Double MINSTROKE = 1.0;
    Button btnClear;
    Button btnPencil;
    Button btnMetal;
    Button btnEraser;
    
    public ViewPortDisplacementSound VPDS;
    public boolean netTransaction = true;
    
    Slider strokeSlider;
    Label labelStroke;
    public final Line sampleLine; 
    final Label colorLabel;
    public Grain2Files player;
    public ArrayList<Path> modelPaths;
    public ArrayList<Coordinate> modelPathsCoordinates;
    public ArrayList<Coordinate> modelPathsTranslateByCoordinates;
    public InteractionModel iModel;
    PlayPathSound p = null;
    drawPath dP = null;
    Coordinate[] pathAngleCalculationCoordinates = new Coordinate[3];
    int pathAngleCalculationCoordinatesUpdateCount = 2;
    public double currentPathAngle;
    public 	Draw2View view;
    public Draw2miniMap radarView;
    public AnInteractiveStaggeredSoundGenerator soundGenerator;
    public int count =0;
    public File selectedSoundFile;
    File selectedImpactFile;
    public int currentTimbre = 1;
    public int PENCIL = 1;
    public int METAL = 2;
    public int ERASER = 3;
    
    public Draw2Model() {
    	modelListeners = new ArrayList<>();
    	modelPaths = new ArrayList<>();
    	modelPathsCoordinates = new ArrayList<>();
    	modelPathsTranslateByCoordinates = new ArrayList<>();
    	path = null;
    	lineGroup = new Group();
    	selectedSoundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
    	selectedImpactFile = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilImpact.WAV");
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
    	
    	setUpStrokeObjectButtons();
    	
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
    
    public synchronized ArrayList<Path> getModelPaths(){
    	return modelPaths;
    }
    
    public void setUpStrokeObjectButtons() {
    	btnPencil = new Button("Pencil");
    	btnPencil.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	setPencilTimbre();
            }
        });    	
    	btnMetal = new Button("Metal");
    	btnMetal.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {              	
            	setMetalTimbre();
            }
        });    	
    	btnEraser = new Button("Eraser");
    	btnEraser.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	setEraserTimbre();
            }
        });    	
    }
    
    public void setTimbre(int t) {
    	if (t == PENCIL ) {
    		setPencilTimbre();
    	}
    	if (t == METAL) {
    		setMetalTimbre();
    	}
    	if (t == ERASER) {
    		setEraserTimbre();
    	}
    }
    
    public void setPencilTimbre() {
    	currentTimbre =PENCIL;
    	selectedSoundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
    	selectedImpactFile = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilImpact.WAV");
    	notifySubscribers();
    }
    public void setMetalTimbre() {
    	currentTimbre =METAL;
    	selectedSoundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");
    	selectedImpactFile = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalImpact.WAV");
    	notifySubscribers();
    }
    public void setEraserTimbre() {
    	currentTimbre =ERASER;
    	selectedSoundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\eraserOnPaperFast.WAV");
    	selectedImpactFile = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\eraserImpact.WAV");
    	notifySubscribers();
    }
    
    
    
    
    public void addSubscriber(modelListener ml) {
    	modelListeners.add(ml);
    }
    
    public void setPathActivityListener(ClientListener c) {
    	pathActivityListener = c;
    }
    
    public void notifySubscribers() {
    	modelListeners.forEach(a->a.modelChanged());
    }
	//this bit should call a method in the model, just pass in the coordinates.
	public void startPath(double x1, double y1) {		
		//relativize the coordinates for strorage
		double x = x1/radarView.width;
		double y = y1/radarView.height;	
		//System.out.println("radarView width "+radarView.width);
		//System.out.println("radarView h "+radarView.height);
		if (path != null) {
			pathToNull();
		}				
    path = new Path();   
    path.setSmooth(true);
    path.setStrokeWidth(sampleLine.getStrokeWidth());    
    path.setStroke(sampleLine.getStroke());    
    path.getElements().add(new MoveTo(x, y));
    path.getElements().add(new LineTo(x,y));
    modelPathsCoordinates.add(new Coordinate(x,y));    
    currentPathCoordinate = new Coordinate(x,y);
    iModel.modelPathsTranslateByCoordinates.add(new Coordinate(0,0));    
    iModel.viewPortXYLocation.add(new Coordinate(iModel.viewPortX, iModel.viewPortY));
    initializePathAngleCalculationCoordinates(x,y);
    pathAngleCalculationCoordinatesUpdateCount =1;
    lineGroup.getChildren().add(path);
    getModelPaths().add(path);       
    //pathActivityListener.modelChanged();
	}
	
	/**
	 * initializes the pathAngleCalculationCoordinates
	 * @param x
	 * @param y
	 */
	public void initializePathAngleCalculationCoordinates(double x, double y) {
		pathAngleCalculationCoordinates[0] = new Coordinate(x,y);
		pathAngleCalculationCoordinates[1] = new Coordinate(0,0);
		pathAngleCalculationCoordinates[2] = new Coordinate(0,0);
	}
	
	/**
	 * updates the pathAngleCalculationCoordinates
	 * @param x
	 * @param y
	 */
	public void updatePathAngleCalculationCoordinates(double x, double y) {
		pathAngleCalculationCoordinates[2].x = pathAngleCalculationCoordinates[1].x;
		pathAngleCalculationCoordinates[2].y = pathAngleCalculationCoordinates[1].y;
		pathAngleCalculationCoordinates[1].x = pathAngleCalculationCoordinates[0].x;
		pathAngleCalculationCoordinates[1].y = pathAngleCalculationCoordinates[0].y;
		pathAngleCalculationCoordinates[0] = new Coordinate(x,y);
		pathAngleCalculationCoordinatesUpdateCount++;
	}

	
	public void strokePath(double x, double y) {
		//relativize the coordinates for strorage
		x = x/radarView.width;
		y = y/radarView.height;	
		dP = new drawPath(path, x,y);
		currentPathCoordinate = new Coordinate(x,y);
		updatePathAngleCalculationCoordinates(x,y);
		//record 3 previous coordinates of the current path elements
		//need to initialize the array[3], in the startpath function
		//need to update the array every time this function is called
		//if there are at least 3 'new' coordinates, 		
		if (pathAngleCalculationCoordinatesUpdateCount>2) {
			currentPathAngle =calculatePathCornerStatus();
		}				
		//pathActivityListener.modelChanged();
		notifySubscribers();
	}
	
	/**
	 * calculates if the current path has cut a corner based on its 3 previous coordinates
	 * 
	 */
	public double calculatePathCornerStatus() {
		//calculate the sides of the triangle
		//calculate the angles of the triangle
		//find angle at point p2.
		//double result = atan2(P3.y - P1.y, P3.x - P1.x) -
        //        atan2(P2.y - P1.y, P2.x - P1.x);
		//double result = Math.atan2(pathAngleCalculationCoordinates[2].y -pathAngleCalculationCoordinates[0].y,pathAngleCalculationCoordinates[2].x -pathAngleCalculationCoordinates[0].x) -
		//		Math.atan2(pathAngleCalculationCoordinates[1].y -pathAngleCalculationCoordinates[0].y,pathAngleCalculationCoordinates[1].x -pathAngleCalculationCoordinates[0].x);
		double da = calculateDistance(pathAngleCalculationCoordinates[0].x, pathAngleCalculationCoordinates[0].y, pathAngleCalculationCoordinates[1].x, pathAngleCalculationCoordinates[1].y);
		double db = calculateDistance(pathAngleCalculationCoordinates[1].x, pathAngleCalculationCoordinates[1].y, pathAngleCalculationCoordinates[2].x, pathAngleCalculationCoordinates[2].y);
		double dc = calculateDistance(pathAngleCalculationCoordinates[2].x, pathAngleCalculationCoordinates[2].y, pathAngleCalculationCoordinates[0].x, pathAngleCalculationCoordinates[0].y);
		double result = Math.acos((Math.pow(db, 2)+Math.pow(da, 2)-Math.pow(dc, 2))/(2*db*da));
		if (isNaN(result)) {
			result = 0;
		}
		//System.out.println("distance a = "+da);
		//System.out.println("distance b = "+db);
		//System.out.println("distance c = "+dc);
		//System.out.println("current path angle = "+Math.toDegrees(result));
		//return a value, use it in sound generator.		
		//System.out.println("path elements count "+path.getElements().size());
		//System.out.println("path angle "+pathAngle);
		return Math.toDegrees(result);
	}
	
	public boolean isNaN(double x){return x != x;}
	
	public double calculateDistance(double x, double y,double x2, double y2) {
		double result = Math.sqrt((Math.pow(x2-x, 2)+Math.pow(y2-y, 2)));
		return result;
		
	}
	
	
	public void pathToNull() {
		dP = null;
		path = null;
	//	pathActivityListener.modelChanged();
		//netWorkPath = null;
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
	
	/**
	 * play path sound Thread
	 * @param v
	 * @param d
	 * @param mouseCoordinates
	 */
	public void playPathSound(ArrayList<Coordinate> v, double d, ArrayList<Coordinate> mouseCoordinates) {
		ArrayList<Float> panValues = calculateVolumeValues(v);		
		SoundThread t = new SoundThread("sound",player,d,v,panValues);
		t.start();
	}
	
	/**
	 * play 
	 * @param duration
	 * @param velocities
	 * @param mouseCoordinates
	 */
	
	public void playStaggeredSoundThreads(double duration, ArrayList<Coordinate> velocities, ArrayList<Coordinate> mouseCoordinates) {
		//ArrayList<Float> panValues = calculatePanValues(mouseCoordinates);	
		//StaggeredSoundThread t = new StaggeredSoundThread("Stagered sound", player, duration, velocities, panValues);
		//t.start();
	}
	
	/**
	 * play the sound of the path as it is being drawn
	 * 
	 */
	public void playPathInteractively(double velocity, Coordinate mouseCoordinate
			, double clipDuration, double clipStaggerIncrement) {
		float panValue = doNotCalculatePanValue(mouseCoordinate); 	
		if (soundGenerator != null) {
			stopSoundGenerator();
		}
		//AnInteractiveStaggeredThread t = new AnInteractiveStaggeredThread("staggeredThread",velocity,panValue, pathAngle, clipDuration);		
		soundGenerator = new AnInteractiveStaggeredSoundGenerator("staggeredThread",
				velocity,panValue, clipDuration,clipStaggerIncrement,selectedSoundFile,selectedImpactFile);
		soundGenerator.start();
	}
	
	public void stopSoundGenerator() {
		soundGenerator.setMouseReleased(true);		
	}
	
	public void updateSoundGeneratorPathAngle() {		
		soundGenerator.setAngle(currentPathAngle);
	}
	
	public void updateSoundGeneratorPathAngleFromNet(double angle) {		
		soundGenerator.setAngle(angle);
	}
	
	public void updateSoundGeneratorVelocity(double v) {
		soundGenerator.setVelocity(v);
	}
	
	public void updateSoundGeneratorPanValue(Coordinate mouseCoordinate) {
		//float panValue = calculatePanValue(mouseCoordinate);
		float panValue = doNotCalculatePanValue(mouseCoordinate);
		soundGenerator.setPanValue(panValue);
	}
	

	
	
	public void play() {
		File f = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\strokeChange.WAV");
		//player.playFor(f, 0.5);
	}
	
	public void playStroke(long time, ArrayList<Double> velocities) {
		File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");
		File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
		ArrayList<FileAndDuration> filesAndDurations = new ArrayList<>();		
		filesAndDurations =player.determineStrokesAndSustain(f1, f2, time, velocities);
		filesAndDurations.forEach(f ->{
			//player.playFor(f.file, f.duration/1000);
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
	} */
	
	

	
	/**
	 * play sound while drawing
	 * @param iM
	 */
	public void threadTest(double duration) {
		
	}
	
	public void setIModel(InteractionModel iM) {
		iModel = iM;
	}
	/**
	 * will calculate the appropriate panValues based on the list of coordinates of the mouse.
	 * @param location
	 * @return
	 */
	public float doNotCalculatePanValue(Coordinate location){
		//ArrayList<Float> panValues = new ArrayList<>();
		//box center is at 400
		//if x < 400 pan value should be between -1 and 0
		//if x >400 and <800 value should be between 0 and 1
		//System.out.println("location x "+location.x);
		location.x = location.x *1000;
		//System.out.println("location x "+location.x);
		float panValue = 0;
			if (location.x < 400) {
				panValue = (float) ((-1) + location.x/400f);
			}
			if (location.x >=400 && location.x < 800) {
				panValue = (float)(location.x -400)/400;
			}
			/*if (panValue > 0.3) {
				panValue = 1.0f;
			}
			if (panValue < -0.3) {
				panValue = -1.0f;
			}	*/		
			
		return panValue;
	}
	
	/**
	 * will calculate a single pan value based on the given coordinate
	 * the coordinate is expected to be the center of the other user's netViewPort
	 */
	public float calculatePanValue(Coordinate mouse) {
		//need to adjust the mouse values based on the current minimap displacement
		//if the user is drawing more than +-0.3 of the netViewPort center, the value should be -1 or 1.
		
	//	System.out.println("my minimap viewport X "+iModel.viewPortX);
		
		
		double relativeViewPortX = (iModel.viewPortX*7)/radarView.width;
		mouse.x = mouse.x +relativeViewPortX;
		
		float panValue = 0;
		if (radarView.hasNetMiniMap == true) {						
		Coordinate viewPointCenter = radarView.calculateNetViewPortCenter();	
		//System.out.println("netPortX "+viewPointCenter.x);
		//System.out.println("mouseX "+mouse.x);
		
		if (mouse.x < viewPointCenter.x) {
			panValue= (float) ((-1) + mouse.x/viewPointCenter.x);
		}
		if (mouse.x >=viewPointCenter.x && mouse.x < 1.0) {
			//calculate distance from centerPoint to edge, 1-centerpoint 
			//distance from mouse to center point				
			//fix this
			//mouse x needs to be adjusted by the currrent location of the viewport!			
			panValue = (float) ((mouse.x -viewPointCenter.x)/(1-viewPointCenter.x));
			
			//System.out.println("mouse - netPortX "+(mouse.x-viewPointCenter.x));
			//System.out.println("1 -viewpointcenter "+(1-viewPointCenter.x));
			}
		/*if (panValue > 0.3) {
			panValue = 1;
		}
		if (panValue < -0.3) {
			panValue = -1;
		}*/
		
		}
		return panValue;
	}
	
	/**
	 * calculate masterVolume values
	 */
	public ArrayList<Float> calculateVolumeValues(ArrayList<Coordinate> velocities){
		ArrayList<Float> panValues = new ArrayList<>();
		//for each velocity
		//if it is .y==3
		//silence -80
		//else normal volume
		//eventually try to make it quieter as it slows down.
		velocities.forEach(a -> {
			System.out.println("velocity = "+a.x+ " DURATION = "+a.y);			
			if (a.x == 0) {				
				//normal increment is every 0.043 seconds so this/0.043
				int batches = (int) (a.y/0.043);
				for (int i = 0; i < batches;i++) {
					panValues.add(-80f);
				}						
					//System.out.println("something will be silent");
			}else {
				panValues.add(0f);
			}
		});			
		
		//issue discovered
		//silence isn't playing for long because the increment is in even amounts of time, i would need to add consequentially more 'silence' increments
		//or change the way my player plays back sounds, the former sounds simpleler
		//this was also why my thing wasn't working yesterday!.				
		return panValues;
	}
	
	public void createNewPathFromNetwork(double[] points, String pathPaint) {
		netWorkPath = new Path();   
		netWorkPath.setSmooth(true);
		netWorkPath.setStrokeWidth(points[2]);
		netWorkPath.setStroke(Paint.valueOf(pathPaint));
		netWorkPath.getElements().add(new MoveTo(points[0], points[1]));
		netWorkPath.getElements().add(new LineTo(points[0], points[1]));
	    //path.getElements().add(new LineTo(points[2], points[3]));
	    modelPathsCoordinates.add(new Coordinate(points[0], points[1]));    

	    //these are the values that are wrong?
	    iModel.modelPathsTranslateByCoordinates.add(new Coordinate(0,0));    
	    iModel.viewPortXYLocation.add(new Coordinate(iModel.viewPortX, iModel.viewPortY));
	    lineGroup.getChildren().add(netWorkPath);
	    getModelPaths().add(netWorkPath); 
	    notifySubscribers();
	}
	
	public void updateNewPathFromNetwork(double[] points) {
		netWorkPath.getElements().add(new LineTo(points[0], points[1]));		
	}
	
	public void setModelView(Draw2View v) {
		view = v;
	}
	public void setModelRadarView(Draw2miniMap v) {
		radarView = v;
	}
	
	public void beginViewPortMovementSound() { 
		VPDS = new ViewPortDisplacementSound();
		VPDS.start();
	}
	public void stopVPDS() {
		VPDS.updateDisplacementProgress();
	}
}
