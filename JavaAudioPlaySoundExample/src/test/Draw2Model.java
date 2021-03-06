package test;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;




import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import javaServer.ClientListener;
import javafx.application.Platform;
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
import unusedClasses.PlayPathSound;
import unusedClasses.SoundThread;

public class Draw2Model {
	/**
	 * ArrayList of model listeners
	 * Will be notified every time the model changes
	 */
	ArrayList<modelListener> modelListeners;		
	/**
	 * a reference to the path being currently drawn by the user
	 */
    public Path path;  
    /**
     * A reference to the network path being currently drawn
     */
    public Path netWorkPath = null;
    /**
     * A reference to coordinate of the last segment location of the path being drawn
     */
    public Coordinate currentPathCoordinate;
    /**
     * Sets the initial path stroke size
     */
    public static final Double DEFAULTSTROKE = 3.0;
    /**
     * The largest allowed path stroke size
     */
    public static final Double MAXSTROKE = 30.0;
    /**
     * The smallest allowed path stroke size
     */
    public static final Double MINSTROKE = 1.0;
    /**
     * Reference to the clear button, 
     * will clear all paths and restore the canvas 
     * to its original appearance
     */
    Button btnClear;
    /**
     * Button for selection of the Pencil tool
     * Will alter the drawing sounds to match the tone of a pencil
     */
    Button btnPencil;
    /**
     * Button for selection of the Metal tool
     * Will alter the drawing sounds to have a more metallic timbre
     */
    Button btnMetal;
    /**
     * Button for selection of the Chalk tool
     * Will alter the drawing sounds to match the tone of a piece of chalk
     */
    Button btnChalk;
    /**
     * Button for selection of the Eraser tool
     * Will alter the drawing sounds to match the tone of an eraser
     */
    Button btnEraser;    
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
    /**
     * 
     */
    public String user1FreezeQuestionResult = "";
    
    public ViewPortDisplacementSound localVPDS;
    public ViewPortDisplacementSound networkVPDS;
    
    Slider strokeSlider;
    Label labelStroke;
    public final Line sampleLine; 
    final Label colorLabel;
    public Grain2Files player;
    public ArrayList<Path> modelPaths;
    public ArrayList<Coordinate> modelPathsCoordinates;
    public ArrayList<Coordinate> modelPathsTranslateByCoordinates;
    public InteractionModel iModel;   
    public Draw2Controller controller;
    drawPath dP = null;
    public ReadAndObserveStage readAndObserveTrial =null;
    public boolean soundGeneratorStart = false;
    public boolean netSoundGeneratorStart = false;
    
    //eliminate all of this
  //  Coordinate[] pathAngleCalculationCoordinates = new Coordinate[3];
   // int pathAngleCalculationCoordinatesUpdateCount = 2;
  //  public double currentPathAngle;
    
    public 	Draw2View view;
    public Draw2miniMap radarView;
    public AnInteractiveStaggeredSoundGenerator localSoundGenerator;
    public AnInteractiveStaggeredSoundGenerator networkSoundGenerator;
    
    public File selectedSoundFile;
    public File selectedImpactFile;
    public File netSelectedSoundFile;
    public File netSelectedImpactFile;
    public int currentTimbre = 1;
    public int PENCIL = 1;
    public int METAL = 2;
    public int ERASER = 3;
    public int CHALK = 4;
    
    //impact sound files
    public File pencilTap = new File(new File("src\\soundAndImageFiles\\pencilTap.WAV").getAbsolutePath());
    public File metalTap = new File(new File("src\\soundAndImageFiles\\metalTap.WAV").getAbsolutePath());
    public File chalkTap = new File(new File("src\\soundAndImageFiles\\chalkTap.WAV").getAbsolutePath());
    public File eraserTap = new File(new File("src\\soundAndImageFiles\\eraserTap.WAV").getAbsolutePath());
    
    
    public File getImpactSoundFile(int timbre) {
    	File impact;    	
    		impact = pencilTap;
    	
    	if (timbre == METAL) {
    		impact = metalTap;
    	}
    	if (timbre == ERASER) {
    		impact = eraserTap;
    	}
    	if (timbre == CHALK) {
    		impact = chalkTap;
    	}    	
    	return impact;
    }
    
