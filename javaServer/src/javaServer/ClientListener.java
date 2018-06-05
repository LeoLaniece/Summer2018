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
		try {
		//send the controller state over
		out.writeUTF(Integer.toString(controller.state));		
		//if user is panning the background, send viewPortCoordinates over
		if (controller.state == controller.PAN_READY) {
			out.writeUTF(Double.toString(model.iModel.viewPortX));
			out.writeUTF(Double.toString(model.iModel.viewPortY));	
		}	
		
		//if the user is ready to draw a path
		//send over all the path drawing info
		//send over the path sound info
			if (controller.state ==controller.READY) {
			out.writeUTF("Client model changed!");
			//is the path Alive
			if (model.path == null) {
				out.writeUTF("false");
			}else {
				out.writeUTF("true");
			}
			
			//sending the line over		
			out.writeUTF(Double.toString(model.currentPathCoordinate.x+(model.iModel.viewPortX*7/model.radarView.width)));
			out.writeUTF(Double.toString(model.currentPathCoordinate.y+(model.iModel.viewPortY*7/model.radarView.height)));				

			//sending the colour over
			out.writeUTF(model.sampleLine.getStroke().toString());
			//send the strokeWidth over
			out.writeUTF(Double.toString(model.sampleLine.getStrokeWidth()));
			
			//sending the sound over
			//velocity
			out.writeUTF(Double.toString(controller.soundVelocityThread.getVelocity()));
			//mouseCoordinates
			out.writeUTF(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).x));
			out.writeUTF(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).y));
			//angle
			out.writeUTF(Double.toString(model.currentPathAngle));
			//clipDuration
			out.writeUTF(Double.toString(controller.clipDuration));
			//clipStaggerIncrement
			out.writeUTF(Double.toString(controller.clipStaggerIncrement));
			
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	

