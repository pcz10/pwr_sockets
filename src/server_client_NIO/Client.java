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
		
		boolean connectionRequest = true;
		while(connectionRequest)
		{
			try (
				BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
				)
			{
				
				String userInput;
				while((userInput = inputStream.readLine()) != null)
				{
					byte[] message = new String(userInput).getBytes();
					ByteBuffer buffer = ByteBuffer.wrap(message);
					clientSocket.write(buffer);
					buffer.clear();
					
					if(userInput == "end")
						connectionRequest = false;
					
					Thread.sleep(1);
				}
			}
		}
	clientSocket.close();
	}
}
