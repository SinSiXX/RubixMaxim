package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Server;

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
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		sendMessage(msg, msg.getMessage().getContent().substring(guild.getPrefix().length()+4));
	}
}
