package javaServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.shape.Path;
import test.Draw2Controller;
import test.Draw2Model;

/**
 * This class will send messages over the network whenever the server model changes
 * All messages are received by the DrawingClient class
 * @author HCI Lab
 */
public class ServerListener extends ClientListener {
	
	//to keep track of how many messages have been sent so far
	public int msgCount = 0;
	public ServerListener(Draw2Model m, Draw2Controller c, DataOutputStream o) {
		super(m, c, o);
	}
	
	@Override 
	public synchronized void modelChanged() {							
		//only send messages if the super state indicates that the network should be used!
		if (controller.superState == controller.SOUNDS_OVER_NETWORK) {
		try {
			//send transaction over in a single message
			ArrayList<String> msg = new ArrayList<String>();
			
			//send the controller state over
			msg.add(Integer.toString(controller.state)+"\n");			
			
			if (controller.state == controller.READ_AND_OBSERVE) {				
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.PAUSE_UNTIL_QUIZ_COMPLETE) {				
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.CLOSE_PROMPT_FOR_SHAPE) {				
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.PLAY_IMPACT) {	
				msg.add(Integer.toString((model.currentTimbre))+"\n");
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.READY_TO_BEGIN_TASK) {				
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.FREEZE_TEST_TASK) {				
				msg.add(Integer.toString((model.iModel.task))+"\n");
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.NOTREADY) {				
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.CLOSE_INSTRUCTIONS) {				
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			if (controller.state == controller.PROMPT_FOR_SHAPE) {			
				System.out.println("sending prompt shape");
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}				
				out.writeUTF(fullmsg);
			}
			
			
			if (controller.state == controller.PAN_READY) {
				//send current viewPort location 
				msg.add(Double.toString(model.iModel.viewPortX)+"\n");
				msg.add(Double.toString(model.iModel.viewPortY)+"\n");
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}
				out.writeUTF(fullmsg);
				//uncomment to keep track of the total number of messages sent
				//msgCount++;
				//System.out.println("msg count "+msgCount);				
			}
		
		if (controller.state ==controller.READY) { 
			//path information is sent over the network	
				msg.add("Server model changed!"+"\n");
				//is the path Alive				
				if (model.path == null) {
					msg.add("false"+"\n");
				}else {
					msg.add("true"+"\n");
				}								
				//sending the line over						
				msg.add(Double.toString(model.currentPathCoordinate.x+(model.iModel.viewPortX*7/model.radarView.width))+"\n");
				msg.add(Double.toString(model.currentPathCoordinate.y+(model.iModel.viewPortY*7/model.radarView.height))+"\n");
				
				//sending the colour over
				msg.add(model.sampleLine.getStroke().toString()+"\n");
				//send the strokeWidth over			
				msg.add(Double.toString(model.sampleLine.getStrokeWidth())+"\n");
				
				//sending the sound over
				//velocity			
				msg.add(Double.toString(controller.soundVelocityThread.getVelocity())+"\n");
				//mouseCoordinates			
				msg.add(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).x)+"\n");
				msg.add(Double.toString(controller.mouseCoordinates.get(controller.mouseCoordinates.size()-1).y)+"\n");
				
				//can probably eliminate this code
				//clipDuration			
				//msg.add(Double.toString(model.calculateClipDuration())+"\n");
				//clipStaggerIncrement			
				//msg.add(Double.toString(model.calculateStaggerIncrement(model.selectedSoundFile))+"\n");
				//current timbre
				msg.add(Integer.toString(model.currentTimbre));
				
				//send one msg over
				String fullmsg = "";
				for (int i = 0; i<msg.size();i++) {
					fullmsg += msg.get(i);
				}
				out.writeUTF(fullmsg);
				msgCount++;			
		}
		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}	
	}
	}



