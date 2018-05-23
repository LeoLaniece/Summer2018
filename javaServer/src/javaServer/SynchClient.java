package javaServer;

import java.io.*;
import java.net.*;
import java.util.*;
public class SynchClient
{

    public static void main(String[] args) throws IOException
    {
        InetAddress host = null;
        final int PORT = 1234;
        Socket socket;
        Scanner networkInput,keyboard;
        PrintWriter output;

        try
        {
            host = InetAddress.getLocalHost();
        }
        catch(UnknownHostException uhEx)
        {
            System.out.println("\nHost ID not found!\n");
        }

        socket = new Socket(host, PORT);
        networkInput = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(),true);

        keyboard = new Scanner(System.in);

        String message, response;

        do
        {
            System.out.print("\nEnter message ('QUIT' to exit): ");
            message = keyboard.nextLine();

            while (message.length() < 4)
            {
                System.out.print("\nPlease enter 4 or more characters: ");
                message = keyboard.nextLine();
            }

            output.println(message);
            if (!message.equals("QUIT"))
            {
                response = networkInput.nextLine();

                System.out.println("\n" + response);
            }

        }while (!message.equals("QUIT"));

        try
        {
            System.out.println("\nClosing down connection...\n");
            socket.close();
        }
        catch(IOException ioEx)
        {
            System.out.println("\n* Disconnection problem! *\n");
        }
    }
}
