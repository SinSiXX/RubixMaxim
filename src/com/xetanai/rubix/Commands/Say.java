package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Say extends Command {
	private static String keyword = "say";
	private static String usage = "say <phrase>";
	private static String helpShort = "Repeat what you say.";
	private static String helpLong = "Repeats exactly what you supply as the parameter.";
	
	public Say()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		sendMessage(bot, msg, msg.getMessage().getContent().substring(bot.getSettings().getPrefix().length()+4));
	}
}
