package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Alias;
import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class GetAliases extends Command {	
	public GetAliases()
	{
		super("aliases");
		setUsage("aliases <command>");
		setHelp("Shows aliases for commands.",false);
		setHelp("Shows alieses for commands.\n"
				+ "Aliases can be used interchangably with the command's name.\n"
				+ "If no command is provided, it will list all aliases.\n"
				+ "You cannot use an alias to check the aliases of a command.",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		String post = "";
		
		if (params.length == 1)
		{
			int longest = 0;
			for (Alias alias : Bot.aliasList)
				if(alias.getKeyword().length() > longest)
					longest = alias.getKeyword().length();
			
			post+="```cs\n";
			post += "Alias";
			for(int i=0; i < longest - 4; i++)
				post += " ";
			post += "# Command\n";
			post += "###################################\n";
			
			for (Alias alias : Bot.aliasList)
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
		for (Alias alias : Bot.aliasList)
		{
			if (alias.getCommand().getKeyword().equals(params[1]))
			{
				post += alias.getKeyword() +"\n";
			}
		}
		post=post.trim();
		post+="```";
		sendMessage(msg, post);
	}
}