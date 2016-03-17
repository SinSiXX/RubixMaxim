package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Alias;
import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class GetAliases extends Command {
	private static String keyword = "aliases";
	private static String helpShort = "!aliases <command> - Shows aliases, if any.";
	private static String helpLong = "Usage: !aliases <command>\nIf no command is provided, it will list all aliases for all commands. You can alternatively provide a command name and see all aliases for that command.";
	
	public GetAliases()
	{
		super(helpShort,helpLong,keyword);
	}
	
	@Override
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String[] params = msg.getMessage().getContent().split(" ");
		String post = "";
		if (params.length == 1)
		{
			post+="```";
			for (Alias alias : bot.getAliasList())
				post+=alias.getKeyword() +" is an alias for "+ alias.getCommand().getKeyword() +".\n";
			post+="```";
			msg.getChannel().sendMessage(post);
			return;
		}
		post+=params[1] +" is aliased by:\n```";
		for (Alias alias : bot.getAliasList())
			if (alias.getCommand().getKeyword().equals(params[1]))
				post+=alias.getKeyword() +"\n";
		post=post.substring(0,post.length()-1);
		post+="```";
		sendMessage(bot, msg, post);
	}
}