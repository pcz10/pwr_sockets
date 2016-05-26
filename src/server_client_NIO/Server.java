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
		Logs logger = new Logs();
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		configureServerSocketProperties(serverSocket, logger);
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
					establishConnectionBetweenClientSocketAndServer(selector, serverSocket, clientSocket, connectedClients, logger);
				}
				else if(myKey.isWritable())
				{
					SocketChannel clientSocket = (SocketChannel) myKey.channel();
					ByteBuffer output = ByteBuffer.allocate(512);
					clientSocket.read(output);
					String forLogs = new String(output.array()).trim();
					if(forLogs.length()>0)
						logger.saveServerLog(forLogs);
					output.flip();
					for(SocketChannel client : connectedClients)
					{
						if(!client.equals(clientSocket))
						{
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
	private static ServerSocketChannel configureServerSocketProperties(ServerSocketChannel serverSocket, Logs logger) throws IOException 
	{
		serverSocket.configureBlocking(false);
		InetSocketAddress socketAddress = new InetSocketAddress(1111);
		serverSocket.bind(socketAddress);
		logger.saveServerLog("Server local address: " + serverSocket.getLocalAddress().toString());
		return serverSocket;
	}
	private static void establishConnectionBetweenClientSocketAndServer (Selector selector,
			ServerSocketChannel serverSocket, SocketChannel clientSocket, ArrayList<SocketChannel> listOfClientsConnectedToServer,
			Logs logger)
			throws IOException, ClosedChannelException 
	{
		clientSocket.configureBlocking(false);
		SelectionKey key2 = clientSocket.register(selector, SelectionKey.OP_WRITE );
		listOfClientsConnectedToServer.add(clientSocket);
		String connectionLog = ("Connection accepted " + clientSocket.getLocalAddress() + "\n");
		logger.saveServerLog(connectionLog);
	}
	public static void log(String message)
	{
		System.out.println(message);
	}
	
}
