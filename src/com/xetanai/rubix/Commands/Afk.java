package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Person;

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
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		Person usr = bot.loadUser(msg.getAuthor().getId());
		if(usr.isAfk())
			sendMessage(bot, msg, msg.getAuthor().getAsMention() +" is no longer afk.");
		else
			sendMessage(bot, msg, msg.getAuthor().getAsMention() +" is now afk.");
		
		usr.setAfk();
		bot.saveUser(usr);
	}
}