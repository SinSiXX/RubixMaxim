package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Say extends Command {
	private static String keyword = "say";
	private static String helpShort = "say <phrase> - Repeat what you say.";
	private static String helpLong = "say <phrase>\nRepeats exactly what you supply as the parameter.";
	
	public Say()
	{
		super(helpShort,helpLong,keyword);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		sendMessage(bot, msg, msg.getMessage().getContent().substring(bot.getSettings().getPrefix().length()+4));
	}
}
