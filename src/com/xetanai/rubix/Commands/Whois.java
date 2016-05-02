package com.xetanai.rubix.Commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Person;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Whois extends Command {
	public Whois()
	{
		super("whois");
		setUsage("whois [user]");
		setHelp("get information on someone.",false);
		setHelp("Get information about a user.\n"
				+ "If no arguments are given, it will get the user who called it.",true);
	}
	
	@Override
	public void onCalled( MessageReceivedEvent msg, String[] params, Server guild)
	{
		if(msg.getMessage().getMentionedUsers().size() > 1)
		{
			sendMessage(msg, "One at a time please, "+ msg.getAuthor().getAsMention() +"!");
			return;
		}
		
		String post = "";
		List<User> users = searchUsers(msg,params);
		if(users.size()==0)
			sendMessage(msg, "I couldn't find anyone with that name. Check your spelling or mention them instead.");
		if(users.size()>1)
		{
			sendMessage(msg, "I found more than one user with that name. Please add a discriminator to their name. (For example: Xetanai#9388)\nI've sent you a PM with the users who came up and their discrims.");
			requestDiscrim(msg,users);
		}
		if(users.size()!=1)
			return;
		
		User usr = users.get(0);
		Person usrP = SQLUtils.loadUser(usr.getId());
		
		post += "Who is **"+ usr.getUsername() +"**?\n```";
		
		post += "Operator: ";
		if(Bot.userIsOp(users.get(0).getId(), msg.getGuild().getId()))
			post += "Yes\n";
		else
			post += "No\n";
		
		post += "Mutual servers: ";
		int ct = 0;
		for(Guild x : Bot.jda.getGuilds())
			if(x.getUsers().contains(usr))
				ct++;
		post += ct +"\n";
		
		post += "Roles: ";
		for(Role x : msg.getGuild().getRolesForUser(usr))
			post += x.getName() +", ";
		post = post.substring(0,post.length()-2) +"\n";
		
		post += "Last heard: ";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date lastHeard = usrP.getLastHeard();
		Date now = new Date();
		
		if(lastHeard==null)
		{
			post += "Never\n";
		}
		else
		{
			post += dateFormat.format(lastHeard) + " (";
			
			long nowL = now.getTime();
			long thenL = lastHeard.getTime();
			
			int timeSince = (int) ((nowL-thenL) /1000);
			int minutes = timeSince / 60;
			int secs = timeSince % 60;
			
			int hours = minutes / 60;
			minutes = minutes % 60;
			
			int days = hours / 24;
			hours = hours % 24;
			
			if(days!=0)
				post += days +" days, ";
			if(hours!=0)
				post += hours +" hours, ";
			if(minutes!=0)
				post += minutes +" minutes, ";
			if(secs!=0)
				post += secs +" seconds, ";
			
			if(timeSince==0)
				post += "Just now)\n";
			else
			{
				post = post.substring(0,post.length()-2);
				post += " ago)\n";
			}
		}
		
		
		int[] fame = SQLUtils.getFame(users.get(0).getId());
		post += "Fame: ";
		if(fame[1]==0)
			post += "100";
		else
		{
			double fameP = (double) fame[0]/(fame[0]+fame[1]);
			post += (int) Math.floor(fameP*100);
		}
		post += "% ("+ (fame[0]+fame[1]) +" votes. +"+ fame[0] +" / -"+ fame[1] +")\n";
		
		
		post = post.trim() + "```";
		post += "**Avatar**: "+ usr.getAvatarUrl();
		
		sendMessage(msg, post);
	}
}
