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

import java.io.*;

public class GreetingServer extends Thread {
private ServerSocket serverSocket;

public Draw2View view;
public Draw2Model model;
public CanvasAddress canvas;

public GreetingServer(int port, CanvasAddress cd) throws IOException {
   serverSocket = new ServerSocket(port);
   serverSocket.setSoTimeout(10000);
   canvas = cd;

}

public void run() {
   while(true) {
      try {    	      	      	  
         System.out.println("Waiting for client on port " + 
            serverSocket.getLocalPort() + "...");
        
         Socket server = serverSocket.accept();
         
         System.out.println("Just connected to " + server.getRemoteSocketAddress());
         DataInputStream in = new DataInputStream(server.getInputStream());
         
         System.out.println(in.readUTF());
         
         //figure out how to create a public canvas thing.
         ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());         
         System.out.println("sending canvas!");
       //  out.writeUTF(sc.nextLine());
         out.writeObject(canvas);
         out.flush();
         
         
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

public static void main(String [] args, CanvasAddress cd) {
	//test for javaFX component
	
   int port = 1100;//Integer.parseInt(args[0]);
   try {
      Thread t = new GreetingServer(port, cd);
      t.start();
   } catch (IOException e) {
      e.printStackTrace();
   }   
   
   //Scanner sc = new Scanner(System.in);
   
   String[] arr = new String[2];
   arr[0] = "DESKTOP-3QFK6AS";
   arr[1] = "1100";
   GreetingClient.main(arr);
}


}
