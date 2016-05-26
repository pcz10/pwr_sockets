package server_client_NIO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;


public class ConversationLogs 
{
	public void saveMessage(String message) throws IOException
	{
		try (
				BufferedWriter writer = new BufferedWriter(new FileWriter("history.txt", true))
			)
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			dateFormat.format(date);
			String actualMessage = date + message + "\n";
			if(!(this.uniqueMessage.contains(actualMessage)))
			{
				uniqueMessage.add(actualMessage);
				writer.write(actualMessage+"\n");
			}
		}
	}
	HashSet<String> uniqueMessage = new HashSet<>();
}
