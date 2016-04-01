package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Redact extends Command {
	private static String keyword = "redact";
	private static String usage = "redact";
	private static String helpShort = "Removes the last message from this bot.";
	private static String helpLong = "Makes the bot remove the last message he sent. Messages older than it are impossible to remove with this. Private messages are also ignored, and therefore impossible to remove.";
	
	public Redact()
	{
		super(helpShort,helpLong,keyword,usage);
		this.setElevation(true);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild)
	{
		redact(bot);
	}
}
