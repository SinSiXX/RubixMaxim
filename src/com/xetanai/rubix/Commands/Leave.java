package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Leave extends Command {
	private static String keyword = "leave";
	private static String usage = "leave";
	private static String helpShort = "Removes me from the server.";
	private static String helpLong = "Removes the bot from the server this command was issued in.";
	
	public Leave()
	{
		super(helpShort,helpLong,keyword,usage);
		this.setElevation(true);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild)
	{
		sendMessage(bot, msg, "Okay, "+ msg.getAuthor().getUsername() +". I'm out! Just DM an invite to have me back.");
		msg.getGuild().getManager().leave();
	}
}
