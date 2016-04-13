package com.xetanai.rubix.Commands;

import java.sql.SQLException;
import java.util.List;

import com.xetanai.rubix.SQLUtils;
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
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		String guildid = msg.getGuild().getId();
		
		if(params.length==1)
		{
			List<String> cols = SQLUtils.getColumnNames("serversettings");
			String post = "";
			
			post = "**Available settings**:```\n";
			
			for(String x : cols)
				if(!x.equals("DiscordID"))
					post += x + ", ";
			post = post.substring(0,post.length()-2) +"```";
			
			sendMessage(msg,post);
			
			return;
		}
		else if(params.length==2)
		{
			String post = "**Current value**: ";
			String cv = null;
			
			cv = SQLUtils.getSettingVal(guild,params[1]);
			
			if(cv==null)
			{
				sendMessage(msg,"That isn't a valid setting. ***Settings are case-sensitive***.");
				return;
			}
			
			post += cv;
			
			sendMessage(msg,post);
			
			return;
		}
		else
		{
			if(params[1].toLowerCase().equals("discordid"))
			{
				sendMessage(msg, "That setting cannot be changed with this command.");
				return;
			}
			
			try {
				String newValue = "";
				
				for(int i=2; i < params.length; i++)
					newValue += params[i] +" ";
				
				newValue = newValue.trim();

				SQLUtils.changeServer(guildid, params[1], newValue);
				
				sendMessage(msg, "Successfully changed.");
			} catch(SQLException e) {
				sendMessage(msg, "Failed to change that setting.\n```"+ e.toString() +"```");
			}
		}
	}
}
