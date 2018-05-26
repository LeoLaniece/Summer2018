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
import test.Draw2Model;
import test.Draw2View;
import test.modelListener;

import java.io.*;

public class GreetingServer extends Thread {
private ServerSocket serverSocket;

public Draw2View view;
public Draw2Model model;
public Socket server;


public GreetingServer(int port, Draw2Model m) throws IOException {
   serverSocket = new ServerSocket(port);
   serverSocket.setSoTimeout(100000);
   model = m;

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
         ServerListener serverListener = new ServerListener(model,out);
         //wait for a message, print the message
         
         
         boolean isNetPathAlive = false;
         String msg = "greetingServer";
         double[] line = new double[2];
         while (msg!="exit") {
        	 
        	 msg = (String) objectIn.readUTF();
        	 isNetPathAlive = Boolean.parseBoolean(objectIn.readUTF());
        	 line[0] = Double.parseDouble((objectIn.readUTF()));
        	 line[1] = Double.parseDouble(objectIn.readUTF());
        	 if (isNetPathAlive == false) {
        		 model.netWorkPath = null;
        	 }
        	 
        	 if (model.netWorkPath == null) {
        		 model.createNewPathFromNetwork(line);
        	 }
        	 if (model.netWorkPath!=null) {
        		 model.updateNewPathFromNetwork(line);
        	 }

        	 
        	 
        	 model.notifySubscribers();
        	System.out.println("Server got msg :"+ msg);
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

public static void main(String [] args, Draw2Model m) {
	//test for javaFX component
	
   int port = 9090;//Integer.parseInt(args[0]);
   try {
      Thread t = new GreetingServer(port,m);
      t.start();
   } catch (IOException e) {
      e.printStackTrace();
   }   
   
   //Scanner sc = new Scanner(System.in);
   
   String[] arr = new String[2];
   arr[0] = "DESKTOP-3QFK6AS";
   arr[1] = "9090";
   GreetingClient GC = new GreetingClient(arr);
   GC.start();
}




}
