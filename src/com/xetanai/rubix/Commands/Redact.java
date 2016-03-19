package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Redact extends Command {
	private static String keyword = "redact";
	private static String helpShort = "redact - Removes the last message from this bot.";
	private static String helpLong = "redact\nMakes the bot remove the last message he sent. Messages older than it are impossible to remove with this. Private messages are also ignored, and therefore impossible to remove.";
	
	public Redact()
	{
		super(helpShort,helpLong,keyword);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		redact(bot);
	}
}
