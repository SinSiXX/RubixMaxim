package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;

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
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String[] params = msg.getMessage().getRawContent().split(" ");
		
		if(params.length==1)
		{
			sendMessage(bot, msg, msg.getAuthor().getAsMention() +", your ID is "+ msg.getAuthor().getId() +".");
			return;
		}
		
		String post = "";
		for(int i = 1; i<params.length; i++)
		{
			String user = params[i];
			List<User> usr = bot.getJDA().getUsersByName(user);
			if(usr.size()>1)
				post += "The user "+ user +" brought up multiple results. Try @mentioning for perfect accuracy.\n";
			else if(usr.size()==0)
			{
				User mention = bot.getJDA().getUserById(user.substring(2,user.length()-1));
				if(mention != null)
					post += mention.getUsername() +"'s ID is "+ mention.getId() +".\n";
				else
					post += "I couldn't find the user "+ user +".\n";
			}
			else
				post += usr.get(0).getUsername() +"'s ID is "+ usr.get(0).getId() +".\n";
		}
		post = post.substring(0,post.length()-1); /* Cut off the last newline. */
		sendMessage(bot, msg, post);
	}
}
