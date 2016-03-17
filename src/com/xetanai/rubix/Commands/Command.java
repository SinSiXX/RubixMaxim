package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Command {
	String helpLong;
	String helpShort;
	String keyword;
	
	public Command(String hs, String hl, String key)
	{
		helpLong = hl;
		helpShort = hs;
		keyword = key;
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

	public void onCalled(Bot bot, MessageReceivedEvent message) {
		return;
	}
	
	public void sendMessage(Bot bot, MessageReceivedEvent event, String message)
	{
		if (bot.getSettings().PMReplies())
			event.getAuthor().getPrivateChannel().sendMessage(message);
		else
			event.getChannel().sendMessage(message);
	}
	
	public String toString()
	{
		return keyword;
	}
}

