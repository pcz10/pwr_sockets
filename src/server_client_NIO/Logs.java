package server_client_NIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;


public class Logs 
{
	public Logs(String clientName) throws IOException
	{
		File conversationFilePath = new File("_history_"+clientName+".txt");
		this.conversationFilePath = conversationFilePath;
	}
	public Logs()
	{
		File serverFilePath = new File("SERVER_LOGS_HISTORY.txt");
		this.serverFilePath = serverFilePath;
	}
	public void saveMessage(String message) throws IOException
	{
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.conversationFilePath,true));
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String actualDate =	dateFormat.format(date);
			String actualMessage = message;
			if(!(this.uniqueMessage.contains(actualMessage)))
			{
				uniqueMessage.add(actualMessage);
				writer.write(actualDate);
				writer.newLine();
				writer.write(actualMessage);
				writer.newLine();
			}
			writer.close();
		}
		finally
		{
		}
	}
	public void saveServerLog(String message) throws IOException
	{
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.serverFilePath,true));
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String actualDate =	dateFormat.format(date);
			String log = message;
				writer.write(actualDate);
				writer.newLine();
				writer.write(log);
				writer.newLine();
			writer.close();
		}
		finally
		{
		}
	}
	File serverFilePath;
	File conversationFilePath;
	HashSet<String> uniqueMessage = new HashSet<>();
}
