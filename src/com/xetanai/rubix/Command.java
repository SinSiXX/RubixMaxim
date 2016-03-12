package com.xetanai.rubix;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public interface Command {
	String helpShort = "";
	String helpLong = "";
	String keyword = "";
	
	public String getHelp(boolean getLong);
	public String getKeyword();
	public boolean onCalled(Bot bot, MessageReceivedEvent message);

}
