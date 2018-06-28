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
		//model.setPathActivityListener(this);
		model.addSubscriber(this);
	}
	
	@Override
	public synchronized void modelChanged() {
		try {
			//send transaction over
		//out.writeUTF(Boolean.toString(model.netTransaction));
			ArrayList<String> msg = new ArrayList<String>();
		
												
			//send the controller state over
			//out.writeUTF(Integer.toString(controller.state));
			msg.add(Integer.toString(controller.state)+"\n");
			
			if (controller.state == controller.NOTREADY) {
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.PAN_READY) {
				//out.writeUTF(Double.toString(model.iModel.viewPortX));
				msg.add(Double.toString(model.iModel.viewPortX)+"\n");
				//out.writeUTF(Double.toString(model.iModel.viewPortY));	
				msg.add(Double.toString(model.iModel.viewPortY)+"\n");
				//send single string over the net
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}
				out.writeUTF(fullmsg);
			}		
		
		if (controller.state ==controller.READY) { 
			//need to not send info too often?
			//	out.writeUTF("Server model changed!");
				msg.add("Server model changed!"+"\n");
				//is the path Alive
				//path is not null when other user is drawing on your canvas?
				if (model.path == null) {
			//		out.writeUTF("false");
					msg.add("false"+"\n");
				}else {
			//		out.writeUTF("true");
					msg.add("true"+"\n");
				}				
				
				//sending the line over			
			//	out.writeUTF(Double.toString(model.currentPathCoordinate.x+(model.iModel.viewPortX*7/model.radarView.width)));
				msg.add(Double.toString(model.currentPathCoordinate.x+(model.iModel.viewPortX*7/model.radarView.width))+"\n");
			//	out.writeUTF(Double.toString(model.currentPathCoordinate.y+(model.iModel.viewPortY*7/model.radarView.height)));	
				msg.add(Double.toString(model.currentPathCoordinate.y+(model.iModel.viewPortY*7/model.radarView.height))+"\n");
				
				//sending the colour over
			//	out.writeUTF(model.sampleLine.getStroke().toString());
				msg.add(model.sampleLine.getStroke().toString()+"\n");
				//send the strokeWidth over
			//	out.writeUTF(Double.toString(model.sampleLine.getStrokeWidth()));
				msg.add(Double.toString(model.sampleLine.getStrokeWidth())+"\n");
				
				//sending the sound over
				//velocity
			//	out.writeUTF(Double.toString(controller.soundVelocityThread.getVelocity()));
				msg.add(Double.toString(controller.soundVelocityThread.getVelocity())+"\n");
				//mouseCoordinates
			//	out.writeUTF(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).x));
				msg.add(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).x)+"\n");
			//	out.writeUTF(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).y));
				msg.add(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).y)+"\n");
				//angle
			//	out.writeUTF(Double.toString(model.currentPathAngle));
				//msg.add(Double.toString(model.currentPathAngle)+"\n");
				//clipDuration
			//	out.writeUTF(Double.toString(controller.clipDuration));
				msg.add(Double.toString(controller.clipDuration)+"\n");
				//clipStaggerIncrement
			//	out.writeUTF(Double.toString(controller.clipStaggerIncrement));
				msg.add(Double.toString(controller.clipStaggerIncrement)+"\n");
				//current timbre
				msg.add(Integer.toString(model.currentTimbre));
				
				//send one msg over
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}
				out.writeUTF(fullmsg);
		}
		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}	
		
	}
	

