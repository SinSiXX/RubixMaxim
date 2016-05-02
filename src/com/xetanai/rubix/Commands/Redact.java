package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Redact extends Command {
	public Redact()
	{
		super("redact");
		setUsage("redact");
		setHelp("Removes the last message from Rubix.",false);
		setHelp("Removes the last message from Rubix on this server.\n"
				+ "Does not work if his last message was over 100 messages ago.\n"
				+ "Can be used multiple times.",true);
		this.setElevation(true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		MessageHistory hist = new MessageHistory(msg.getChannel());
		List<Message> history = hist.retrieve();
		
		for(Message x : history)
		{
			if(x.getAuthor().equals(Bot.jda.getSelfInfo()))
			{
				x.deleteMessage();
				return;
			}
		}
	}
}
