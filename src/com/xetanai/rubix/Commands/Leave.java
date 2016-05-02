package com.xetanai.rubix.Commands;

import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Leave extends Command {
	public Leave()
	{
		super("leave");
		setUsage("leave");
		setHelp("Removes Rubix from the server.",false);
		setHelp("Removes Rubix from the server.\n"
				+ "To get Rubix back, you must reuse the link used to add him in the first place.",true);
		this.setElevation(true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		sendMessage(msg, "Okay, "+ msg.getAuthor().getUsername() +", I'm out! Goodbye!");
		System.out.println("[Invitation PM] Removed from "+ msg.getGuild().getName() +".");
		msg.getGuild().getManager().leave();
	}
}
