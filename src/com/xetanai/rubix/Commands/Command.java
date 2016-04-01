package com.xetanai.rubix.Commands;

import java.io.File;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.MalformedCommandException;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Command {
	String helpLong;
	String helpShort;
	String keyword;
	String usage;
	boolean elevated;
	boolean isnsfw;
	
	public Command(String hs, String hl, String key, String usg)
	{
		helpLong = hl;
		helpShort = hs;
		keyword = key;
		usage = usg;
		elevated = false;
		isnsfw = false;
	}
	
	public String getHelp(boolean isLong)
	{
		if (isLong)
			return helpLong;
		else
			return helpShort;
	}
	
	public String getHelp()
	{
		return helpShort;
	}
	
	public String getKeyword()
	{
		return keyword;
	}
	
	public boolean isElevated()
	{
		return elevated;
	}
	
	public Command setElevation(boolean newval)
	{
		elevated = newval;
		return this;
	}
	
	public String getUsage()
	{
		return usage;
	}
	
	public boolean isNsfw()
	{
		return isnsfw;
	}
	
	public Command setNsfw(boolean newval)
	{
		isnsfw = newval;
		return this;
	}

	public void onCalled(Bot bot, MessageReceivedEvent message, String[] params, Server guild) throws Exception {
		throw new MalformedCommandException();
	}
	
	public Message sendMessage(Bot bot, MessageReceivedEvent event, String message)
	{	
		String[] lines = message.split("\n");
		
		Message ret = null;
		if(lines.length > 20)
		{
			ret = event.getAuthor().getPrivateChannel().sendMessage(message);
			event.getChannel().sendMessage("My response is long, so I PM'd it to you, "+ event.getAuthor().getAsMention() +".");
		}
		else
			message = "\u200B"+ message;
			ret = event.getChannel().sendMessage(message);
			bot.setLastMessage(ret);
		return ret;
	}
	
	public Message sendFile(Bot bot, MessageReceivedEvent event, File file)
	{
		Message ret = null;
		
		ret = event.getChannel().sendFile(file,null);
		
		bot.setLastMessage(ret);
		
		return ret;
	}
	
	public void redact(Bot bot)
	{
		bot.getLastMessage().deleteMessage();
	}
	
	public String toString()
	{
		return keyword;
	}
}

