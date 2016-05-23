package pwr_java_lab05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable
{

	public static void main(String [] args) throws Exception
	{
		String hostName = args[0];
		Thread thread = new Thread(new Client(hostName));
		thread.start();
	}
	public Client(String hostName)
	{
		this.hostName = hostName;
	}
	@Override
	public void run() 
	{

		try(
			Socket socket = new Socket(hostName,4444);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			)
		{
			String userInput;
			while((userInput = stdIn.readLine()) != null)
			{
				out.println(userInput);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	String hostName;
}
