package pwr_java_lab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		ServerSocket serverSocket = null;
		while(true)
		{
			try
			{
				serverSocket = new ServerSocket(PORT);
				Socket clientSocket = serverSocket.accept();
				BufferedReader inputReader = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
				System.out.println(inputReader.readLine());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					serverSocket.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
