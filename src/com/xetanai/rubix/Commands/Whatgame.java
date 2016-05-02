package com.xetanai.rubix.Commands;

import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Whatgame extends Command {
	public Whatgame()
	{
		super("whatgame");
		setUsage("whatgame");
		setHelp("Tells what games this server's playing.",false);
		setHelp("Lists the games users on this server are playing, in order from most players to least.\n"
				+ "If over 15 games are being played, then only the top 15 will be shown.\n"
				+ "Bots are ignored.",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		List<String> games = new ArrayList<String>();
		List<Integer> count = new ArrayList<Integer>();
		int none=0;
		
		for(User usr : msg.getGuild().getUsers()) /* Only get the games for this server. */
		{
			if(usr.getId() != Bot.jda.getSelfInfo().getId() && !usr.isBot() && !usr.getOnlineStatus().equals(OnlineStatus.OFFLINE))
			{
				if(usr.getCurrentGame() == null)
					none+=1;
				else
				{
					if(games.contains(usr.getCurrentGame()))
						count.set(games.indexOf(usr.getCurrentGame()), count.get(games.indexOf(usr.getCurrentGame()))+1);
					else
					{
						games.add(usr.getCurrentGame());
						count.add(1);
					}
				}
			}
		}
		/* DONE GETTING GAMES */
		/* START SORTING */
		for(int i=0; i<count.size(); i++)
		{
			int largestIndex = i;
			for(int j=i+1; j<count.size(); j++)
				if(count.get(j) > count.get(largestIndex))
					largestIndex = j;
			int tempCount = count.get(largestIndex);
			String tempName = games.get(largestIndex);
			
			count.set(largestIndex, count.get(i));
			games.set(largestIndex, games.get(i));
			
			count.set(i, tempCount);
			games.set(i, tempName);
		}
		
		String post="";
		
		int longest = 0;
		int iMax = 0;
		
		if(games.size() > 15)
			iMax = 15;
		else
			iMax = games.size();
		
		for(int i = 0; i < iMax; i++)
		{
			String game = games.get(i);
			if(game.length() > longest)
				longest = game.length();
		}
		
		if(count.size() > 15)
		{
			post += "__**The top 15 games being played on this server**__:```glsl\n";
			for(int i=0; i<15; i++)
			{
				post += games.get(i);
				
				for(int j = 0; j < longest - games.get(i).length(); j++)
					post += " ";
				
				post+=" # "+ count.get(i) +" playing.\n";
			}
		}
		else
		{
			post += "Games being played on this server:```glsl\n";
			for(int i=0; i<count.size(); i++)
			{
				post += games.get(i) +" # "+ count.get(i) +" playing.\n";
			}
		}
		post=post.trim();
		
		post += "\n\n"+ none +" not playing anything.```";
		
		sendMessage(msg, post);
	}
}
