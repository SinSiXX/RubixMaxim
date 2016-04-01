package com.xetanai.rubix.Commands;

import java.sql.SQLException;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Config extends Command {
	private static String keyword = "config";
	private static String usage = "config <setting> <value>";
	private static String helpShort = "Change settings.";
	private static String helpLong = "Change most admin settings using this command.";
	
	public Config()
	{
		super(helpShort,helpLong,keyword,usage);
		this.setElevation(true);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild)
	{
		String guildid = msg.getGuild().getId();
		
		if(params.length==1)
		{
			sendMessage(bot, msg, "Valid settings:\n```Lewd, Vulgar, Prefix```");
		}
		else if(params.length==2)
		{
			if(params[1].toLowerCase().equals("lewd"))
			{
				sendMessage(bot, msg, "Currently: **"+ guild.isLewd() +"**");
			}
			if(params[1].toLowerCase().equals("vulgar"))
			{
				sendMessage(bot, msg, "Currently: **"+ guild.isVulgar() +"**");
			}
			if(params[1].toLowerCase().equals("prefix"))
			{
				sendMessage(bot, msg, "Currently: **"+ guild.getPrefix() +"**");
			}
		}
		else
		{
			try {
				String newValue = "";
				
				for(int i=2; i < params.length; i++)
					newValue += params[i] +" ";
				
				newValue = newValue.trim();
				
				bot.changeServer(guildid, params[1], newValue);
			} catch(SQLException e) {
				sendMessage(bot, msg, "Couldn't update the server's SQL entry.\n```"+ e.toString() +"```");
			}
		}
	}
}
