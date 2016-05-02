package com.xetanai.rubix.Commands;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public class Vote extends Command {
	public Vote()
	{
		super("vote");
		setUsage("vote <username> <+/->");
		setHelp("Votes on a user, affecting their fame. PM only.",false);
		setHelp("This command only works by PM.\n"
				+ "Given a username and a + or -, votes on a user.\n"
				+ "This will affect their fame, as shown in `whois`.\n"
				+ "As this command only works in PM, you cannot @mention a user to vote on them.",true);
		this.setAllowPM(true);
	}
	
	@Override
	public void onCalled( MessageReceivedEvent msg, String[] params, Server guild)
	{
		if(guild!=null) // This is a PM only command.
		{
			sendMessage(msg, "Votes made in public are ***ignored*** and have no effect. PM your vote to me instead.");

			if(PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), Permission.MESSAGE_MANAGE, msg.getTextChannel()))
				msg.getMessage().deleteMessage();
			return;
		}
		if(params.length<3)
		{
			sendMessage(msg, "You must supply a username and a plus or minus.");
			return;
		}
		if(!params[params.length-1].equals("+") && !params[params.length-1].equals("-"))
		{
			sendMessage(msg, "You must supply a plus or minus to indicate a positive or negative vote.");
		}
		
		List<User> possibleUsers = globalSearchUsers(msg, ArrayUtils.remove(params,params.length-1));
		
		if(possibleUsers.size() == 0)
		{
			sendMessage(msg, "I can't find anyone like that who shares a server with you.");
			return;
		}
		if(possibleUsers.size() > 1)
		{
			requestDiscrim(msg, possibleUsers);
			return;
		}
		
		if(possibleUsers.get(0).equals(msg.getAuthor()))
		{
				sendMessage(msg, "You can't vote on yourself.");
				return;
		}
		if(possibleUsers.get(0).equals(Bot.jda.getSelfInfo()))
		{
			sendMessage(msg, "You can't vote on me.");
			return;
		}
		
		if(params[params.length-1].equals("+"))
			SQLUtils.vote(msg.getAuthor().getId(), possibleUsers.get(0).getId(), true);
		else
			SQLUtils.vote(msg.getAuthor().getId(), possibleUsers.get(0).getId(), false);
		
		sendMessage(msg, "Voted on "+ possibleUsers.get(0).getUsername() +" successfully.");
	}
}
