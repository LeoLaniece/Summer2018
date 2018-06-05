package javaServer;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
//File Name GreetingClient.java
import java.net.Socket;
import java.net.UnknownHostException;

import test.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class GreetingClient extends Thread{
	
	public Draw2View view;
	public Draw2Model model;
	public Draw2Controller controller;
	public static Socket client;
	

public GreetingClient(String [] args) {
   String serverName = args[0];
   int port = Integer.parseInt(args[1]);
   try {
      System.out.println("Connecting to " + serverName + " on port " + port);
      client = new Socket(serverName, port);
      
      System.out.println("Just connected to " + client.getRemoteSocketAddress());
      OutputStream outToServer = client.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);
      
      out.writeUTF("Hello from " + client.getLocalSocketAddress());

    //  InputStream inFromServer = client.getInputStream();
   //   ObjectInputStream in = new ObjectInputStream(inFromServer);
    //  String cd =  (String) in.readObject();
      
      
    //  ObjectOutputStream objectOut = new ObjectOutputStream(outToServer);
      //start second stage
      SecondStage clientStage = new SecondStage();
      model = clientStage.m;
      controller = clientStage.c;
      

     // DataInputStream objectIn = new DataInputStream(client.getInputStream());
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
    	  DataInputStream objectIn;    	  
		try {
			objectIn = new DataInputStream(client.getInputStream());
			      //wait for a message, print the message  	      	
          boolean isNetPathAlive = false;
          String msg = "greetingServer";
          String pathPaint = "";
          double[] line = new double[9];
          int serverState;
          while (msg!="exit") {       	          	  
         	 //get the client state
         	 serverState = Integer.parseInt(objectIn.readUTF());
         	 
         	 if (serverState == controller.PAN_READY) {
         		 //draw a second viewport on the miniMap!!
         		 line[0] = Double.parseDouble((objectIn.readUTF()));
             	 line[1] = Double.parseDouble(objectIn.readUTF());           
             	 model.radarView.modelChanged();
             	 model.radarView.drawViewPortFromNet(line[0], line[1]);           	         		         		 
         		// System.out.println("client is panning their minimap");        		 
         	 }
        	  
         	 if (serverState == controller.READY) {
         	 msg = (String) objectIn.readUTF();
         	 isNetPathAlive = Boolean.parseBoolean(objectIn.readUTF());
         	 
         	 line[0] = Double.parseDouble((objectIn.readUTF()));
         	 line[1] = Double.parseDouble(objectIn.readUTF());        	 
         	 pathPaint = objectIn.readUTF();
         	 line[2] = Double.parseDouble(objectIn.readUTF());

        	 //velocity
        	 line[3] = Double.parseDouble(objectIn.readUTF());
        	 //mouseCoordinates
        	 line[4] = Double.parseDouble(objectIn.readUTF());
        	 line[5] = Double.parseDouble(objectIn.readUTF());
        	 //angle
        	 line[6] = Double.parseDouble(objectIn.readUTF());
        	 //clipDuration
        	 line[7] = Double.parseDouble(objectIn.readUTF());
        	 //clipStaggerIncrement
        	 line[8] = Double.parseDouble(objectIn.readUTF());
        	 
        	 
        	 
        	 if (model.netWorkPath == null) {
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);
        		 model.createNewPathFromNetwork(line,pathPaint);
        		 //start the path sound
        		 Coordinate mouseCoordinate = new Coordinate(line[4],line[5]);
        		 model.playPathInteractively(line[3], mouseCoordinate, line[7], line[8]);        		 
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
        		 model.netWorkPath = null;
        		 model.stopSoundGenerator();
        	 }
         	 

         	 
         	 
         	 model.notifySubscribers();
         	
     	 }
          }
          
          client.close();
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

