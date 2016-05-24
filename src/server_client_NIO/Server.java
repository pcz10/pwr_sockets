package server_client_NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		Selector selector = Selector.open();
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1111);
		
		serverSocket.bind(socketAddress);
		serverSocket.configureBlocking(false);
		
		int ops = serverSocket.validOps();
		SelectionKey selectKey = serverSocket.register(selector, ops, null);
		
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
					clientSocket.configureBlocking(false);
					clientSocket.register(selector, SelectionKey.OP_READ);
					System.out.println("connection accepted " + clientSocket.getLocalAddress() + "\n");
				}
				else if(myKey.isReadable())
				{
					SocketChannel clientSocket = (SocketChannel) myKey.channel();
					ByteBuffer buffer = ByteBuffer.allocate(256);
					clientSocket.read(buffer);
					
					String result = new String(buffer.array()).trim();
					System.out.println(result);

					if (result == "end")
						clientSocket.close();
				}
				keysIterator.remove();
			}
		}
	}
}
