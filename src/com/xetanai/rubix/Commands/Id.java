package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Id extends Command {
	private static String keyword = "id";
	private static String usage = "id <Users>";
	private static String helpShort = "Gets the ID of someone.";
	private static String helpLong = "Gets the ID of the supplied user. If no users are listed, then it gets the ID of whoever called the command.";
	
	public Id()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		String post = "";
		List<String> users = getIdsInParams(msg,params);
		
		if(users==null)
		{
			sendMessage(msg,"I couldn't find anyone with that name. Try @mentioning them.");
			return;
		}
		for(String usrid : users)
		{
			User target = Bot.jda.getUserById(usrid);
			
			post += target.getUsername() +"'s ID is "+ usrid +".\n";
		}
		
		post = post.trim();
		
		sendMessage(msg,post);
	}
}
