package javaServer;

//File Name GreetingServer.java
import java.net.*;

import javafx.scene.Group;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import java.util.Scanner;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import test.Draw2Controller;
import test.Draw2Model;
import test.Draw2View;
import test.modelListener;

import java.io.*;

public class GreetingServer extends Thread {
private ServerSocket serverSocket;

public Draw2View view;
public Draw2Model model;
public Socket server;
public Draw2Controller controller;


public GreetingServer(int port, Draw2Model m, Draw2Controller c) throws IOException {
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
         String pathPaint = "";
         double[] line = new double[3];
         while (msg!="exit") {
        	 
        	 msg = (String) objectIn.readUTF();
        	 isNetPathAlive = Boolean.parseBoolean(objectIn.readUTF());
        	 
        	 line[0] = Double.parseDouble((objectIn.readUTF()));
        	 line[1] = Double.parseDouble(objectIn.readUTF());        	 
        	 pathPaint = objectIn.readUTF();
        	 line[2] = Double.parseDouble(objectIn.readUTF());
        	 
        	 
        	 if (model.netWorkPath == null) {
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);
        		 model.createNewPathFromNetwork(line,pathPaint);
        	 }else
        	 if (model.netWorkPath!=null) {
        		 //calculate coordinate offsets
        		 line[0] = line[0]-(model.iModel.viewPortX*7/model.radarView.width);
        		 line[1] = line[1]-(model.iModel.viewPortY*7/model.radarView.height);
        		 model.updateNewPathFromNetwork(line);
        	 }        	 
        	 if (isNetPathAlive == false) {
        		 model.netWorkPath = null;
        	 }
        	 
        	// System.out.println("Server got msg :"+ msg);
        	 model.notifySubscribers();
        	
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
	
   int port = 9080;//Integer.parseInt(args[0]);
   try {
      Thread t = new GreetingServer(port,m, c);
      t.start();
   } catch (IOException e) {
      e.printStackTrace();
   }   
   
   //Scanner sc = new Scanner(System.in);
   
   String[] arr = new String[2];
   arr[0] = "DESKTOP-3QFK6AS";
   arr[1] = "9080";
   GreetingClient GC = new GreetingClient(arr);
   GC.start();
}




}
