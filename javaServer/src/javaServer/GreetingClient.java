package javaServer;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
//File Name GreetingClient.java
import java.net.Socket;
import test.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class GreetingClient {
	
	public Draw2View view;
	public Draw2Model model;
	

public static void main(String [] args) {
   String serverName = args[0];
   int port = Integer.parseInt(args[1]);
   try {
      System.out.println("Connecting to " + serverName + " on port " + port);
      Socket client = new Socket(serverName, port);
      
      System.out.println("Just connected to " + client.getRemoteSocketAddress());
      OutputStream outToServer = client.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);
      
      out.writeUTF("Hello from " + client.getLocalSocketAddress());
      InputStream inFromServer = client.getInputStream();
      ObjectInputStream in = new ObjectInputStream(inFromServer);
      CanvasAddress cd =  (CanvasAddress) in.readObject();
      System.out.println("Server says : got canvas!");
      GraphicsContext gc = cd.c.getGraphicsContext2D();
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, 400, 400);
      
      
      
      client.close();
   } catch (IOException e) {
      e.printStackTrace();
   } catch (ClassNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}
}
