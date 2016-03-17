package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Help extends Command {
	private static String keyword = "help";
	private static String helpShort = "!help <command> - Shows this list or explains a command.";
	private static String helpLong = "Usage: !help <command>\nShows a list of all commands. You can append a command name to get indepth information about that command.";
	
	public Help()
	{
		super(helpShort,helpLong,keyword);
	}
	
	@Override
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String[] params = msg.getMessage().getContent().split(" ");
		
		if (params.length == 1)
		{
			String post = bot.getSettings().getName() +" help.\n```";
			for (Command cmd : bot.getCommandList())
				post+=cmd.getHelp(false)+"\n";
			post += "```\nUse !help <command> to get indepth information on a command.";
			sendMessage(bot, msg, post);
			return;
		}
		for (Command cmd : bot.getCommandList())
			if (cmd.getKeyword().equals(params[1]))
				sendMessage(bot, msg, "```"+ cmd.getHelp(true) +"```");
		
		return;
	}

}
