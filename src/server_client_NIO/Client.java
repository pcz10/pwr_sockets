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
				boolean isConnected = true;
				while(isConnected)
				{
					String userInput;
					if((userInput = inputStream.readLine())!=null)
					{
						sendMessageToServer(clientSocket, userInput);
						log(readMessageFromServer(clientSocket));
					}
				}	
		}	
	clientSocket.close();
	}
	
	private static String readMessageFromServer(SocketChannel clientSocket) throws IOException 
	{
		ByteBuffer buffer = ByteBuffer.allocate(512);
		clientSocket.read(buffer);
		String result = new String("RECIVED: " + buffer.array()).trim();
		buffer.rewind();
		return result;
	}
	private static void sendMessageToServer(SocketChannel clientSocket, String userInput) throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(512);
		byte[] message = new String("SEND :" + userInput).getBytes();
		buffer = ByteBuffer.wrap(message);	
		clientSocket.write(buffer);
	}
	public static void log(String message)
	{
		System.out.println(message);
	}
}
