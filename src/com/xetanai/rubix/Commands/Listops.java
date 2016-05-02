package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Listops extends Command {
	public Listops()
	{
		super("listops");
		setUsage("listops");
		setHelp("List the operators on this server.",false);
		setHelp("Lists all operators on this server.",true);
	}
	
	@Override
	public void onCalled( MessageReceivedEvent msg, String[] params, Server guild)
	{
		String post = "";
		List<String> ops = SQLUtils.getOperators(msg.getGuild().getId());
		
		post += "The operators on this server are: ```";
		
		post += "* "+ Bot.jda.getUserById(msg.getGuild().getOwnerId()).getUsername() +"\n";
		
		for(String id : ops)
			post += "* "+ Bot.jda.getUserById(id).getUsername() +"\n";
		post = post.trim() +"```";
		
		sendMessage(msg, post);
	}
}
