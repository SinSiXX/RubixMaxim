package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.SQLUtils;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Listops extends Command {
	private static String keyword = "listops";
	private static String usage = "listops";
	private static String helpShort = "List Rubix operators on this server.";
	private static String helpLong = "Prints out a list of all operators on this server.";
	
	public Listops()
	{
		super(helpShort,helpLong,keyword,usage);
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
