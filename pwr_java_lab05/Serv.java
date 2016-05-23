package pwr_java_lab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Serv implements Runnable
{
	public static void main(String [] args) throws Exception
	{
		Thread thread = new Thread(new Serv());
		thread.start();
	}
	
	
	private static final int PORT = 4444;


	@Override
	public void run() 
	{
		while(true)
		{
			try ( 
				ServerSocket serverSocket = new ServerSocket(PORT);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedReader lineToSend = new BufferedReader(new InputStreamReader(System.in));
				)
			{
				String inputLine;
				
				while((inputLine = in.readLine()) != null)
				{
					System.out.println(inputLine);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
