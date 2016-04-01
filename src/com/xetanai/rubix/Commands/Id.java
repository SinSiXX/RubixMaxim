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
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild)
	{
		String post = "";
		
		if(params.length==1)
		{
			sendMessage(bot, msg, msg.getAuthor().getAsMention() +", your ID is "+ msg.getAuthor().getId() +".");
			return;
		}
		if(msg.getMessage().getMentionedUsers().size()==0)
		{
			String uname = "";
			for(int i = 1; i < params.length; i++)
			{
				uname += params[i] + "";
			}
			uname=uname.trim();
			
			List<User> possibilities = bot.getJDA().getUsersByName(uname);
			if(possibilities.size() > 1)
			{
				sendMessage(bot, msg, "The user "+ uname +" brought up multiple results. Try @mentioning them.");
				return;
			}
			else if(possibilities.size() == 0)
			{
				sendMessage(bot, msg, "I couldn't finy anyone with that name. Try @mentioning them.");
				return;
			}
			else
			{
				post += possibilities.get(0).getUsername() +"'s ID is "+ possibilities.get(0).getId() +".\n";
			}
		}
		else
		{
			for(User mention : msg.getMessage().getMentionedUsers())
			{
				post += mention.getUsername() +"'s ID is "+ mention.getId() +".\n";
			}
		}
		post = post.trim();
		sendMessage(bot, msg, post);
	}
}
