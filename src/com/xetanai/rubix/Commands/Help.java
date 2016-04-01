package com.xetanai.rubix.Commands;

import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Person;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Help extends Command {
	private static String keyword = "help";
	private static String usage = "help <command or page>";
	private static String helpShort = "Shows this list or explains a command.";
	private static String helpLong = "Shows a list of all commands. You can append a command name to get indepth information about that command.";
	
	public Help()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	@Override
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild)
	{
		int page = 0;
		int pageTotal;
		int longest = 0;
		String post;
		List<Command> listed = null;
		
		if(params.length==2) // Either a page number or command.
		{
			if(params[1].matches("^[0-9]+")) // Is a page number.
				page = Integer.parseInt(params[1]);
			else // Is a command
			{
				for(Command cmd : bot.getCommandList())
					if(cmd.getKeyword().equals(params[1]))
					{
						post = "```Usage: "+ cmd.getUsage() + "\n";
						if(cmd.isElevated())
							post += "Requires Operator.\n";
						if(cmd.isNsfw())
							post += "Requires guild to allow nsfw commands.\n";
						post += cmd.getHelp(true) +"```";
						
						sendMessage(bot, msg, post);
						return;
					}
				sendMessage(bot, msg, "That command wasn't found.");
				return;
			}
		}
		
		Person user = bot.loadUser(msg.getAuthor().getId());
		
		listed = new ArrayList<Command>();
		for(Command cmd : bot.getCommandList())
			if(user.can(bot, cmd, guild))
			{
				listed.add(cmd);
				if(cmd.getUsage().length() >  longest)
					longest = cmd.getUsage().length();
			}
		
		pageTotal = 1 + ((listed.size()-1)/15); // 15 commands per page.
		
		post = "Commands available to you (This may be different for others):```glsl\n";
		
		if(page == 0)
			page = 1;
		if(page>pageTotal)
		{
			sendMessage(bot, msg, "That page doesn't exist.");
			return;
		}
		
		for(int i = (page-1)*15; i < page*15; i++)
		{
			if(i>=listed.size()) // End of the list.
				break;
			
			Command cmd = listed.get(i);
			
			post += guild.getPrefix() + cmd.getUsage();
			for(int j = 0; j < longest - cmd.getUsage().length(); j++)
				post += " ";
			post += " # "+ cmd.getHelp() +"\n";
		}
		post += "```\nPage "+ page +"/"+ pageTotal +" - Use "+ guild.getPrefix() +"help <page> to get another page.\n"
				+ "Use "+ guild.getPrefix() +"help <command> to get help with a command.";
		
		sendMessage(bot, msg, post);
		return;
	}
}
