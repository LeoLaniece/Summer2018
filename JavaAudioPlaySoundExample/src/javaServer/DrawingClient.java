package javaServer;


import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
//File Name GreetingClient.java
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import test.*;

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
        	  //model.netTransaction = false;        	  
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
         	 serverState = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex++;
         	 
         	 //if server is panning move all paths by...
         	 //adjust the server's minimap location
         	 if (serverState == controller.PAN_READY) {
         		 //draw a second viewport on the miniMap!
         		 line[0] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
             	 line[1] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;                     	              	 
             	 model.radarView.modelChanged();
             	 model.radarView.drawViewPortFromNet(line[0], line[1]);          	         		         		          		        		 
         	 }
        	  
         	 //if server is drawing a path, replicate path and produce sound
         	 if (serverState == controller.READY) {
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
        	 line[6] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
        	 //clipDuration
        	 line[7] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;
        	 //clipStaggerIncrement
        	 line[8] = Double.parseDouble(netInfo.get(netInfoIndex)); netInfoIndex++;     	         	 
        	 //timbre selection
        	 int netTimbre = Integer.parseInt(netInfo.get(netInfoIndex)); netInfoIndex ++;
        	 
        	 //start new path
        	 if (model.netWorkPath == null) {        		         		 
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);
        		 model.createNewPathFromNetwork(line,pathPaint);
        		 //start the path sound
        		 Coordinate mouseCoordinate = new Coordinate(line[4],line[5]);
        		 //set timbre
        		 model.setTimbre(netTimbre);
        		 model.playPathInteractively(line[3], mouseCoordinate, line[7], line[8]);        		 
        	 }else
        		 //add on to current path
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
        	 //end the path and path sound generator
        	 if (isNetPathAlive == false) {
        		 model.netWorkPath = null;
        		 model.stopSoundGenerator();
        	 }        	          	          	 
        	 model.netTransaction = true;
         	 model.notifySubscribers();
         	 System.out.println(fullmsg);
     	 }
          }                    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
            
      }
      
   /*   public static void main(String[] args) {
    	   String[] arr = new String[2];
    	   arr[0] = "DESKTOP-3QFK6AS";
    	   arr[1] = "9080";
    	   GreetingClient GC = new GreetingClient(arr);
    	   GC.start();
      }*/ 
      
      

}

