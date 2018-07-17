package test;

import java.util.ArrayList;

public class InteractionModel {
	
	//needs access to the model
	//needs to be notified by the view and the controller
	//needs to store:
	//path translate by coordinates
	//will store the viewPort
	Draw2Model model;
	public ArrayList<Coordinate> modelPathsTranslateByCoordinates;
	public double viewPortX, viewPortY, viewPortHeight, viewPortWidth;
	public ArrayList<Coordinate> viewPortXYLocation;
	public Draw2View view;
	public boolean freezeTest = false;
	public int LOCATION_IDENTIFICATION_TASK = 0;
	public int TOOL_IDENTIFICATION_TASK =1;
	public int ACTIVITY_IDENTIFICATION_TASK = 2;
	public int REAL_FREEZE_TEST = 3;
	public int SHAPE_DETECTION_TASK = 4;
	public int TOOL_REACTION_TASK = 5;
	public int READ_AND_OBSERVE = 6;
	
	public boolean logTaskSoundStatus = true;
	public int task = -1;
	public boolean noSounds = false;
	public String currentLogFileName = "";
	
	public void noSoundsTrue() {
		noSounds = true;
		logTaskSoundStatus = false;
	}
	public void noSoundsFalse() {
		noSounds = false;
		logTaskSoundStatus = true;
	}
	
	//public double normalizedViewPortX, normalizedViewPortY;
	
	public InteractionModel(Draw2Model m, Draw2View view) {
		model = m;
		modelPathsTranslateByCoordinates = new ArrayList<>();
		viewPortX = 0;
		viewPortY = 0;
		viewPortXYLocation = new ArrayList<>();
		this.view = view;
		
		//how to make the view port relative to the workspace
		//workspace = 1000 by 1000
		//view = 800 by 300
		//scale = x/7
		
		viewPortHeight = view.height/7;
		viewPortWidth = view.width/7;
	}	
	public double getNormalizedViewPortX() {
		return viewPortX/view.radarView.width;
	}
	public double getNormalizedViewPortY() {
		return viewPortY/view.radarView.height;
	}
	public Coordinate calculateViewPortCenter() {		 		
	//	System.out.println("viewport X   Y "+(viewPortX+viewPortWidth)*7/view.radarView.width+" "
	//+(viewPortY+viewPortHeight)*7/view.radarView.height);
		Coordinate p =new Coordinate((viewPortX+viewPortWidth/2)*7/view.radarView.width, 
				(viewPortY+viewPortHeight/2)*7/view.radarView.height);
	
		return p;	
	}
	
	public void freezeTestOn() {
		freezeTest = true;
	}
	public void freezeTestOff() {
		freezeTest = false;
	}

}
