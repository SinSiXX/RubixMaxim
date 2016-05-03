package com.xetanai.rubix.Commands;

import java.awt.Event;
import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Chan;
import com.xetanai.rubix.enitites.Person;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public class Help extends Command {
	public Help()
	{
		super("help");
		setUsage("help <command or page>");
		setHelp("Shows this list, or explains a command in detail.",false);
		setHelp("Shows a list of all commands.\n"
				+ "Only 15 will be shown per page, and only commands the person who called it can use are shown.\n"
				+ "If a command name is given, it will show indepth information on how to use the command, like this.\n"
				+ "If a page number is given, it will get that page in the list of commands.",true);
		this.setAllowPM(true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		String prefix = "!";
		if(guild!=null)
			prefix = guild.getPrefix();
		
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
				for(Command cmd : Bot.commandList)
					if(cmd.getKeyword().equals(params[1]))
					{
						post = "Usage: *"+ guild.getPrefix() + cmd.getUsage() + "*\n";
						if(cmd.isElevated())
							post += "**Requires Operator**.\n";
						if(cmd.isNsfw())
							post += "**Requires AllowLewd setting to be 1**.\n";
						
						for(Permission x : cmd.getPermissions())
						{
							post += "**Requires permission**: ***"+ x.name().replace("_", " ") +"*** (";
							if(PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), x, msg.getTextChannel()))
								post += "\u2713";
							else
								post += "\u2717";
							post += ")\n";
						}
						post += "\n";
						post += cmd.getHelp(true);
						
						sendMessage(msg, post);
						return;
					}
				
				/* Search FAQ */
				String faq = SQLUtils.getFaq(guild.getId(), params[1]);
				if(faq!=null)
				{
					sendMessage(msg,faq);
					return;
				}
				
				sendMessage(msg, "That command wasn't found.");
				return;
			}
		}
		
		Person user = SQLUtils.loadUser(msg.getAuthor().getId());
		Chan channel = null;
		if(!msg.isPrivate());
			channel = SQLUtils.loadChannel(msg.getTextChannel().getId());
		
		listed = new ArrayList<Command>();
		for(Command cmd : Bot.commandList)
			if(channel==null || user.canUse(cmd, guild, channel))
			{
				listed.add(cmd);
				if(cmd.getUsage().length() >  longest)
					longest = cmd.getUsage().length();
			}
		
		pageTotal = 1 + ((listed.size()-1)/15); // 15 commands per page.
		
		if(guild!=null)
			post = "Commands available to you (This may be different for others):\n";
		else
			post = "Full command list. (Most cannot be used in PM):\n";
		
		if(page == 0)
			page = 1;
		if(page>pageTotal)
		{
			sendMessage(msg, "That page doesn't exist.");
			return;
		}
		
		for(int i = (page-1)*15; i < page*15; i++)
		{
			if(i>=listed.size()) // End of the list.
				break;
			
			Command cmd = listed.get(i);
			
			post += "`"+ prefix + cmd.getUsage();
			for(int j = 0; j < longest - cmd.getUsage().length(); j++)
			{
				post += " ";
			}
			post += " -` "+ cmd.getHelp() +"\n";
		}
		post += "\nPage ***"+ page +"/"+ pageTotal +"*** - Use **"+ prefix +"help <page>** to get another page.\n"
				+ "Use **"+ prefix +"help <command>** to get help with a command.";
		
		sendMessage(msg, post);
		return;
	}
}
