package com.xetanai.rubix.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.MalformedCommandException;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Command {
	String helpLong;
	String helpShort;
	String keyword;
	String usage;
	boolean elevated;
	boolean isnsfw;
	boolean allowPM;
	
	public Command(String hs, String hl, String key, String usg)
	{
		helpLong = hl;
		helpShort = hs;
		keyword = key;
		usage = usg;
		elevated = false;
		isnsfw = false;
		allowPM = false;
	}
	
	public String getHelp(boolean isLong)
	{
		if (isLong)
			return helpLong;
		else
			return helpShort;
	}
	
	public String getHelp()
	{
		return helpShort;
	}
	
	public String getKeyword()
	{
		return keyword;
	}
	
	public boolean isElevated()
	{
		return elevated;
	}
	
	public Command setElevation(boolean newval)
	{
		elevated = newval;
		return this;
	}
	
	public String getUsage()
	{
		return usage;
	}
	
	public boolean isNsfw()
	{
		return isnsfw;
	}
	
	public Command setNsfw(boolean newval)
	{
		isnsfw = newval;
		return this;
	}
	
	public boolean allowPM()
	{
		return allowPM;
	}
	
	public Command setAllowPM(boolean newval)
	{
		allowPM = newval;
		return this;
	}

	public void onCalled(MessageReceivedEvent message, String[] params, Server guild) throws Exception {
		throw new MalformedCommandException();
	}
	
	public Message sendMessage(MessageReceivedEvent event, String message)
	{	
		String[] lines = message.split("\n");
		
		Message ret = null;
		if(lines.length > 20)
		{
			ret = event.getAuthor().getPrivateChannel().sendMessage(message);
			event.getChannel().sendMessage("My response is long, so I PM'd it to you, "+ event.getAuthor().getAsMention() +".");
		}
		else
			message = "\u200B"+ message;
			ret = event.getChannel().sendMessage(message);
			Bot.lastMessage = ret;
		return ret;
	}
	
	public void sendFile(MessageReceivedEvent event, File file, String comment)
	{
		MessageBuilder ret = new MessageBuilder();
		ret.appendString(comment);
		
		Bot.lastMessage = event.getChannel().sendFile(file,ret.build());
	}
	
	public List<String> getIdsInParams(MessageReceivedEvent msg, String[] params)
	{
		List<String> targets = new ArrayList<String>();
		
		if(params.length==1) // No user provided.
			targets.add(msg.getAuthor().getId());
		else if(msg.getMessage().getMentionedUsers().size()!=0) // Users are referenced by mention.
			for(User x : msg.getMessage().getMentionedUsers())
				targets.add(x.getId());
		else // Not mentioned, so only one user.
		{
			String username = "";
			for(int i = 1; i < params.length; i++)
				username += params[i] + " ";
			username = username.substring(0,username.length()-1);
			List<User> possibleUsers = new ArrayList<User>();
			for(User x : msg.getGuild().getUsers())
				if(x.getUsername().equals(username))
					possibleUsers.add(x);
			
			if(possibleUsers.size()!=1)
			{
				return null;
			}
			targets.add(possibleUsers.get(0).getId());
		}
		
		return targets;
	}
	
	public String toString()
	{
		return keyword;
	}
}

