package com.xetanai.rubix;

import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Commands.Command;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Bot {
	public static JDA jda;
	public static List<Command> commandList = new ArrayList<Command>();
	public static List<Alias> aliasList = new ArrayList<Alias>();
	public static final String version = "2.6.0";
	public static final String ID_XETANAI = "155490847494897664";
	public static final String ID_CROWDHOPPER = "153966430558224384";
	
	public static void registerCommand(Command cmd)
	{
		commandList.add(cmd);
	}
	
	public static void registerAlias(String cmd, String alias)
	{
		Command old = null;
		for (Command x : commandList)
		{
			if (x.getKeyword().equals(cmd))
				old=x;
		}
		
		aliasList.add(new Alias(old,alias));
	}
	
	public static boolean userIsOp(String userid, String serverid)
	{
		if(userid.equals(jda.getGuildById(serverid).getOwnerId()))
			return true;
		
		if(userid.equals("155490847494897664") // Do not question the devs
				|| userid.equals("153966430558224384"))
				return true;
		
		Server srv = SQLUtils.loadServer(serverid);
		
		List<String> ops = srv.getOperators();
		
		return ops.contains(userid);
	}
	
	public static List<String> createErrorMessage(Exception e, MessageReceivedEvent event)
	{
		String post = "An error occurred. Details provided below.```\n"+ e.toString() +"\n";
		
		for(StackTraceElement st : e.getStackTrace())
		{
			post += st.toString() + "\n";
		}
		
		post += "```\nEvent details:";
		
		if(event.getMessage().isPrivate())
		{
			post += "**In a PM**\n";
		}
		else
		{
			post += "In **"+ event.getTextChannel().getName() +"@"+ event.getGuild().getName() +"**\n"
					+ "(Channel ID: **"+ event.getTextChannel().getId() +"** | Guild ID: **"+ event.getGuild().getId() +"**)\n";
		}
		
		post += "Message from: **"+ event.getAuthor().getUsername() +"** (ID: **"+ event.getAuthor().getId() +"**)\n"
				+ "Raw message content: ```\n"+ event.getMessage().getRawContent() +"```";
		
		List<String> shards = new ArrayList<String>();
		while(post.length()!=0)
		{
			if(post.length()<1999)
			{
				shards.add(post);
				post ="";
			}
			else
			{
				shards.add(post.substring(0, 1999));
				post = post.substring(1999);
		
			}
		}
		
		return shards;
	}
	
	public static List<String> createErrorMessage(Exception e)
	{
		String post = "An error occurred. Details provided below.\n```"+ e.getMessage() +"\n";
		
		for(StackTraceElement st : e.getStackTrace())
		{
			post += st.toString() + "\n";
		}
		
		post += "No triggering event was provided. This could be due to errors on startup, or the error isn't directly linked to an event. Either way, I blame Isaak.";
		
		List<String> shards = new ArrayList<String>();
		while(post.length()!=0)
		{
			if(post.length()<1999)
			{
				shards.add(post);
				post ="";
			}
			else
			{
				shards.add(post.substring(0, 1999));
				post = post.substring(1999);
		
			}
		}
		
		return shards;
	}
	
	public static void adminAlert(String message)
	{
		jda.getUserById(ID_XETANAI).getPrivateChannel().sendMessage(message);
		if(!isDev())
			jda.getUserById(ID_CROWDHOPPER).getPrivateChannel().sendMessage(message);
	}
	
	public static void adminAlert(List<String> message)
	{
		for(String shard : message)
		{
			jda.getUserById(ID_XETANAI).getPrivateChannel().sendMessage(shard);
			if(!isDev())
				jda.getUserById(ID_CROWDHOPPER).getPrivateChannel().sendMessage(shard);
		}
	}
	
	public static boolean isDev()
	{
		return Bot.jda.getSelfInfo().getId().equals("168327291879948288");
	}
}