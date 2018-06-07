package unusedClasses;

import java.io.*;

import java.net.*;
import java.util.*;
public class SynchServer
{

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = null;
        final int PORT = 1234;
        Socket client;
        ClientHandler handler;

        try
        {
            serverSocket = new ServerSocket(PORT);
        }
        catch (IOException ioEx)
        {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
        }

        System.out.println("\nServer running...\n");

        do
        {
            //Wait for client.
            client = serverSocket.accept();

            System.out.println("\nNew client accepted.\n");
            handler = new ClientHandler(client);
            handler.start();
        }while (true);
    }
}

class ClientHandler extends Thread
{
    private Socket client;
    private Scanner input;
    private PrintWriter output;

    private static String text = "";

    public ClientHandler(Socket socket) throws IOException
    {
        client = socket;

        input = new Scanner(client.getInputStream());
        output = new PrintWriter(client.getOutputStream(),true);
    }

    public void run()
    {
        String head, tail, received;

        received = input.nextLine();

        // create head and tail in case first input is rep: or app:
        head = received.substring(0, 4);
        tail = received.substring(4);

        while (!received.equals("QUIT"))
        {
            if (head.equals("rep:"))
            {
                changeText(tail);
                output.println(text);
                // input for next one
            }
            else
                if (head.equals("app:"))
                {
                    appendText(tail);
                    output.println(text);
                    // get input for next
                }
            else
            {
                //must be some random thing that just needs to be echoed
                output.println(text);
            }

            //Get next input
            received = input.nextLine();
            //and set the head and tail again
            head = received.substring(0, 4);
            tail = received.substring(4);
        }

        try
        {
            System.out.println("Closing down connection...");
            client.close();
        }
        catch(IOException ioEx)
        {
            System.out.println("* Disconnection problem! *");
        }
    }

    private synchronized void changeText(String changedText)
    {
        text = changedText;
    }

    private synchronized void appendText(String appendedText)
    {
        text += appendedText;
    }
}
