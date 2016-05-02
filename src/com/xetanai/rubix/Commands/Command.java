package com.xetanai.rubix.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
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
	List<Permission> neededPerms;
	
	public Command(String _keyword)
	{
		keyword = _keyword;
		elevated = false;
		isnsfw = false;
		allowPM = false;
		neededPerms = new ArrayList<Permission>();
	}
	
	public Command setHelp(String newVal, boolean isLong)
	{
		if(isLong)
			helpLong = newVal;
		else
			helpShort = newVal;
		
		return this;
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
	
	public Command setUsage(String newVal)
	{
		usage = newVal;
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
	
	public void needsPermissionTo(Permission p)
	{
		neededPerms.add(p);
	}
	
	public List<Permission> getPermissions()
	{
		return neededPerms;
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
		return;
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
		{
			message = "\u200B"+ message;
			event.getChannel().sendMessage(message);
		}
		return ret;
	}
	
	public void sendFile(MessageReceivedEvent event, File file, String comment)
	{
		MessageBuilder ret = new MessageBuilder();
		ret.appendString(comment);
		
		event.getChannel().sendFile(file,ret.build());
	}
	
	public List<User> searchUsers(MessageReceivedEvent msg, String[] params)
	{
		List<User> possibleUsers = new ArrayList<User>();
		
		if(params.length==1) // No user provided.
		{
			possibleUsers.add(msg.getAuthor()); // Target self
			return possibleUsers;
		}
		else if(msg.getMessage().getMentionedUsers().size()!=0) // Users are referenced by mention.
			for(User x : msg.getMessage().getMentionedUsers())
				possibleUsers.add(x);
		else // Not mentioned, so only one user.
		{
			String username = "";
			String discrim = null;
			for(int i = 1; i < params.length; i++)
			{
				username += params[i];
				if(i!=params.length-1)
					username += " ";
			}
			
			Pattern discrimFinder = Pattern.compile("#(\\d{4})$");
			Matcher m = discrimFinder.matcher(username);
			
			if(m.find())
			{
				discrim = m.group(0);
				discrim = discrim.substring(1,discrim.length());
				username = username.substring(0,username.length()-5);
			}

			for(User x : msg.getGuild().getUsers())
			{
				if(x.getUsername().toLowerCase().contains(username.toLowerCase()))
					if(discrim != null && x.getDiscriminator().equals(discrim))
					{
						possibleUsers.add(x);
					}
					else if(discrim==null && x.getUsername().toLowerCase().contains(username.toLowerCase()))
					{
						possibleUsers.add(x);
					}	
			}
		}
		
		return possibleUsers;
	}
	
	public List<User> globalSearchUsers(MessageReceivedEvent msg, String[] params)
	{
		List<User> possibleUsers = new ArrayList<User>();
		String username = "";
		String discrim = null;
		for(int i = 1; i < params.length; i++)
		{
			username += params[i];
			if(i!=params.length-1)
				username += " ";
		}
		
		Pattern discrimFinder = Pattern.compile("#(\\d{4})$");
		Matcher m = discrimFinder.matcher(username);
		
		if(m.find())
		{
			discrim = m.group(0);
			discrim = discrim.substring(1,discrim.length());
			username = username.substring(0,username.length()-5);
		}
		
		for(Guild gld : Bot.jda.getGuilds())
			for(User usr : gld.getUsers())
			{
				if(usr.getUsername().toLowerCase().contains(username.toLowerCase()) && !possibleUsers.contains(usr))
				{
					if(discrim != null && usr.getDiscriminator().equals(discrim))
					{
						possibleUsers.add(usr);
					}
					else if(discrim == null)
					{
						possibleUsers.add(usr);
					}
				}
			}
		
		return possibleUsers;
	}
	
	public void requestDiscrim(MessageReceivedEvent msg, List<User> possibleUsers)
	{
		if(possibleUsers.size()>=5)
		{
			msg.getAuthor().getPrivateChannel().sendMessage("I found too many users to list. Try their full username.");
			return;
		}
		String post = "I found more than one user matching. Their mutual servers and discrims are as follows. Try the command with their discrim or full username.\n\n";
		
		for(User x : possibleUsers)
		{
			post += "**"+ x.getUsername() +"#"+ x.getDiscriminator() +"**. Common servers:```\n";
			for(Guild gld : Bot.jda.getGuilds())
				if(gld.getUsers().contains(x) && gld.getUsers().contains(msg.getAuthor()))
					post += "* "+ gld.getName() +"\n";
			post = post.trim() +"```Avatar: "+ x.getAvatarUrl() +"\n\n";
		}
		
		msg.getAuthor().getPrivateChannel().sendMessage(post);
		return;
	}
	
	public String toString()
	{
		return keyword;
	}
}

