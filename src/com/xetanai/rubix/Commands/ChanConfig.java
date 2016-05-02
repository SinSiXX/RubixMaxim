package com.xetanai.rubix.Commands;

import java.sql.SQLException;
import java.util.List;

import com.xetanai.rubix.enitites.Chan;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ChanConfig extends Command {
	public ChanConfig()
	{
		super("chanconfig");
		setUsage("chanconfig [setting] [value]");
		setHelp("Change channel settings.",false);
		setHelp("Change how Rubix behaves on the current channel.\n"
				+ "If no setting is provided, Rubix will list the settings available.\n"
				+ "If no value is provided, Rubix will say the current value.",true);
		setElevation(true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		String channelid = msg.getTextChannel().getId();
		Chan chan = SQLUtils.loadChannel(channelid);
		
		if(params.length==1)
		{
			List<String> cols = SQLUtils.getColumnNames("channelsettings");
			String post = "";
			
			post = "**Available settings**:```\n";
			
			for(String x : cols)
				if(!x.equals("ChannelID"))
					post += x + ", ";
			post = post.substring(0,post.length()-2) +"```";
			
			sendMessage(msg,post);
			
			return;
		}
		else if(params.length==2)
		{
			String post = "**Current value**: ";
			String cv = null;
			
			cv = SQLUtils.getSettingVal(chan,params[1]);
			
			if(cv==null)
			{
				sendMessage(msg,"That isn't a valid setting.");
				return;
			}
			
			post += cv;
			
			sendMessage(msg,post);
			
			return;
		}
		else
		{
			if(params[1].equalsIgnoreCase("channelid"))
			{
				sendMessage(msg, "That setting cannot be changed with this command.");
				return;
			}
			String cv = SQLUtils.getSettingVal(chan,params[1]);
			
			if(cv==null)
			{
				sendMessage(msg,"That isn't a valid setting.");
				return;
			}
			
			try {
				String newValue = "";
				
				for(int i=2; i < params.length; i++)
					newValue += params[i] +" ";
				
				newValue = newValue.trim();
				
				SQLUtils.changeChannel(channelid, params[1], newValue);
				
				sendMessage(msg, "Successfully changed.");
			} catch(SQLException e) {
				if(e.toString().contains("Data too long"))
				{
					sendMessage(msg, "Please choose something shorter.");
					return;
				}
				if(e.toString().contains("Incorrect integer value"))
				{
					sendMessage(msg, "You must supply a number.");
					return;
				}
				sendMessage(msg, "Failed to change that setting. Unknown reason. \n```"+ e.toString() +"```");
			}
		}
	}
}
