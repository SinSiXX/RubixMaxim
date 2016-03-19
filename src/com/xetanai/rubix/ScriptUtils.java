package com.xetanai.rubix;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ScriptUtils {
	private TextChannel hostChannel;
	
	public ScriptUtils(MessageReceivedEvent trigger)
	{
		hostChannel = trigger.getTextChannel();
	}
	
	public void say(String msg)
	{
		hostChannel.sendMessage(msg);
	}
}
