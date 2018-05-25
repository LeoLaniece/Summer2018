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

      
      ClientListener clientListener = new ClientListener(clientStage.m,out);
      
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
      String msg = "Greeting client";
      while (msg!="exit") {
    	  System.out.println(msg);
     	 msg = (String) objectIn.readUTF();
     	 System.out.println(msg);
     	 } 									
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
            
      }
      
      

}

