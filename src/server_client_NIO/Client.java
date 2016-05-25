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

		Thread.sleep(1000);
			try (
				BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
				)
			{
				boolean connectionRequest = true;
				ByteBuffer buffer = ByteBuffer.allocate(512);
				while(connectionRequest)
				{
					String message = readMessageFromServer(clientSocket, buffer);
						log(" "+message+ " ");
					String userInput;
					if((userInput = inputStream.readLine()) != null)
					{
						log(readMessageFromServer(clientSocket, buffer));
						sendMessageToServer(clientSocket, userInput, buffer);
					}
				}
		}	
	clientSocket.close();
	}
	
	private static String readMessageFromServer(SocketChannel clientSocket, ByteBuffer buffer) throws IOException 
	{
		buffer.clear();
		clientSocket.read(buffer);
		String result = new String(buffer.array()).trim();
		buffer.rewind();
		return result;
	}
	private static void sendMessageToServer(SocketChannel clientSocket, String userInput, ByteBuffer buffer) throws IOException
	{
		buffer.clear();
		byte[] message = new String(userInput).getBytes();
		buffer = ByteBuffer.wrap(message);	
		buffer.flip();	
		clientSocket.write(buffer);
	}
	public static void log(String message)
	{
		System.out.println(message);
	}
}
