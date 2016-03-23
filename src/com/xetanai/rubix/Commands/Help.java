package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Help extends Command {
	private static String keyword = "help";
	private static String usage = "help <command>";
	private static String helpShort = "Shows this list or explains a command.";
	private static String helpLong = "Shows a list of all commands. You can append a command name to get indepth information about that command.";
	
	public Help()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String[] params = msg.getMessage().getContent().split(" ");
		
		int page = 0;
		if(params.length == 1)
			page = 1;
		else if(params[1].equals("page"))
		{
			if(params.length==2)
			{
				sendMessage(bot, msg, "Please supply a page number.");
				return;
			}
			else
				page = Integer.parseInt(params[2]);
			
			if(bot.getCommandList().size()-1 < (page-1)*15)
			{
				sendMessage(bot, msg, "That page number doesnt exist.");
				return;
			}
		}
		if (params.length == 1 || params[1].equals("page"))
		{
			int longest = 0;
			for(int i = (page*15)-15; i < (page*15); i++)
			{
				if(i < bot.getCommandList().size())
				{
					Command cmd = bot.getCommandList().get(i);
					
					if((bot.getSettings().getPrefix() + cmd.getUsage()).length() > longest)
						longest = (bot.getSettings().getPrefix() + cmd.getUsage()).length();
				}
			}
			
			String post = bot.getSettings().getName() +" help.```glsl\n";
			for (int i = (page*15)-14; i < page*15; i++)
			{
				if(i < bot.getCommandList().size())
				{
					Command cmd = bot.getCommandList().get(i);
					
					post+=bot.getSettings().getPrefix() + cmd.getUsage();
					for(int j = 0; j < longest - (bot.getSettings().getPrefix() + cmd.getUsage()).length(); j++)
						post += " ";
					post += " # ";
					if(cmd.getElevated())
						post += "[OP] ";
					post += cmd.getHelp(false) + "\n";
				}
			}
			post += "```\nPage "+ page + "/"+ ((bot.getCommandList().size() / 15)) +". Use "+ bot.getSettings().getPrefix() +"help page <page> to get a specific page.\n";
			post += "Use "+ bot.getSettings().getPrefix() +"help <command> to get indepth information on a command.";
			sendMessage(bot, msg, post);
			return;
		}
		
		
		for (Command cmd : bot.getCommandList())
			if (cmd.getKeyword().equals(params[1]))
			{
				String post ="```";
				if(cmd.getElevated())
					post += "[OPERATOR COMMAND]\n";
				post += "Usage: !"+ cmd.getUsage() +" - "+ cmd.getHelp(true) +"```";
				
				sendMessage(bot, msg, post);
				return;
			}
		return;
	}

}
