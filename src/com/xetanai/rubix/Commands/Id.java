package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Id extends Command {
	public Id()
	{
		super("id");
		setUsage("id <Users>");
		setHelp("gets a user's ID.",false);
		setHelp("Gets the ID of all mentioned users.\n"
				+ "If a username is given without a mention, Rubix will try to find someone with the name you supply, and get their ID.\n"
				+ "If no arguments are given, Rubix will say the ID of whoever called him.",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		String post = "";
		List<User> users = searchUsers(msg,params);
		
		if(users.size()==0)
		{
			sendMessage(msg,"I couldn't find anyone with that name. Try @mentioning them.");
			return;
		}
		if(users.size()>1 && msg.getMessage().getMentionedUsers().size()==0)
		{
			requestDiscrim(msg, users);
			sendMessage(msg, "I found more than one user with that name. Please add a discriminator to their name. (For example: Xetanai#9388)\nI've sent you a PM with the users who came up and their discrims.");
			return;
		}
		
		for(User x : users)
			post += x.getUsername() +"'s ID is "+ x.getId() +".\n";
		
		post = post.trim();
		
		sendMessage(msg,post);
	}
}
