package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Ignore extends Command {
	private static String keyword = "ignore";
	private static String usage = "ignore [User]";
	private static String helpShort = "Make Rubix ignore someone.";
	private static String helpLong = "Toggle Rubix ignoring a user. Commands from them will be ignored entirely.";
	
	public Ignore()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild) throws Exception
	{
		
	}
}
