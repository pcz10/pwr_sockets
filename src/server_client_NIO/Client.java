package server_client_NIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client 
{
	public static void main(String[] args) throws IOException, InterruptedException
	{
		String hostName = args[0];
		InetSocketAddress address = new InetSocketAddress(hostName,1111);
		SocketChannel clientSocket = SocketChannel.open(address);
		clientSocket.configureBlocking(false);
		
		Thread.sleep(500);
			try (
				BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
				)
			{
				log("Type your nick name: ");
				String clientInput = inputStream.readLine();
				String clientNick = "\n" + clientInput;
				Logs logger = new Logs(clientInput);
				boolean isConnected = true;
				while(isConnected)
				{
					log("\nsend: ");
					String userInput;
					if((userInput = inputStream.readLine())!=null)
					{
						sendMessageToServer(clientSocket, userInput, clientNick, logger);
						log(readMessageFromServer(clientSocket, logger,clientNick));
					}
				}	
			}	
	clientSocket.close();
	}
	private static String readMessageFromServer(SocketChannel clientSocket, Logs logger, String clientNick) throws IOException 
	{
		
		ByteBuffer buffer = ByteBuffer.allocate(512);
		clientSocket.read(buffer);
		String result = new String(buffer.array()).trim();
		String print = result;
		if(print.length()>0)
		{
			logger.saveMessage(print);
		}
		buffer.rewind();
		return print;
	}
	private static void sendMessageToServer(SocketChannel clientSocket, String userInput, String clientNick, Logs logger) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(512);
		String message = userInput;
		if(message.length()>0)
		{
			byte[] messageAsBytes = new String(clientNick + " says: " + message).getBytes();
			logger.saveMessage(message);
			buffer = ByteBuffer.wrap(messageAsBytes);	
			clientSocket.write(buffer);
		}
	}
	public static void log(String message)
	{
		System.out.print(message);
	}
}
