package com.xetanai.rubix.Commands;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
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
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();

		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
		
		long uptimeSecs = (mxBean.getUptime() / 1000) % 60;
		long uptimeMins = (mxBean.getUptime() / (1000 * 60)) % 60;
		long uptimeHours = (mxBean.getUptime() / (1000 * 60 * 60)) % 24;
		long uptimeDays = (mxBean.getUptime() / (1000 * 60 * 60 * 24));
		

		String uptime = String.format("%02dd:%02dh:%02dm:%02ds", uptimeDays, uptimeHours, uptimeMins, uptimeSecs);
		
		
		String post="```About "+ Bot.jda.getSelfInfo().getUsername() +"\nRubixMaximus: "+ Bot.version +".\nLibrary: JDA (Java) "+ JDAInfo.VERSION +".\n-----\n";
		
		post += "Guilds: "+ Bot.jda.getGuilds().size() +"\n";
		post += "Unique users: "+ Bot.jda.getUsers().size() +"\n";
		post += "-----\nHere: "+ msg.getTextChannel().getName() +"@"+ msg.getGuild().getName() +"\n";
		post += "Guild ID: "+ msg.getGuild().getId() +"\n";
		post += "-----\nFree memory: "+ format.format(freeMemory / 1024 / 1024) +"MB.\n";
		post += "Allocated memory: "+ format.format(allocatedMemory / 1024 / 1024) +"MB.\n";
		post += "Uptime: "+ uptime +"\n";
		post += "```\nhttps://www.github.com/xetanai/rubixmaxim\n";
		post += "Report bugs to https://discord.gg/0urtMWtPtkhFFShv";
		
		sendMessage(msg, post);
	}
}
