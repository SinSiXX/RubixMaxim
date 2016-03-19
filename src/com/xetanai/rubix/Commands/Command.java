package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Command {
	String helpLong;
	String helpShort;
	String keyword;
	boolean elevated;
	
	public Command(String hs, String hl, String key, boolean isOp)
	{
		helpLong = hl;
		helpShort = hs;
		keyword = key;
		elevated = isOp;
	}
	
	public Command(String hs, String hl, String key)
	{
		helpLong = hl;
		helpShort = hs;
		keyword = key;
		elevated = false;
	}
	
	public String getHelp(boolean isLong)
	{
		if (isLong)
			return helpLong;
		else
			return helpShort;
	}
	
	public String getKeyword()
	{
		return keyword;
	}

	public void onCalled(Bot bot, MessageReceivedEvent message) throws Exception {
		return;
	}
	
	public void sendMessage(Bot bot, MessageReceivedEvent event, String message)
	{	
		String[] lines = message.split("\n");
		if (bot.getSettings().PMReplies())
		{
			bot.setLastMessage(event.getAuthor().getPrivateChannel().sendMessage(message));
			return;
		}
		else
			if(lines.length > 20)
			{
				event.getAuthor().getPrivateChannel().sendMessage(message);
				event.getChannel().sendMessage("My response is long, so I PM'd it to you, "+ event.getAuthor().getAsMention() +".");
			}
			else
				message = "\u200B"+ message;
				bot.setLastMessage(event.getChannel().sendMessage(message));
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

