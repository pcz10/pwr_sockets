package server_client_NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		configureServerSocketProperties(serverSocket);
		Selector selector = Selector.open();
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		ArrayList<SocketChannel> connectedClients = new ArrayList<>();
		
		while(true)
		{
			selector.select();
			Set<SelectionKey> serverKeys = selector.selectedKeys();
			Iterator<SelectionKey> keysIterator = serverKeys.iterator();
			while(keysIterator.hasNext())
			{
				SelectionKey myKey = keysIterator.next();
				if(myKey.isAcceptable())
				{	
					SocketChannel clientSocket = serverSocket.accept();
					configureConnectionBetweenClientSocketAndServer(selector, serverSocket, clientSocket, connectedClients);
				}
				else if(myKey.isWritable())
				{
					SocketChannel clientSocket = (SocketChannel) myKey.channel();
					ByteBuffer output = ByteBuffer.allocate(512);
					clientSocket.read(output);
					String messageFromClient = new String (output.array()).trim();
					byte[] result = messageFromClient.getBytes();
					output = ByteBuffer.wrap(result);
					for(SocketChannel client : connectedClients)
					{
						if(!client.equals(clientSocket))
						{
							log(connectedClients.size() + " size of conneccted clients");
							while(output.hasRemaining())
							client.write(output);	
								output.rewind();
						}
					}
				}
				keysIterator.remove();
			}
		}
	}
	private static ServerSocketChannel configureServerSocketProperties(ServerSocketChannel serverSocket) throws IOException 
	{
		serverSocket.configureBlocking(false);
		InetSocketAddress socketAddress = new InetSocketAddress(1111);
		serverSocket.bind(socketAddress);
		return serverSocket;
	}
	private static void configureConnectionBetweenClientSocketAndServer (Selector selector,
			ServerSocketChannel serverSocket, SocketChannel clientSocket, ArrayList<SocketChannel> listOfClientsConnectedToServer)
			throws IOException, ClosedChannelException 
	{
		clientSocket.configureBlocking(false);
		SelectionKey key2 = clientSocket.register(selector, SelectionKey.OP_WRITE );
		listOfClientsConnectedToServer.add(clientSocket);
		System.out.println("connection accepted " + clientSocket.getLocalAddress() + "\n");
	}
	public static void log(String message)
	{
		System.out.println(message);
	}
}
