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
		Selector selector = Selector.open();
		ServerSocketChannel serverSocket = configureServerSocketProperties();
		registerServerSocket(selector, serverSocket);
		
		ArrayList<SocketChannel> connectedClients = new ArrayList<>();
		
		while(true)
		{
			selector.select();
			Set<SelectionKey> serverKeys = selector.selectedKeys();
			Iterator<SelectionKey> keysIterator = serverKeys.iterator();
			
			while(keysIterator.hasNext())
			{
				SelectionKey myKey = keysIterator.next();
				SocketChannel clientSocket;
				selector.selectNow();
				
				if(myKey.isAcceptable())
					configureConnectionBetweenClientSocketAndServer(selector, serverSocket, connectedClients);
				
				if(myKey.isReadable())
				{
					clientSocket = (SocketChannel) myKey.channel();
					ByteBuffer buffer = Client.buffer;
					clientSocket.read(buffer);
					String messageFromClient = new String (buffer.array()).trim();
					
					buffer.clear();
					byte[] result = messageFromClient.getBytes();
					buffer = ByteBuffer.wrap(result);
					
					for(SocketChannel client : connectedClients)
					{
						String whatBuffersSends = new String(buffer.array()).trim();
						int size = connectedClients.size();
						log("\nKLASA SERVER, rozmiar kontenera klientow : "+size);
						log("\nKLASA SERVER. Wysylam do klienta tekst: "+ whatBuffersSends);
					
						client.write(buffer);	
						buffer.rewind();
						//buffer.flip();
					}
				}
				keysIterator.remove();
			}
		}
	}
	
	private static void registerServerSocket(Selector selector, ServerSocketChannel serverSocket) throws ClosedChannelException 
	{
		int ops = serverSocket.validOps();
		SelectionKey selectKey = serverSocket.register(selector, ops, null);
	}
	private static ServerSocketChannel configureServerSocketProperties() throws IOException {
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		InetSocketAddress socketAddress = new InetSocketAddress(1111);
		serverSocket.bind(socketAddress);
		serverSocket.configureBlocking(false);
		return serverSocket;
	}
	private static void configureConnectionBetweenClientSocketAndServer (Selector selector,
			ServerSocketChannel serverSocket, ArrayList<SocketChannel> listOfClientsConnectedToServer)
			throws IOException, ClosedChannelException {
		SocketChannel clientSocket;
		clientSocket = serverSocket.accept();
		clientSocket.configureBlocking(false);
		clientSocket.register(selector, SelectionKey.OP_READ );
		listOfClientsConnectedToServer.add(clientSocket);
		System.out.println("connection accepted " + clientSocket.getLocalAddress() + "\n");
	}
	public static void log(String message)
	{
		System.out.println(message);
	}
}
