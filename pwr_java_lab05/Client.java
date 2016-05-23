package pwr_java_lab05;

import java.io.IOException;
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
		Socket socket = null;
		try
		{
			Thread.sleep(1000);
			
			socket = new Socket(hostName,4444);
			PrintWriter outWriter = new PrintWriter(socket.getOutputStream(),true);
			outWriter.println("hi server");
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	String hostName;
}
