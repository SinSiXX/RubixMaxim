package com.xetanai.rubix.Commands;

import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Whatgame extends Command {
	private static String keyword = "whatgame";
	private static String helpShort = "whatgame - Tells what everyone's playing.";
	private static String helpLong = "whatgame \nShow what games are being played, and sorts them by most played to least. If more than 20 games are being played, then only the top 5 will be shown.";
	
	public Whatgame()
	{
		super(helpShort,helpLong,keyword);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		List<String> games = new ArrayList<String>();
		List<Integer> count = new ArrayList<Integer>();
		int none=0;
		
		for(User usr : msg.getGuild().getUsers()) /* Only get the games for this server. */
		{
			if(usr.getId() != bot.getJDA().getSelfInfo().getId())
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
		
		if(count.size() > 15)
		{
			post += "The top 15 games being played on this server:```";
			for(int i=0; i<15; i++)
			{
				post += "\n"+ games.get(i) +" - "+ count.get(i) +" playing.";
			}
		}
		else
		{
			post += "Games being played on this server:```";
			for(int i=0; i<count.size(); i++)
			{
				post +="\n"+ games.get(i) +" - "+ count.get(i) +" playing.";
			}
		}
		
		post += "\n\n"+ none +" not playing anything.```";
		
		sendMessage(bot, msg, post);
	}
}
