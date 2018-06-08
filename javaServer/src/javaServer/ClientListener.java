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
	public int count =0;
	
	public ClientListener(Draw2Model m, Draw2Controller c, DataOutputStream o) {
		model = m;
		controller = c;
		out =o;
		model.addSubscriber(this);
	}
	
	@Override
	public void modelChanged() {
		try {
		//send the controller state over
		
		out.writeUTF(Integer.toString(controller.state));		
		System.out.println("msgs sent "+count++);
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
			System.out.println("msgs sent "+count++);
			//is the path Alive, add on to an existing path or create a new path
			if (model.path == null) {
				out.writeUTF("false");
			}else {
				out.writeUTF("true");
			}			
			System.out.println("msgs sent "+count++);
			//sending the line over		
			out.writeUTF(Double.toString(model.currentPathCoordinate.x+(model.iModel.viewPortX*7/model.radarView.width)));
			System.out.println("msgs sent "+count++);
			out.writeUTF(Double.toString(model.currentPathCoordinate.y+(model.iModel.viewPortY*7/model.radarView.height)));				
			System.out.println("msgs sent "+count++);
			//sending the colour over
			out.writeUTF(model.sampleLine.getStroke().toString());
			System.out.println("msgs sent "+count++);
			//send the strokeWidth over
			out.writeUTF(Double.toString(model.sampleLine.getStrokeWidth()));
			System.out.println("msgs sent "+count++);
			
			//sending the sound over
			//velocity
			out.writeUTF(Double.toString(controller.soundVelocityThread.getVelocity()));
			System.out.println("msgs sent "+count++);
			//mouseCoordinates
			out.writeUTF(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).x));
			System.out.println("msgs sent "+count++);
			out.writeUTF(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).y));
			System.out.println("msgs sent "+count++);
			//angle
			out.writeUTF(Double.toString(model.currentPathAngle));
			System.out.println("msgs sent "+count++);
			//clipDuration
			out.writeUTF(Double.toString(controller.clipDuration));
			System.out.println("msgs sent "+count++);
			//clipStaggerIncrement
			out.writeUTF(Double.toString(controller.clipStaggerIncrement));	
			System.out.println("msgs sent "+count++);
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	

