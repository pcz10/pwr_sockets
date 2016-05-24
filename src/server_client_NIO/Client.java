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

		Thread.sleep(5000);
			try (
				BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
				)
			{
				boolean connectionRequest = true;
				//
				while(connectionRequest)
				{
					String userInput;
					if((userInput = inputStream.readLine()) != null)
					{
						sendMessageToServer(clientSocket, userInput, buffer);
						String message = readMessageFromServer(clientSocket, buffer);
						if(message!=null)
							log(" "+message+ " ");
					}
				}
		}	
	clientSocket.close();
	}
	
	private static String readMessageFromServer(SocketChannel clientSocket, ByteBuffer buffer) throws IOException 
	{
		log("metoda readMessageFromServer. PRZED CZYTANIEM");
		clientSocket.read(buffer);
		String result = new String(buffer.array()).trim();
		buffer.clear();
		log("metoda readMessageFromServer. PO CZYTANIU");
		return result;
	}
	private static void sendMessageToServer(SocketChannel clientSocket, String userInput, ByteBuffer buffer) throws IOException
	{
		log("metoda sendMessageToServer. PRZED WPISANIEM");

		log("\ntekst który wpisuje to: "+userInput);
		byte[] message = new String(userInput).getBytes();
		buffer = ByteBuffer.wrap(message);
		clientSocket.write(buffer);
		log("metoda sendMessageToServer. PO WPISANIU");
	}
	public static ByteBuffer buffer = ByteBuffer.allocate(256);
	public static void log(String message)
	{
		System.out.println(message);
	}
}
