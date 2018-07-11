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


public DrawingServer(int port, Draw2Model m, Draw2Controller c) throws IOException {
   serverSocket = new ServerSocket(port);
   serverSocket.setSoTimeout(100000);
   model = m;
   controller = c;

}

public void run() {		
   while(true) {
      try {    	      	      	  
         System.out.println("Waiting for client on port " + 
            serverSocket.getLocalPort() + "...");
        
         server = serverSocket.accept();
         
         System.out.println("Just connected to " + server.getRemoteSocketAddress());
         DataInputStream in = new DataInputStream(server.getInputStream());         
         System.out.println(in.readUTF());         
         DataOutputStream out = new DataOutputStream(server.getOutputStream());         
         DataInputStream objectIn = new DataInputStream(server.getInputStream());
         ServerListener serverListener = new ServerListener(model, controller,out);
         //wait for a message, print the message
         
         
         boolean isNetPathAlive = false;
         String msg = "greetingServer";
         String fullmsg = "";
         String pathPaint = "";
         double[] line = new double[9];
         int clientState;
         while (msg!="exit") {
        	 //get the client state
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
        	 
        	 clientState = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;
        	 
         	 //if the read and observe test is happening
        	 //open instructions
     		if (clientState == controller.READ_AND_OBSERVE) {     			
     			model.launchReadAndObserverInstructionsStage(model.iModel);
     			controller.taskRunning =true;
     			controller.view.drawBorder();
     		}
     		
     		if (clientState == controller.FREEZE_TEST_TASK) {
     			int serverTask = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;
     			System.out.println("server task "+serverTask);
     			if (serverTask == model.iModel.LOCATION_IDENTIFICATION_TASK) {
     				model.iModel.task = serverTask;
     			}
    			model.launchReadAndObserverInstructionsStage(model.iModel);
     			controller.taskRunning =true;
     			controller.view.drawBorder();
     		}
     		
     		if (clientState == controller.CLOSE_PROMPT_FOR_SHAPE) {     		
     			if (model.instructions!= null) {
     				model.instructions.submit();
     			}     			
     		}
     		
     		//close instructions
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
     		
     		if (clientState == controller.PLAY_IMPACT) {
     			int impactFile = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;
     			File impact = model.getImpactSoundFile(impactFile);
     			model.player.playFileClip(impact);
      		}
     		
     		if (clientState == controller.READY_TO_BEGIN_TASK) {
          		 //close instruction window if it is still there
          		 if (model.instructions != null) {
          			 model.hideInstructions();
          			// model.createFileForFreezeTest();
          		 }
       		}
     		
     		if (clientState == controller.PROMPT_FOR_SHAPE) {
     			 System.out.println("got prompted to shape !");
        		 model.updateInstructionsStage();     			
     		}
     		
     		if (clientState == controller.PAUSE_UNTIL_QUIZ_COMPLETE) {
     			System.out.println("pause until quiz cmplete !");
        		 model.showPauseStage();     			
     		}
        	 
        	 if (clientState == controller.PAN_READY) {
        		  //START the VPDS generator here
        	     if (model.VPDS == null) {
            		 model.beginViewPortMovementSound();
            	 }else {
            		 model.updateVPDSGeneratorLocation(model.radarView.calculateNetViewPortCenter());
            	 }
        		 //draw a second viewport on the miniMap!!
        		 line[0] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
             	 line[1] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;           
            	 model.radarView.modelChanged();
            	 //want these values to be relative to the minimap logical size
            	 model.radarView.drawViewPortFromNet(line[0], line[1]);            	            	 
        	 }else {
         		//stop the VPDS generator
            	 if (model.VPDS != null) {
            		 model.stopVPDS();
            	 }
        	 }


        	 if (clientState == controller.READY) {
             	 msg = netInfo.get(netInfoIndex); netInfoIndex++;        	 
             	 //draw path info
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
            	 //angle
            	// line[6] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
            	 //clipDuration
            	 line[7] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
            	 //clipStaggerIncrement
            	 line[8] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++; 
            	//timbre selection
            	 int netTimbre = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex ++;
        	 
        	 if (model.netWorkPath == null) {
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);
        		 model.createNewPathFromNetwork(line,pathPaint);
        		//set timbre
        		 model.setNetTimbre(netTimbre);
        		 //start the path sound
        		 Coordinate mouseCoordinate = new Coordinate(line[4],line[5]);
        		 model.playPathInteractively(line[3], mouseCoordinate, line[7], line[8]);     
        		 //log user1 activity here 
        		 if (controller.readAndObserveTrial != null) {
        			 controller.readAndObserveTrial.User1ActiveTimes.add(System.currentTimeMillis()-controller.readAndObserveTrial.startTime);
        		 }
        	 }else
        	 if (model.netWorkPath!=null) {
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);
        		 model.updateNewPathFromNetwork(line);
        		 //update the sound generator
         		model.updateSoundGeneratorVelocity(line[3]);
         		Coordinate mouseCoordinate = new Coordinate(line[4],line[5]);
         		model.updateSoundGeneratorPanValue(mouseCoordinate);
         		model.updateSoundGeneratorPathAngleFromNet(line[6]);        		 
         		
        	 }        	 
        	 if (isNetPathAlive == false) {
        		 //model.radarView.getLastKnownCoordinate();
        		 model.netWorkPath = null;        		 
        		 model.stopSoundGenerator();  
        		 //log user 1 activity here
        		 if (controller.readAndObserveTrial != null) {
        			 controller.readAndObserveTrial.User1ActiveTimes.add(System.currentTimeMillis()-controller.readAndObserveTrial.startTime);
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
	//test for javaFX component
	
   int port = 9080;
   try {
      Thread t = new DrawingServer(port,m, c);
      t.start();
   } catch (IOException e) {
      e.printStackTrace();
   }   
    /*  
   String[] arr = new String[2];
   arr[0] = "DESKTOP-3QFK6AS";
   arr[1] = "9080";
   DrawingClient GC = new DrawingClient(arr);
   GC.start();
  // */
}




}
