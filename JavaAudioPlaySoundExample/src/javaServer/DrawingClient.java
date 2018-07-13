package javaServer;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import test.*;

/**
 * This class receives all messages sent from the serverListener 
 * Will execute the appropriate action based on the message received
 * Actions are executed on the drawing client stage
 * @author HCI Lab
 *
 */
public class DrawingClient extends Thread{
	
	public Draw2View view;
	public Draw2Model model;
	public Draw2Controller controller;
	public static Socket client;
	public int transaction = 1;
	

public DrawingClient(String [] args) {
   String serverName = args[0];
   int port = Integer.parseInt(args[1]);
   try {
	   //connect to the server
      System.out.println("Connecting to " + serverName + " on port " + port);
      client = new Socket(serverName, port);      
      System.out.println("Just connected to " + client.getRemoteSocketAddress());      
      //set up send and receive messages from client to server
      OutputStream outToServer = client.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);      
      out.writeUTF("Hello from " + client.getLocalSocketAddress());      
      
      //start client drawing application
      ClientDrawingStage clientStage = new ClientDrawingStage();
      model = clientStage.m;
      controller = clientStage.c;      
     
      //start the clientListener in charge of communicating with the server
      ClientListener clientListener = new ClientListener(clientStage.m, clientStage.c,out);            
   } catch (UnknownHostException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}finally {}
}
	//listening for messages
      public void run() {    	  
    	  //initialize variables which will store information received from the drawing server
    	  DataInputStream objectIn;    	  
		try {						
		  objectIn = new DataInputStream(client.getInputStream());			      	      	
          boolean isNetPathAlive = false;          
          String pathPaint = "";
          String msg = "";
          String fullmsg = "";
          double[] line = new double[9];
          int serverState;
          while (true) {       	          	        	  
         	 //read the message line by line
        	  fullmsg = objectIn.readUTF();
        		String msgline;
        		ArrayList<String> netInfo = new ArrayList<>();
        		BufferedReader reader = new BufferedReader(new StringReader(fullmsg));        				
        		try {
        			while ((msgline = reader.readLine()) != null) {
        			    netInfo.add(msgline);		    
        			}
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		int netInfoIndex = 0;
        		
        		//get the controller state of the drawing server
         	 serverState = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;
         	 
         	 //if the read and observe test is happening
     		if (serverState == controller.READ_AND_OBSERVE) {     			
     			model.launchReadAndObserverInstructionsStage(model.iModel);
     			controller.taskRunning =true;
     			controller.view.drawBorder();
     		}
     		
     		//if the Freeze Test test is happening
     		if (serverState == controller.FREEZE_TEST_TASK) {
     			int serverTask = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;     			
     			if (serverTask == model.iModel.REAL_FREEZE_TEST) {
     				model.iModel.task = serverTask;
     			}
     			if (serverTask == model.iModel.LOCATION_IDENTIFICATION_TASK) {
     				model.iModel.task = serverTask;
     			}
     			if (serverTask == model.iModel.SHAPE_DETECTION_TASK) {
     				model.iModel.task = serverTask;
     			}
    			model.launchReadAndObserverInstructionsStage(model.iModel);
     			controller.taskRunning =true;
     			controller.view.drawBorder();
     		}
     		
     		//if the FreezeQuiz questionnaire just got submitted
     		if (serverState == controller.CLOSE_PROMPT_FOR_SHAPE) {     		
     			if (model.instructions!= null) {
     				model.instructions.submit();
     			}     			
     		}
     		
     		//if the server task is complete
     		if (serverState == controller.CLOSE_INSTRUCTIONS) {        		 
        		 if (model.instructions != null) {
        			 model.closeInstructions();
        			 controller.taskRunning =false;
        			 model.iModel.task = -1;
        			 controller.view.resetView();
        			 model.createFileForFreezeTest();
        				Platform.runLater(new Runnable() {
           				    @Override
           				        public void run() {	
           			 new TaskCompleteStage(controller);
           				    }
           				});
        		 }
     		}
     		
     		//if the server has just selected a drawing tool
     		if (serverState == controller.PLAY_IMPACT) {
     			int impactFile = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;
     			File impact = model.getImpactSoundFile(impactFile);
     			model.player.playFileClip(impact);
     			if (model.iModel.task == model.iModel.TOOL_REACTION_TASK) {
     				model.logPartnerImpact();
     			}
      		}
     		
     		//if the server has pressed ready and is ready to begin the task
     		if (serverState == controller.READY_TO_BEGIN_TASK) {
       		 //close instruction window if it is still there
       		 if (model.instructions != null) {
       			 model.hideInstructions();       			
       		 }
    		}
     		  
     		//if the server is filling out a survey and needs to know which shape was drawn
     		if (serverState == controller.PROMPT_FOR_SHAPE) {     			
        		 model.updateInstructionsStage();     			
     		}
     		
     		//if the server is filling out a survey and needs the other user to sit still
     		if (serverState == controller.PAUSE_UNTIL_QUIZ_COMPLETE) {     			
        		 model.showPauseStage();     			
     		}
         	 
         	 //adjust the server's minimap location
         	 if (serverState == controller.PAN_READY) {
       		  //START the ViewPortDisplacementSound generator here
        	     if (model.VPDS == null) {
            		 model.beginViewPortMovementSound();
            		 //log user1 activity here for read and observe
            		 if (controller.readAndObserveTrial != null) {
            			 controller.readAndObserveTrial.User1ActiveTimes.add(System.currentTimeMillis()-controller.readAndObserveTrial.startTime);
            		 }
            	 }else {
            		 //update sound if the view port sound is already happening
            		 model.updateVPDSGeneratorLocation(model.radarView.calculateNetViewPortCenter());
            	 }         		 
         		 //draw a second viewport on the miniMap!
         		 line[0] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
             	 line[1] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;                     	              	 
             	 model.radarView.modelChanged();
             	 model.radarView.drawViewPortFromNet(line[0], line[1]);    

         	 }else {
         		//stop the VPDS generator
            	 if (model.VPDS != null) {            		 
            		 model.stopVPDS();
               		 //log user 1 activity here for ReadAndObserve study task
            		 if (controller.readAndObserveTrial != null) {
            			 controller.readAndObserveTrial.User1ActiveTimes.add(System.currentTimeMillis()-controller.readAndObserveTrial.startTime);
            		 }
            	 }
 
        	 }
        	  
         	 //if server is drawing a path, replicate path and produce sound
         	 if (serverState == controller.READY) {         		          		 
         	 msg = netInfo.get(netInfoIndex); netInfoIndex++;        	 
         	 //path location, paint and size info
         	 isNetPathAlive = Boolean.parseBoolean(netInfo.get(netInfoIndex)); netInfoIndex++;      	 
         	 line[0] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
         	 line[1] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;      	 
         	 pathPaint = netInfo.get(netInfoIndex); netInfoIndex++;
         	 line[2] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;

         	 //path sound info
        	 //velocity
        	 line[3] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
        	 //mouseCoordinates
        	 line[4] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
        	 line[5] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
        	 
        	 //probably can eliminate these
        	 //clipDuration
        	 //line[7] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
        	 //clipStaggerIncrement
        	 //line[8] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;     	         	 
        	 //timbre selection
        	 int netTimbre = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex ++;
        	 
        	 //draw the network path
        	 if (model.netWorkPath == null) {        		         		 
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);
        		 //draw the path
        		 model.createNewPathFromNetwork(line,pathPaint);
        		 //start the path sound
        		 Coordinate mouseCoordinate = new Coordinate(line[4],line[5]);
        		 //set timbre
        		 model.setNetTimbre(netTimbre);        		
        		 model.playPathInteractively(line[3], mouseCoordinate); //line[7], line[8]);        		 
        		 //log user1 activity here for read and observe
        		 if (controller.readAndObserveTrial != null) {
        			 controller.readAndObserveTrial.User1ActiveTimes.add(System.currentTimeMillis()-controller.readAndObserveTrial.startTime);
        		 }
        	 }else
        		 //add on to current network path
        	 if (model.netWorkPath!=null) {
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);
        		 //add bit to current path
        		 model.updateNewPathFromNetwork(line);
        		 //update the sound generator
         		model.updateSoundGeneratorVelocity(line[3]);
         		Coordinate mouseCoordinate = new Coordinate(line[4],line[5]);
         		model.updateSoundGeneratorPanValue(mouseCoordinate);
         		//can eliminate this line
         		//model.updateSoundGeneratorPathAngleFromNet(line[6]);        		 
        	 }        	 
        	 //close the path and path sound generator
        	 if (isNetPathAlive == false) {
        		 model.netWorkPath = null;
        		 model.stopSoundGenerator();
        		 //log user 1 activity here for read and observe study task
        		 if (controller.readAndObserveTrial != null) {
        			 controller.readAndObserveTrial.User1ActiveTimes.add(System.currentTimeMillis()-controller.readAndObserveTrial.startTime);
        		 }
        	 }       	          	                   	
     	 }
          }                    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                  
      }
}

