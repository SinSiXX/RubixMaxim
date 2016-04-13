package com.xetanai.rubix.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.SQLUtils;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Vote extends Command {
	private static String keyword = "vote";
	private static String usage = "vote <username> <+/->";
	private static String helpShort = "Give a positive or negative vote to a user. (PM)";
	private static String helpLong = "This command only works by PM.\nGiven a username and + or -, it will apply a vote to a user.\nThis vote will affect their fame.";
	
	public Vote()
	{
		super(helpShort,helpLong,keyword,usage);
		this.setAllowPM(true);
	}
	
	@Override
	public void onCalled( MessageReceivedEvent msg, String[] params, Server guild)
	{
		if(guild!=null) // This is a PM only command.
		{
			sendMessage(msg, "You shouldn't vote in public. PM your vote to me instead.");

			List<Role> botroles = msg.getGuild().getRolesForUser(Bot.jda.getSelfInfo());
			for(Role x : botroles)
				if(x.hasPermission(Permission.MESSAGE_MANAGE))
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
		
		String username = "";
		String discrim = null;
		for(int i = 1; i < params.length-1; i++)
			username += params[i] + " ";
		username = username.substring(0,username.length()-1);
		
		Pattern discrimFinder = Pattern.compile("#(\\d{4})$");
		Matcher m = discrimFinder.matcher(username);
		
		if(m.find())
		{
			discrim = m.group(0);
			discrim = discrim.substring(1,discrim.length());
			username = username.substring(0,username.length()-5);
		}
		
		List<User> possibleUsers = new ArrayList<User>();
		for(Guild gld : Bot.jda.getGuilds())
			for(User usr : gld.getUsers())
			{
				if(usr.getUsername().equals(username) && !possibleUsers.contains(usr))
				{
					if(discrim != null && usr.getDiscriminator().equals(discrim))
						possibleUsers.add(usr);
					else if(discrim == null)
						possibleUsers.add(usr);
				}
			}
		
		if(possibleUsers.size() == 0)
		{
			sendMessage(msg, "I can't find anyone by that name who shares a server with you.");
			return;
		}
		if(possibleUsers.size() > 1)
		{
			String post = "I found more than one user with that name. Their mutual servers and discrims are as follows. Try the command with their discrim.\n\n";
			
			for(User x : possibleUsers)
			{
				post += "**"+ x.getUsername() +"#"+ x.getDiscriminator() +"**. Common servers:```\n";
				for(Guild gld : Bot.jda.getGuilds())
					if(gld.getUsers().contains(x) && gld.getUsers().contains(msg.getAuthor()))
						post += "* "+ gld.getName() +"\n";
				post = post.trim() +"```Avatar: "+ x.getAvatarUrl() +"\n\n";
			}
			
			sendMessage(msg,post);
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
		
		sendMessage(msg, "Vote successful.");
	}
}
