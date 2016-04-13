package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.SQLUtils;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Whois extends Command {
	private static String keyword = "whois";
	private static String usage = "whois [user]";
	private static String helpShort = "Get information about a user.";
	private static String helpLong = "Gets information about a user. Will get information about the person who calls it if no user is provided.";
	
	public Whois()
	{
		super(helpShort,helpLong,keyword,usage);
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
		List<String> users = getIdsInParams(msg,params);
		User usr = Bot.jda.getUserById(users.get(0));
		
		post += "Who is **"+ usr.getUsername() +"**?\n```";
		
		post += "Operator: ";
		if(Bot.userIsOp(users.get(0), msg.getGuild().getId()))
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
		
		int[] fame = SQLUtils.getFame(users.get(0));
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
