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
		//System.out.println("miniMap center "+p.x+" "+p.y);
		//System.out.println("viewport relative width "+iModel.viewPortWidth);
		return p;	
	}
	
	public void freezeTestOn() {
		freezeTest = true;
	}
	public void freezeTestOff() {
		freezeTest = false;
	}

}
