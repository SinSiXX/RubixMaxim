package com.xetanai.rubix;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Help implements Command {
	private String keyword = "help";
	private String helpShort = "    !help <command> - Shows this list or explains a command.";
	private String helpLong = "Usage: !help <command>\nShows a list of all commands. You can append a command name to get indepth information about that command.";
	
	public Help() {
	}
	
	public String getHelp(boolean isLong)
	{
		if (isLong)
			return helpLong;
		else
			return helpShort;
	}
	
	public String getKeyword()
	{
		return keyword;
	}
	
	public boolean onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String param = msg.getMessage().getContent().substring(5).trim();
		if (param.equals(""))
		{
			String post = "```";
			for (Command cmd : bot.getCommandList())
				post+=cmd.getHelp(false)+"\n";
			post += "```\nUse !help <command> to get indepth information on a command.";
			msg.getChannel().sendMessage(post);
		}
		for (Command cmd : bot.getCommandList())
			if (cmd.getKeyword().equals(param))
				msg.getChannel().sendMessage("```"+ cmd.getHelp(true) +"```");
		
		return true;
	}

}
