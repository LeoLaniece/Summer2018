package javaServer;

//File Name GreetingServer.java
import java.net.*;

import javafx.scene.Group;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import test.Coordinate;
import test.CreateFile;
import test.Draw2Controller;
import test.Draw2Model;
import test.Draw2View;
import test.TaskCompleteStage;
import test.modelListener;

import java.io.*;

public class DrawingServer extends Thread {
private ServerSocket serverSocket;

public Draw2View view;
public Draw2Model model;
public Socket server;
public Draw2Controller controller;
public int transaction = 1;

/**
 * This class receives all messages sent from the clientListener 
 * Will execute the appropriate action based on the message received
 * Actions are executed on the drawing server stage
 * @param port
 * @param m
 * @param c
 * @throws IOException
 */
public DrawingServer(int port, Draw2Model m, Draw2Controller c) throws IOException {
   serverSocket = new ServerSocket(port);
   serverSocket.setSoTimeout(100000);
   model = m;
   controller = c;

}

public void run() {		
   while(true) {
      try {    	      	      	  
    	  //establish connection
         System.out.println("Waiting for client on port " + 
            serverSocket.getLocalPort() + "...");        
         server = serverSocket.accept();         
         System.out.println("Just connected to " + server.getRemoteSocketAddress());
         DataInputStream in = new DataInputStream(server.getInputStream());         
         System.out.println(in.readUTF());         
         DataOutputStream out = new DataOutputStream(server.getOutputStream());         
         DataInputStream objectIn = new DataInputStream(server.getInputStream());
         ServerListener serverListener = new ServerListener(model, controller,out);

         //initialize variables needed for networking
         boolean isNetPathAlive = false;
         String msg = "greetingServer";
         String fullmsg = "";
         String pathPaint = "";
         double[] line = new double[9];
         int clientState;
         while (msg!="exit") {
        	 //get clientListener message as a line separated string    	 
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
        	 
       		//get the client state
        	 clientState = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;
        	 
         	 //if the read and observe test is happening
        	 //open instructions
     		if (clientState == controller.READ_AND_OBSERVE) {     			
     			model.launchReadAndObserverInstructionsStage(model.iModel);
     			controller.taskRunning =true;
     			controller.view.drawBorder();
     		}
     		
     		if (clientState == controller.CLEAR_WORKSPACE) {     			
	        	   controller.model.resetModel();
	        	   controller.view.resetView();
	        	   controller.radarView.resetView();	        	   
	        	   controller.model.notifySubscribers();
     		}
     		
     		//if the freeze test is in progress lauch the instruction stage 
     		if (clientState == controller.FREEZE_TEST_TASK) {
     			//adjust iModeltask depending on freeze test variation
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
     		if (clientState == controller.CLOSE_PROMPT_FOR_SHAPE) {     		
     			if (model.instructions!= null) {
     				model.instructions.submit();
     			}     			
     		}
     		
     		//if the server task is complete
     		if (clientState == controller.CLOSE_INSTRUCTIONS) {
       		 //close instruction window if it is still there
       		 if (model.instructions != null) {
       			 model.closeInstructions();       			
       			controller.taskRunning =false;
       			model.iModel.task = -1;
   			    controller.view.resetView();
   				Platform.runLater(new Runnable() {
   				    @Override
   				        public void run() {	
   			 new TaskCompleteStage(controller);
   				    }
   				});
       		 }
    		}
     		
     		//if the server has just selected a drawing tool
     		if (clientState == controller.PLAY_IMPACT) {
     			int impactFile = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;
     			File impact = model.getImpactSoundFile(impactFile);
     			model.player.playFileClip(impact);
     			if (model.iModel.task == model.iModel.TOOL_REACTION_TASK) {
     				model.logPartnerImpact();
     			}
      		}
     		
     		//if the server has pressed ready and is ready to begin the task
     		if (clientState == controller.READY_TO_BEGIN_TASK) {
          		 //close instruction window if it is still there
          		 if (model.instructions != null) {
          			 model.hideInstructions();          			
          		 }
       		}
     		
     		//if the server is filling out a survey and needs to know which shape was drawn
     		if (clientState == controller.PROMPT_FOR_SHAPE) {
     			 System.out.println("got prompted to shape !");
        		 model.updateInstructionsStage();     			
     		}
     		
     		//if the server is filling out a survey and needs the other user to sit still
     		if (clientState == controller.PAUSE_UNTIL_QUIZ_COMPLETE) {
     			System.out.println("pause until quiz cmplete !");
        		 model.showPauseStage();     			
     		}
        	 
     		//adjust the server's minimap location
        	 if (clientState == controller.PAN_READY) {
        		 //draw a second viewport on the miniMap!!
        		 line[0] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
             	 line[1] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++; 
        		 //START the ViewPortDisplacementSound generator here
        	     if (model.networkVPDS == null) {
            		 model.beginNetworkViewPortMovementSound();
          			//for logging
            		 Coordinate p = model.radarView.calculateNetViewPortCenter();
          			CreateFile log = new CreateFile("Network panning action began at"
          	    			+" x = "+p.x+" y = "+p.y, model.iModel.currentLogFileName);              		
            	 }else {
            		 //update sound if the view port sound is already happening
            		 model.updateNetworkVPDSGeneratorLocation(model.radarView.calculateNetViewPortCenter());
            	 }          
            	 model.radarView.modelChanged();            	 
            	 model.radarView.drawViewPortFromNet(line[0], line[1]);            	            	 

        	 }else {
         		//stop the VPDS generator
            	 if (model.networkVPDS != null) {
          			//for logging
            		 Coordinate p = model.radarView.calculateNetViewPortCenter();
          			CreateFile log = new CreateFile("Network panning action ended at"
          	    			+" x = "+p.x+" y = "+p.y, model.iModel.currentLogFileName);  
          			model.stopNetworkVPDS();
            	 }
        	 }

        	//if server is drawing a path, replicate path and produce sound
        	 if (clientState == controller.READY) {
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
            	// line[7] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
            	 //clipStaggerIncrement
            	// line[8] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++; 
            	//timbre selection
            	 int netTimbre = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex ++;
        	 
            	//draw the network path
        	 if (model.netWorkPath == null) {
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);        		         		
        		//draw the path and log the drawing action start
        		 model.createNewPathFromNetwork(line,pathPaint);        		         		 
        		//set timbre
        		 model.setNetTimbre(netTimbre);
        		 //start the path sound
        		 Coordinate mouseCoordinate = new Coordinate(line[4],line[5]);
        		 model.playNetworkPathInteractively(line[3], mouseCoordinate); //line[7], line[8]);     
        		 //log user1 activity here 
        		 if (controller.model.readAndObserveTrial != null) {
        			 controller.model.readAndObserveTrial.User1ActiveTimes.add(System.currentTimeMillis()-controller.model.readAndObserveTrial.startTime);
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
         		if (!model.netSoundGeneratorStart) {
        			model.networkSoundGenerator.start();
        			model.netSoundGeneratorStart = true;
        		}
         		model.updateNetworkSoundGeneratorVelocity(line[3]);
         		Coordinate mouseCoordinate = new Coordinate(line[4],line[5]);
         		model.updateNetworkSoundGeneratorPanValue(mouseCoordinate);
         		//can eliminate this line
         		//model.updateSoundGeneratorPathAngleFromNet(line[6]);        		 
        	 }        	
        	//close the path and path sound generator
        	 if (isNetPathAlive == false) {        		 
        		 model.netWorkPath = null;        		 
        		 model.stopNetworkSoundGenerator();  
        			//for logging
        			CreateFile log = new CreateFile("Network drawing action ended at"
        	    			+" x = "+model.radarView.getLastKnownCoordinate().x+
        	    			" y = "+model.radarView.getLastKnownCoordinate().y, model.iModel.currentLogFileName);
        		 //log user 1 activity here
        		 if (controller.model.readAndObserveTrial != null) {
        			 controller.model.readAndObserveTrial.User1ActiveTimes.add(System.currentTimeMillis()-controller.model.readAndObserveTrial.startTime);
        		 }
        	 }        	
        	 }    
         }		         
         server.close();
         
      } catch (SocketTimeoutException s) {
         System.out.println("Socket timed out!");
         break;
      } catch (IOException e) {
         e.printStackTrace();
         break;
      }
   }
}

public static void main(String [] args, Draw2Model m, Draw2Controller c) {		
   //sets up server and client on the same device
	int port = 9080;
   try {
      Thread t = new DrawingServer(port,m, c);
      t.start();
   } catch (IOException e) {
      e.printStackTrace();
   }      
   //uncomment this block to test server client connection on the same device
    /*  
   String[] arr = new String[2];
   arr[0] = "DESKTOP-3QFK6AS";
   arr[1] = "9080";
   DrawingClient GC = new DrawingClient(arr);
   GC.start();
  // */
}




}
