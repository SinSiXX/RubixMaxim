package com.xetanai.rubix.Commands;

import com.xetanai.rubix.enitites.Person;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Afk extends Command {
	public Afk()
	{
		super("afk");
		
		setUsage("afk");
		setHelp("Toggles your afk status.",false);
		setHelp("Toggles your afk status. While afk, all messages @mentioning you will be sent to you by PM, and the person who mentioned you will be notified of your status.",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{		
		Person usr = SQLUtils.loadUser(msg.getAuthor().getId());
		if(usr.isAfk())
		{
			sendMessage(msg, msg.getAuthor().getAsMention() +" is no longer afk.");
		}
		else
		{
			sendMessage(msg, msg.getAuthor().getAsMention() +" is now afk. I'll PM them any messages you @mention them in.");
		}
		
		SQLUtils.changeUser(usr.getId(),"Afk",!usr.isAfk());
	}
}
