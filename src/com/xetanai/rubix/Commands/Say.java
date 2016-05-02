package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public class Say extends Command {
	public Say()
	{
		super("say");
		setUsage("say <phrase>");
		setHelp("Makes Rubix repeat something.",false);
		setHelp("Rubix will repeat everything after the command.",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		if(params.length==1)
		{
			sendMessage(msg, "Say what?");
			return;
		}
		if(params[1].equals("announcement"))
		{
			if(!msg.getAuthor().getId().equals(Bot.ID_XETANAI))
			{
				sendMessage(msg, "You're not allowed to make global announcements.");
				return;
			}
			
			for(Guild x : Bot.jda.getGuilds())
			{
				if(x.getUsers().size()<1000) // Large servers have enough traffic already.
				{
					String post = "";
					for(int i=2; i<params.length; i++)
					{
						post += params[i];
						if(i<params.length-1)
							post += " ";
					}
					
					Server srv = SQLUtils.loadServer(x.getId());
					if(PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), Permission.MESSAGE_WRITE, srv.getDefaultChannel()))
						srv.getDefaultChannel().sendMessage("__***Message from the developer***__\n"+ post);
					else
						x.getOwner().getPrivateChannel().sendMessage("__***Message from the developer***__\n*I wasn't allowed to post in the default channel, so I PM'd the message to you instead. Please let everyone know.\n"+ post);
				}
			}
			
			return;
		}
		sendMessage(msg, msg.getMessage().getContent().substring(guild.getPrefix().length()+4));
	}
}
