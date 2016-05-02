package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Faq extends Command {
	public Faq()
	{
		super("faq");
		setUsage("faq [tag] [value]");
		setHelp("Set/get faq entries.",false);
		setHelp("Allows operators to set frequently asked questions to throw at users who ask questions they're tired of hearing.\n"
				+ "If no tag is provided, Rubix will list the tags set on this server. Tags may only be one word.\n"
				+ "Is only a tag is supplied, Rubix will say the value of that tag, if it exists.\n"
				+ "If a value is supplied and the person who calls it is an operator, Rubix will set the value of the tag.\n"
				+ "Help will search tags silently if no command matches the argument given.",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		if(params.length==1)
		{
			List<String> faqs = SQLUtils.getAllFaqs(guild.getId());
			String post = "The faq entries for this guild are: ```";
			
			if(faqs.isEmpty())
				post += "NONE```";
			else
			{
				for(String faq : faqs)
					post += faq +", ";
				post = post.substring(0,post.length()-2) +"```";
			}
			
			sendMessage(msg, post);
			return;
		}
		if(params.length==2) // Get their faq, if it exists.
		{
			String post = SQLUtils.getFaq(guild.getId(), params[1]);
			if(post==null)
			{
				sendMessage(msg, "Faq entry not found.");
				return;
			}
			sendMessage(msg, post);
			return;
		}
		
		// Check their permissions.
		
		if(!Bot.userIsOp(msg.getAuthor().getId(), guild.getId()))
		{
			sendMessage(msg, "You are not allowed to create new faq entries.");
			return;
		}
		
		String newfaq = "";
		for(int i=2; i<params.length; i++)
		{
			newfaq += params[i];
			if(i!=params.length-1)
				newfaq += " ";
		}
		
		if(SQLUtils.getFaq(guild.getId(), params[1])!=null)
		{
			if(params[2].equalsIgnoreCase("clear") || params[2].equalsIgnoreCase("remove"))
			{
				SQLUtils.removeFaq(guild.getId(), params[1]);
				sendMessage(msg, "Removed faq entry.");
				return;
			}
			else
			{
				sendMessage(msg, "A faq entry with that name already exists. Use `"+ guild.getPrefix() +"faq "+ params[1] +" clear` to remove it.");
				return;
			}
		}
		
		SQLUtils.addFaq(guild.getId(), params[1], newfaq);
		sendMessage(msg, "Faq entry added successfully.");
	}
}
