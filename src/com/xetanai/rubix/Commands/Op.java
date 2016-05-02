package com.xetanai.rubix.Commands;

import java.sql.SQLException;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Person;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Op extends Command {
	public Op()
	{
		super("op");
		setUsage("op <user>");
		setHelp("Promotes/demotes a user.",false);
		setHelp("Toggles operator status on a user.\n"
				+ "Operators are allowed to use all commands.",true);
		this.setElevation(true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild) throws SQLException
	{
		List<User> users = searchUsers(msg,params);
		if(msg.getMessage().mentionsEveryone())
		{
			sendMessage(msg,msg.getAuthor().getUsername() +" plz... That's a horrible plan.");
			return;
		}
		if(users.size()==0)
		{
			sendMessage(msg, "I couldn't find anyone by that name. Check your spelling or use a mention.");
			return;
		}
		if(users.get(0).equals(msg.getAuthor())) // SearchUser wasn't given params.
		{
			sendMessage(msg,"Please supply an @Mention or username to Op.");
			return;
		}
		if(msg.getMessage().getMentionedUsers().size() > 1)
		{
			sendMessage(msg, "One at a time please, "+ msg.getAuthor().getAsMention() +"!");
			return;
		}
		if(users.size()>1)
		{
			sendMessage(msg, "I found more than one user with that name. Please add a discriminator to their name. (For example: Xetanai#9388)\nI've sent you a PM with the users who came up and their discrims.");
			requestDiscrim(msg,users);
			return;
		}

		Person newOp = SQLUtils.loadUser(users.get(0).getId());
		Server targetsrv = SQLUtils.loadServer(msg.getGuild().getId());
		
		if(!Bot.userIsOp(newOp.getId(), msg.getGuild().getId()))
		{
			targetsrv.addOperator(newOp.getId());
			sendMessage(msg,users.get(0).getUsername() +" is now an Operator. (Promoted by "+ msg.getAuthor().getUsername() +")");
		}
		else
		{
			if(msg.getGuild().getOwnerId().equals(newOp.getId()))
			{
				sendMessage(msg, "You can't demote the owner!");
				return;
			}
			targetsrv.removeOperator(newOp.getId());
			sendMessage(msg,users.get(0).getUsername() +" is no longer an operator. (Demoted by "+ msg.getAuthor().getUsername() +")");
		}
	}
}
