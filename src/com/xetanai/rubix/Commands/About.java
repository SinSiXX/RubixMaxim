package com.xetanai.rubix.Commands;

import java.text.NumberFormat;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.JDAInfo;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class About extends Command {
	private static String keyword = "about";
	private static String usage = "about";
	private static String helpShort = "Shows various information about the bot.";
	private static String helpLong = "about\nShows information about the bot and its current activities.";
	
	public About()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild)
	{
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();

		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		
		
		String post="```About "+ bot.getJDA().getSelfInfo().getUsername() +"\nRubixMaximus: "+ bot.getVersion() +".\nLibrary: JDA (Java) "+ JDAInfo.VERSION +".\n-----\n";
		
		post += "Guilds: "+ bot.getJDA().getGuilds().size() +"\n";
		post += "Unique users: "+ bot.getJDA().getUsers().size() +"\n";
		post += "-----\nHere: "+ msg.getTextChannel().getName() +"@"+ msg.getGuild().getName() +"\n";
		post += "-----\nFree memory: "+ format.format(freeMemory / 1024 / 1024) +"MB.\n";
		post += "Allocated memory: "+ format.format(allocatedMemory / 1024 / 1024) +"MB.";
		post += "```\nhttps://www.github.com/xetanai/rubixmaxim";
		post += "Report bugs to https://discord.gg/0urtMWtPtkhFFShv";
		
		sendMessage(bot, msg, post);
	}
}
