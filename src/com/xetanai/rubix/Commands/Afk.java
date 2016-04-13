package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Person;
import com.xetanai.rubix.SQLUtils;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Afk extends Command {
	private static String keyword = "afk";
	private static String usage = "afk";
	private static String helpShort = "Toggles your afk status.";
	private static String helpLong = "Toggles your afk status. While afk, all messages @mentioning you will be sent to you by PM, and the person who mentioned you will be notified of your status.";
	
	public Afk()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		Person usr = SQLUtils.loadUser(msg.getAuthor().getId());
		if(usr.isAfk())
			sendMessage(msg, msg.getAuthor().getAsMention() +" is no longer afk.");
		else
			sendMessage(msg, msg.getAuthor().getAsMention() +" is now afk. I'll PM them any messages you @mention them in.");
		
		SQLUtils.changeUser(usr.getId(),"Afk",!usr.isAfk());
	}
}
