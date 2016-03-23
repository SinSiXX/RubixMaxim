package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Alias;
import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class GetAliases extends Command {
	private static String keyword = "aliases";
	private static String usage = "aliases <command>";
	private static String helpShort = "Shows aliases, if any.";
	private static String helpLong = "If no command is provided, it will list all aliases for all commands. You can alternatively provide a command name and see all aliases for that command.";
	
	public GetAliases()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	@Override
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String[] params = msg.getMessage().getContent().split(" ");
		String post = "";
		
		if (params.length == 1)
		{
			int longest = 0;
			for (Alias alias : bot.getAliasList())
				if(alias.getKeyword().length() > longest)
					longest = alias.getKeyword().length();
			
			post+="```cs\n";
			post += "Alias";
			for(int i=0; i < longest - 4; i++)
				post += " ";
			post += "# Command\n";
			post += "###################################\n";
			
			for (Alias alias : bot.getAliasList())
			{
				post+=alias.getKeyword();
				for(int i=0; i < longest - alias.getKeyword().length(); i++)
					post += " ";
				post += " # "+ alias.getCommand().getKeyword() +"\n";
			}
			post+="```";
			msg.getChannel().sendMessage(post);
			return;
		}
		post+="**"+ params[1] +"** is aliased by the following commands:```\n";
		for (Alias alias : bot.getAliasList())
		{
			if (alias.getCommand().getKeyword().equals(params[1]))
			{
				post += alias.getKeyword() +"\n";
			}
		}
		post=post.trim();
		post+="```";
		sendMessage(bot, msg, post);
	}
}