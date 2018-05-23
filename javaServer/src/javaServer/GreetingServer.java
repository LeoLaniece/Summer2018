package javaServer;

//File Name GreetingServer.java
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class GreetingServer extends Thread {
private ServerSocket serverSocket;

public GreetingServer(int port) throws IOException {
   serverSocket = new ServerSocket(port);
   serverSocket.setSoTimeout(10000);
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
         DataOutputStream out = new DataOutputStream(server.getOutputStream());
         out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
            + "\nGoodbye!");
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

public static void main(String [] args) {
   int port = 1100;//Integer.parseInt(args[0]);
   try {
      Thread t = new GreetingServer(port);
      t.start();
   } catch (IOException e) {
      e.printStackTrace();
   }   
   
   Scanner sc = new Scanner(System.in);
   
   String[] arr = new String[2];
   arr[0] = "DESKTOP-3QFK6AS";
   arr[1] = "1100";
   GreetingClient.main(arr);
}


}
