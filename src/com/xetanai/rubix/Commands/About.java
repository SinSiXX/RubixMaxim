package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class About extends Command {
	private static String keyword = "about";
	private static String helpShort = "!about - Shows various information about the bot.";
	private static String helpLong = "Usage: !about\nShows information about the bot and its current activities. Currently only says the version.";
	
	public About()
	{
		super(helpShort,helpLong,keyword);
	}
	
	@Override
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String post=bot.getSettings().getName() +" "+ bot.getVersion() +"\n";
		
		sendMessage(bot, msg, post);
	}
}