    public Draw2Model() {
    	modelListeners = new ArrayList<>();
    	modelPaths = new ArrayList<>();
    	modelPathsCoordinates = new ArrayList<>();
    	modelPathsTranslateByCoordinates = new ArrayList<>();
    	path = null;
    	//lineGroup = new Group();
    	selectedSoundFile = new File(new File("src\\soundAndImageFiles\\pencilSlow.WAV").getAbsolutePath());
    	selectedImpactFile = new File(new File("src\\soundAndImageFiles\\pencilImpact.WAV").getAbsolutePath());
    	btnClear = new Button("Clear");
    	btnClear.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {  
            	modelPathsCoordinates = new ArrayList<>();  
                iModel.modelPathsTranslateByCoordinates =new ArrayList<>(); 
                iModel.viewPortXYLocation =new ArrayList<>();                
                modelPaths =new ArrayList<>();                         
                view.resetView();
                radarView.resetView();
                notifySubscribers();
            }
        });    	
    	
    	setUpStrokeObjectButtons();
    	setUpShapeLabels();
    	
    	strokeSlider = new Slider(MINSTROKE, MAXSTROKE, DEFAULTSTROKE);
    	labelStroke = new Label("Stroke Width");
    	sampleLine = new Line(0, 0, 140, 0);
        sampleLine.strokeWidthProperty().bind(strokeSlider.valueProperty());
        sampleLine.setStroke(Color.DIMGRAY);        
        colorLabel = new Label("color: dimgray");
        
        //set up for sound things
        File soundFile = new File(new File("src\\soundAndImageFiles\\pencilSlow2.WAV").getAbsolutePath());
        File soundFile2 = new File(new File("src\\soundAndImageFiles\\pencilSlow.WAV").getAbsolutePath());
        player = new Grain2Files(soundFile, soundFile2);
        setNetTimbre(PENCIL);
    }
    
    public void resetModel() {
    	modelPathsCoordinates = new ArrayList<>();  
        iModel.modelPathsTranslateByCoordinates =new ArrayList<>(); 
        iModel.viewPortXYLocation =new ArrayList<>();                
        modelPaths =new ArrayList<>();  
    }
    
    public synchronized ArrayList<Path> getModelPaths(){
    	return modelPaths;
    }
    
    public void setUpStrokeObjectButtons() {
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
            	setPencilTimbre();
            	sampleLine.setStroke(Color.DIMGRAY);  
            	strokeSlider.setValue(3);
            	File pencilTap = new File(new File("src\\soundAndImageFiles\\pencilTap2.WAV").getAbsolutePath());
            	if (controller.superState == controller.SOUNDS_LOCAL) {
            		player.playFileClip(pencilTap);
            	}            	
            	view.controller.state = view.controller.PLAY_IMPACT;
            	notifySubscribers();            	
            	if (iModel.task == iModel.TOOL_REACTION_TASK) {            		
            		String fileName = "Tool reaction task";
            		if (!iModel.Sounds) {
            			fileName += " without sounds!";
            		}
            		String userInput = getSelectedTimbreAndNetTimbreAsStrings();
            		CreateFile log = new CreateFile(userInput, fileName);
            		iModel.currentLogFileName = fileName;
            	}
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
            	setMetalTimbre();
            	sampleLine.setStroke(Color.SILVER);
            	strokeSlider.setValue(4);
            	File metalTap = new File(new File("src\\soundAndImageFiles\\metalTap.WAV").getAbsolutePath());
            	if (controller.superState == controller.SOUNDS_LOCAL) {
            		player.playFileClip(metalTap);
            	}               	
            	view.controller.state = view.controller.PLAY_IMPACT;
            	notifySubscribers();
            	if (iModel.task == iModel.TOOL_REACTION_TASK) {
            		String userInput = getSelectedTimbreAndNetTimbreAsStrings();
            		
            		String fileName = "Tool reaction task";
            		if (!iModel.Sounds) {
            			fileName += " without sounds!";
            		}
            		CreateFile log = new CreateFile(userInput, fileName);
            		iModel.currentLogFileName = fileName;
            	}
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
            	setChalkTimbre();
            	sampleLine.setStroke(Color.ANTIQUEWHITE);
            	strokeSlider.setValue(3);
            	File chalkTap = new File(new File("src\\soundAndImageFiles\\pencilTap.WAV").getAbsolutePath());
            	if (controller.superState == controller.SOUNDS_LOCAL) {
            		player.playFileClip(chalkTap);
            	}               	
            	view.controller.state = view.controller.PLAY_IMPACT;
            	notifySubscribers();
            	if (iModel.task == iModel.TOOL_REACTION_TASK) {
            		String userInput = getSelectedTimbreAndNetTimbreAsStrings();
            		String fileName = "Tool reaction task";
            		if (!iModel.Sounds) {
            			fileName += " without sounds!";
            		}
            		CreateFile log = new CreateFile(userInput, fileName);
            		iModel.currentLogFileName = fileName;
            	}
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
            	setEraserTimbre();
            	sampleLine.setStroke(Color.WHITE);
            	strokeSlider.setValue(30);
            	File eraserTap = new File(new File("src\\soundAndImageFiles\\eraserTap.WAV").getAbsolutePath());
            	if (controller.superState == controller.SOUNDS_LOCAL) {
            		player.playFileClip(eraserTap);
            	}               	
            	view.controller.state = view.controller.PLAY_IMPACT;
            	notifySubscribers();
            	if (iModel.task == iModel.TOOL_REACTION_TASK) {
            		String userInput = getSelectedTimbreAndNetTimbreAsStrings();
            		String fileName = "Tool reaction task";
            		if (!iModel.Sounds) {
            			fileName += " without sounds!";
            		}
            		CreateFile log = new CreateFile(userInput, fileName);
            		iModel.currentLogFileName = fileName;
            	}
            }
        });    	    	
    }
    
    public void setUpShapeLabels() {
        //ADD this to an HBox beside the button.
        File square = new File(new File("src\\soundAndImageFiles\\basic-square-outline.JPG").getAbsolutePath());       
        Image img = new Image(square.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv1 = new ImageView();
        iv1.setImage(img);
        this.square = new Label("", iv1);
        
        //ADD this to an HBox beside the button.
        File triangle = new File(new File("src\\soundAndImageFiles\\triangle-outline.png").getAbsolutePath());        
        Image img2 = new Image(triangle.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv2 = new ImageView();
        iv2.setImage(img2);
        this.triangle = new Label("", iv2);
        
        //ADD this to an HBox beside the button.
        File squiggle = new File(new File("src\\soundAndImageFiles\\squiggle.JPG").getAbsolutePath());       
        Image img3 = new Image(squiggle.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv3 = new ImageView();
        iv3.setImage(img3);
        this.squiggle = new Label("", iv3);
        
        //ADD this to an HBox beside the button.
        File circle = new File(new File("src\\soundAndImageFiles\\circle.png").getAbsolutePath());        
        Image img4 = new Image(circle.toURI().toString(), 20, 20, false, false);
        // simple displays ImageView the image as is
        ImageView iv4 = new ImageView();
        iv4.setImage(img4);
        this.circle = new Label("", iv4);
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
    	if (t == CHALK) {
    		setChalkTimbre();
    	}
    }
    
    public void setNetTimbre(int t) {
    	if (t == PENCIL ) {
    		netSelectedSoundFile = new File(new File("src\\soundAndImageFiles\\pencilSlow.WAV").getAbsolutePath());
    		netSelectedImpactFile = new File(new File("src\\soundAndImageFiles\\pencilImpact.WAV").getAbsolutePath());
        	notifySubscribers();    		
    	}
    	if (t == METAL) {
    		netSelectedSoundFile = new File(new File("src\\soundAndImageFiles\\metalOnWoodSlow.WAV").getAbsolutePath());
    		netSelectedImpactFile = new File(new File("src\\soundAndImageFiles\\metalImpact.WAV").getAbsolutePath()); 		
    	}
    	if (t == ERASER) {
    		netSelectedSoundFile = new File(new File("src\\soundAndImageFiles\\eraserOnPaperFast.WAV").getAbsolutePath());
    		netSelectedImpactFile = new File(new File("src\\soundAndImageFiles\\eraserImpact.WAV").getAbsolutePath());    		
    	}
    	if (t == CHALK) {
    		netSelectedSoundFile = new File(new File("src\\soundAndImageFiles\\chalk3.WAV").getAbsolutePath());
    		netSelectedImpactFile = new File(new File("src\\soundAndImageFiles\\chalkTap.WAV").getAbsolutePath());
    	}
    }
    
    public void setPencilTimbre() {
    	currentTimbre =PENCIL;
    	selectedSoundFile = new File(new File("src\\soundAndImageFiles\\pencilSlow.WAV").getAbsolutePath());
    	selectedImpactFile = new File(new File("src\\soundAndImageFiles\\paperImpact.WAV").getAbsolutePath());
    	notifySubscribers();
    }
    public void setMetalTimbre() {
    	currentTimbre =METAL;
    	selectedSoundFile = new File(new File("src\\soundAndImageFiles\\metalOnWoodSlow.WAV").getAbsolutePath());
    	selectedImpactFile = new File(new File("src\\soundAndImageFiles\\metalImpact2.WAV").getAbsolutePath());
    	notifySubscribers();
    }
    public void setEraserTimbre() {
    	currentTimbre =ERASER;
    	selectedSoundFile = new File(new File("src\\soundAndImageFiles\\eraserOnPaperFast.WAV").getAbsolutePath());
    	selectedImpactFile = new File(new File("src\\soundAndImageFiles\\eraserImpact2.WAV").getAbsolutePath());
    	notifySubscribers();
    }
    public void setChalkTimbre() {
    	currentTimbre =CHALK;
    	selectedSoundFile = new File(new File("src\\soundAndImageFiles\\chalk3.WAV").getAbsolutePath());
    	selectedImpactFile = new File(new File("src\\soundAndImageFiles\\chalkTap.WAV").getAbsolutePath());
    	notifySubscribers();
    }        
    
    public void addSubscriber(modelListener ml) {
    	modelListeners.add(ml);
    }
        
    public void notifySubscribers() {
    	modelListeners.forEach(a->a.modelChanged());
    	//System.out.println("count! "+count);
    	//count++;
    }
    
	//this bit should call a method in the model, just pass in the coordinates.
	public void startPath(double x1, double y1) {		
		//relativize the coordinates for storage
		double x = x1/radarView.width;
		double y = y1/radarView.height;	
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
    getModelPaths().add(path);       
	}
	
	/**
	 * initializes the pathAngleCalculationCoordinates
	 * @param x
	 * @param y
	 
	public void initializePathAngleCalculationCoordinates(double x, double y) {
		pathAngleCalculationCoordinates[0] = new Coordinate(x,y);
		pathAngleCalculationCoordinates[1] = new Coordinate(0,0);
		pathAngleCalculationCoordinates[2] = new Coordinate(0,0);
	}*/
	
	/**
	 * updates the pathAngleCalculationCoordinates
	 * @param x
	 * @param y
	 
	public void updatePathAngleCalculationCoordinates(double x, double y) {
		pathAngleCalculationCoordinates[2].x = pathAngleCalculationCoordinates[1].x;
		pathAngleCalculationCoordinates[2].y = pathAngleCalculationCoordinates[1].y;
		pathAngleCalculationCoordinates[1].x = pathAngleCalculationCoordinates[0].x;
		pathAngleCalculationCoordinates[1].y = pathAngleCalculationCoordinates[0].y;
		pathAngleCalculationCoordinates[0] = new Coordinate(x,y);
		pathAngleCalculationCoordinatesUpdateCount++;
	}*/

	
	public void strokePath(double x, double y) {
		//relativize the coordinates for strorage
		x = x/radarView.width;
		y = y/radarView.height;			
		//this could be the slow downer
		//dP = new drawPath(path, x,y);
		path.getElements().add(new LineTo(x,y));
		currentPathCoordinate = new Coordinate(x,y);
		//updatePathAngleCalculationCoordinates(x,y);
		//record 3 previous coordinates of the current path elements
		//need to initialize the array[3], in the startpath function
		//need to update the array every time this function is called
		//if there are at least 3 'new' coordinates, 		
	//	if (pathAngleCalculationCoordinatesUpdateCount>2) {
	//		currentPathAngle =calculatePathCornerStatus();
	//	}				
		//pathActivityListener.modelChanged();
		
		//make this addToPath
		//so that there is less drawing to do?
		notifySubscribers();
		//view.addToPath();
	}
	
	/**
	 * calculates if the current path has cut a corner based on its 3 previous coordinates
	 * 
	 
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
	}*/
	
	
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
	 
	public void playPathSound(ArrayList<Coordinate> v, double d, ArrayList<Coordinate> mouseCoordinates) {
		ArrayList<Float> panValues = calculateVolumeValues(v);		
		//SoundThread t = new SoundThread("sound",player,d,v,panValues);
		//t.start();
	}*/
	
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
	 * 
	 * @param velocity
	 * @param mouseCoordinate
	 * @param clipDuration
	 * @param clipStaggerIncrement
	 */
	public void playLocalPathInteractively(double velocity, Coordinate mouseCoordinate) {
		float panValue = doNotCalculatePanValue(mouseCoordinate); 	
		if (localSoundGenerator != null) {
			stopLocalSoundGenerator();
		}		
		 double clipStaggerIncrement;
		 double clipDuration;		
		 clipStaggerIncrement = this.calculateStaggerIncrement(selectedSoundFile);
		 clipDuration = player.fileLength(selectedSoundFile)*1000;		
		 
		//AnInteractiveStaggeredThread t = new AnInteractiveStaggeredThread("staggeredThread",velocity,panValue, pathAngle, clipDuration);		
    	localSoundGenerator = new AnInteractiveStaggeredSoundGenerator("localStaggeredThread",
    	velocity,panValue, clipDuration,clipStaggerIncrement,selectedSoundFile,selectedImpactFile);    	
    	//localSoundGenerator.start();		
		//use netSelectedSoundFile and netSelectedImpactFile?
	}
	
	/**
	 * 
	 * @param velocity
	 * @param mouseCoordinate
	 * @param clipDuration
	 * @param clipStaggerIncrement
	 */
	public void playNetworkPathInteractively(double velocity, Coordinate mouseCoordinate
			) {
		float panValue = doNotCalculatePanValue(mouseCoordinate); 	
		if (networkSoundGenerator != null) {
			stopNetworkSoundGenerator();
		}		
		 double clipStaggerIncrement;
		 double clipDuration;
		 clipStaggerIncrement = this.calculateStaggerIncrement(netSelectedSoundFile);
		 clipDuration = player.fileLength(netSelectedSoundFile)*1000;

		//AnInteractiveStaggeredThread t = new AnInteractiveStaggeredThread("staggeredThread",velocity,panValue, pathAngle, clipDuration);		

		networkSoundGenerator = new AnInteractiveStaggeredSoundGenerator("networkStaggeredThread",
				velocity,panValue, clipDuration,clipStaggerIncrement,netSelectedSoundFile,netSelectedImpactFile);
    	
		//networkSoundGenerator.start();		
		//use netSelectedSoundFile and netSelectedImpactFile?
	}
	
	public void stopLocalSoundGenerator() {
		localSoundGenerator.setMouseReleased(true);		
		stopLocalDrawingSoundGenerator();
		soundGeneratorStart = false;
	}
	
	public void stopNetworkSoundGenerator() {
		networkSoundGenerator.setMouseReleased(true);		
		stopNetworkDrawingSoundGenerator();
		netSoundGeneratorStart = false;
	}
	
	public void updateLocalSoundGeneratorVelocity(double v) {
		localSoundGenerator.setVelocity(v);
	}
	
	public void updateNetworkSoundGeneratorVelocity(double v) {
		networkSoundGenerator.setVelocity(v);
	}
	
	public void updateLocalSoundGeneratorPanValue(Coordinate mouseCoordinate) {
		//float panValue = calculatePanValue(mouseCoordinate);
		float panValue = doNotCalculatePanValue(mouseCoordinate);
		localSoundGenerator.setPanValue(panValue);
	}
	
	public void updateNetworkSoundGeneratorPanValue(Coordinate mouseCoordinate) {
		//float panValue = calculatePanValue(mouseCoordinate);
		float panValue = doNotCalculatePanValue(mouseCoordinate);
		networkSoundGenerator.setPanValue(panValue);
	}		
	
	public void play() {
		File f = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\strokeChange.WAV");
		//player.playFor(f, 0.5);
	}
	
	public void playStroke(long time, ArrayList<Double> velocities) {
		File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");
		File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
	//	ArrayList<FileAndDuration> filesAndDurations = new ArrayList<>();		
		//filesAndDurations =player.determineStrokesAndSustain(f1, f2, time, velocities);
	//	filesAndDurations.forEach(f ->{
		///	//player.playFor(f.file, f.duration/1000);
		//	System.out.println("file "+ f.file.getName());
		//	System.out.println("duration "+f.duration);			
	//	});
		//System.out.println("total time = "+time);
	}

	//do not erase
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
		location.x = location.x *radarView.width;
		//System.out.println("view width "+view.width);
		float panValue = 0;
			if (location.x < (view.width/2)) {
				panValue = (float) ((-1) + location.x/(view.width/2));
			}
			if (location.x >=(view.width/2) && location.x < (view.width)) {
				panValue = (float) ((location.x -(view.width/2))/(view.width/2));
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
	 
	public ArrayList<Float> calculateVolumeValues(ArrayList<Coordinate> velocities){
		ArrayList<Float> panValues = new ArrayList<>();
		//for each velocity
		//if it is .y==3
		//silence -80
		//else normal volume
		//eventually try to make it quieter as it slows down.
		velocities.forEach(a -> {
						
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
	}*/
	public Coordinate netPathCoordinate;
	public Coordinate netPathTranslateByCoordinate;
	public Coordinate netPathViewPortXYLocation;
	
	public void createNewPathFromNetwork(double[] points, String pathPaint) {
		netWorkPath = new Path();   
		netWorkPath.setSmooth(true);
		netWorkPath.setStrokeWidth(points[2]);
		netWorkPath.setStroke(Paint.valueOf(pathPaint));
		//for logging
		CreateFile log = new CreateFile("Network drawing action begun at"
    			+" x = "+points[0]+" y = "+points[1], iModel.currentLogFileName);		
		netWorkPath.getElements().add(new MoveTo(points[0], points[1]));
		netWorkPath.getElements().add(new LineTo(points[0], points[1]));
	    //path.getElements().add(new LineTo(points[2], points[3]));
		synchronized (getModelPaths()){
			if ((getModelPaths().size() > 1)){
				//make all model path things as 2nd last 	
			    modelPathsCoordinates.add(new Coordinate(points[0], points[1])); 
			    modelPathsCoordinates.add(modelPathsCoordinates.get(modelPathsCoordinates.size()-2));
			    modelPathsCoordinates.remove(modelPathsCoordinates.size()-3);
			    netPathCoordinate = modelPathsCoordinates.get(modelPathsCoordinates.size()-1);
			    
			    iModel.modelPathsTranslateByCoordinates.add(new Coordinate(0,0));  
			    iModel.modelPathsTranslateByCoordinates.add(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-2));
			    iModel.modelPathsTranslateByCoordinates.remove(iModel.modelPathsTranslateByCoordinates.size()-3);
			    
			    netPathTranslateByCoordinate = iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1);
			    iModel.viewPortXYLocation.add(new Coordinate(iModel.viewPortX, iModel.viewPortY));
			    iModel.viewPortXYLocation.add(iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-2));
			    iModel.viewPortXYLocation.remove(iModel.viewPortXYLocation.size()-3);
			    
			    netPathViewPortXYLocation = iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1);	    
			   
			    	  getModelPaths().add(netWorkPath);
			    	  getModelPaths().add(getModelPaths().get(getModelPaths().size()-2));
			    	  getModelPaths().remove(getModelPaths().size()-3);
			    	   	    
			}else {
			//make all model path things as 2nd last 	
	    modelPathsCoordinates.add(new Coordinate(points[0], points[1])); 
	   // modelPathsCoordinates.add(modelPathsCoordinates.get(modelPathsCoordinates.size()-2));	    
	    netPathCoordinate = modelPathsCoordinates.get(modelPathsCoordinates.size()-1);
	    
	    iModel.modelPathsTranslateByCoordinates.add(new Coordinate(0,0));  
	    //iModel.modelPathsTranslateByCoordinates.add(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-2));
	    
	    netPathTranslateByCoordinate = iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1);
	    iModel.viewPortXYLocation.add(new Coordinate(iModel.viewPortX, iModel.viewPortY));
	    //iModel.viewPortXYLocation.add(iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-2));
	    netPathViewPortXYLocation = iModel.viewPortXYLocation.get(iModel.viewPortXYLocation.size()-1);	    
	    
	    	  getModelPaths().add(netWorkPath);
	    	 // getModelPaths().add(getModelPaths().get(getModelPaths().size()-2));
	    	   	    
		}
		}
		
		
	    //notifySubscribers();
	    //replace with drawNetPath!
	    view.drawNetPath();
	    radarView.drawNetPath();
	    if (iModel.freezeTest) {
	    	radarView.paintOverPaths();
	    }
	}
	
	public void updateNewPathFromNetwork(double[] points) {
		netWorkPath.getElements().add(new LineTo(points[0], points[1]));	
		//replace with drawNetPath!
	    view.drawNetPath();
	    radarView.drawNetPath();
	    
	}
	
	public synchronized Path getNetWorkPath() {
		return netWorkPath;
	}
	
	public void setModelView(Draw2View v) {
		view = v;
	}
	public void setModelRadarView(Draw2miniMap v) {
		radarView = v;
	}
	
	public void beginLocalViewPortMovementSound() { 
		localVPDS = new ViewPortDisplacementSound();
		localVPDS.start();
	}
	
	public void beginNetworkViewPortMovementSound() { 
		networkVPDS = new ViewPortDisplacementSound();
		networkVPDS.start();
	}	
	
	public void stopLocalVPDS() {
		localVPDS.updateDisplacementProgress();		
		localVPDS = null;
	}
	
	public void stopNetworkVPDS() {
		networkVPDS.updateDisplacementProgress();		
		networkVPDS = null;
	}
	//public void updateVPDSGeneratorVelocity(double velocity) {
	//	VPDS.updateVelocity(velocity);
	//}
	
	public void updateLocalVPDSGeneratorLocation(Coordinate vp) {
		localVPDS.updateLocation(vp);
	}
	
	public void updateNetworkVPDSGeneratorLocation(Coordinate vp) {
		networkVPDS.updateLocation(vp);
	}
	
	public void stopLocalDrawingSoundGenerator() {
		localSoundGenerator.closeSoundGenerator();		
	}
	
	public void stopNetworkDrawingSoundGenerator() {
		networkSoundGenerator.closeSoundGenerator();		
		
	}
	
	public ReadAndObserveInstructionStage instructions =null;
	
	public void launchReadAndObserverInstructionsStage(InteractionModel imodel) {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		    	if (instructions == null) {
		    				instructions = new ReadAndObserveInstructionStage(imodel);	
		    	}		
		    }
		});
	}
	
	public void closeInstructions() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
				instructions.close();
				instructions = null;
		    }
		});
	}
	
	public void hideInstructions() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
				instructions.hide();				
		    }
		});
	}
	
	//should happen when user2 is promted for a freeze test questionaire
	public void updateInstructionsStage() {
		//get bullet questionaire to query about which shape was drawn
		instructions.showShapeQuestions(this);
	}
	
	public void showPauseStage() {
		instructions.pauseForQuiz();		
	}
	
	public void createFileForFreezeTest() {
		String fileName = "FreezeTest shape claim";
		if (!iModel.Sounds) {
			fileName += " without sounds!";
		}
		CreateFile x = new CreateFile(user1FreezeQuestionResult, fileName);
		iModel.currentLogFileName = fileName;
		
		user1FreezeQuestionResult = "";
	}
	
	
	public TaskSelectionStage menu = null; 
	
	public void startTaskStage(Draw2Controller c){
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		    	menu = new TaskSelectionStage(c);
		    }
		});
	}
	
	public void showTaskStage() {
		menu.show();
	}
	
	
	public FreezeTestInstructions freezeTestInstructionsStage =null;
	
	public void launchFreezeTestIntructions(Draw2Controller c) {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		    	if (freezeTestInstructionsStage != null) {
		    		freezeTestInstructionsStage = null;
		    	}
		    	freezeTestInstructionsStage= new FreezeTestInstructions(c);
		    	//freezeTestInstructionsStage.show();
				if (!iModel.Sounds) {
					new TaskWithoutSoundStage(controller);
				}
		    }
		});
		
	}
	public void setController(Draw2Controller c) {
		controller =c;
	}
	
	private String getSelectedTimbreAndNetTimbreAsStrings(){		
		String result = "User was using "+selectedSoundFile.toString()+
				"\n"+"Partner was using "+netSelectedSoundFile.toString();			
		return result;
	}
	
	public void logPartnerImpact() {
		if (iModel.task == iModel.TOOL_REACTION_TASK) {
		String userInput = "Partner just selected "+netSelectedSoundFile.toString();
		String fileName = "Tool reaction task";
		if (!iModel.Sounds) {
			fileName += " without sounds!";
		}
		CreateFile log = new CreateFile(userInput, fileName);
		iModel.currentLogFileName = fileName;
	}
	}
	   /**
	    * will calculate the stagger increment for a submitted file 
	    */
	   public double calculateStaggerIncrement(File f) {
		   double clipDuration = player.fileLength(f)*1000;
			double clipOverlapDuration = 0.8;//0.865;
			return (clipDuration)-(clipDuration*clipOverlapDuration);			
	   }
	   public double calculateClipDuration() {		   
		   return player.fileLength(selectedSoundFile)*1000;
	   }
	   public void launchDemoSequence() {
		   SoundDemo x = new SoundDemo(this);	
		  
		   
	   }
	
}
