package com.xetanai.rubix.Commands;

import com.xetanai.rubix.enitites.Chan;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class IgnoreChannel extends Command {
	public IgnoreChannel()
	{
		super("ignorechannel");
		setUsage("ignorechannel");
		setHelp("Ignores/unignores the current channel.",false);
		setHelp("Rubix will toggle ignoring the channel this command is called in.\n"
				+ "Commands and profanity will both be overlooked in ignored channels.",true);
		this.setElevation(true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild) throws Exception
	{
		Chan chn = SQLUtils.loadChannel(msg.getTextChannel().getId());
		if(chn.isIgnored())
		{
			SQLUtils.changeChannel(chn.getId(), "Ignored", "0");
			sendMessage(msg, "This channel is no longer being ignored.");
			return;
		}
		else
		{
			SQLUtils.changeChannel(chn.getId(), "Ignored", "1");
			sendMessage(msg, "This channel is now being ignored.");
			return;
		}
	}
}
