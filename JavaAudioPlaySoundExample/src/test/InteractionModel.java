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

}
