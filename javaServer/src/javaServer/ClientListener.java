package javaServer;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import test.*;
public class ClientListener implements modelListener{

	public Draw2Model model;
	public Draw2Controller controller;
	public DataOutputStream out;
	
	public ClientListener(Draw2Model m, Draw2Controller c, DataOutputStream o) {
		model = m;
		controller = c;
		out =o;
		model.addSubscriber(this);
	}
	
	@Override
	public void modelChanged() {
		//what do i need to send to the server so that it can replicate the path i have drawn.
		//start by sending a line over
		
		
		/*
		 * startPath(double x, double y)
		 * path.setStrokeWidth(sampleLine.getStrokeWidth());
    path.setStroke(sampleLine.getStroke());
    path.getElements().add(new MoveTo(x, y));    
    modelPathsCoordinates.add(new Coordinate(x,y));    
    iModel.modelPathsTranslateByCoordinates.add(new Coordinate(0,0));    
    iModel.viewPortXYLocation.add(new Coordinate(iModel.viewPortX, iModel.viewPortY));
    lineGroup.getChildren().add(path);
    modelPaths.add(path);          
		 */
		try {
		//send the controllerstate over
		out.writeUTF(Integer.toString(controller.state));
		
		if (controller.state == controller.PAN_READY) {
			out.writeUTF(Double.toString(model.iModel.viewPortX));
			out.writeUTF(Double.toString(model.iModel.viewPortY));	
		}

										
		
			if (controller.state ==controller.READY) {
			out.writeUTF("Client model changed!");
			//is the path Alive
			if (model.path == null) {
				out.writeUTF("false");
			}else {
				out.writeUTF("true");
			}
			
			//sending the line over		
			//keep trying to figure out why this is not working
			//check the model current pathCoordinate?
			//works! 
			//now substract from the receiving end
			out.writeUTF(Double.toString(model.currentPathCoordinate.x+(model.iModel.viewPortX*7/model.radarView.width)));
			out.writeUTF(Double.toString(model.currentPathCoordinate.y+(model.iModel.viewPortY*7/model.radarView.height)));	
		//	System.out.println("currentpathcoordinate x "+model.currentPathCoordinate.x+" y "+model.currentPathCoordinate.y);			
		//	System.out.println("viewPortX "+model.iModel.viewPortX);
		//	System.out.println("viewPortY "+model.iModel.viewPortY);
		//	System.out.println("width "+(model.radarView.width));
		//	System.out.println("height "+(model.radarView.height));
		//	System.out.println("viewPortX/width "+(model.iModel.viewPortX*7/model.radarView.width));
		//	System.out.println("viewPortY/height "+(model.iModel.viewPortY*7/model.radarView.height));
			

			//sending the colour over
			out.writeUTF(model.sampleLine.getStroke().toString());
			//send the strokeWidth over
			out.writeUTF(Double.toString(model.sampleLine.getStrokeWidth()));
			
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	

