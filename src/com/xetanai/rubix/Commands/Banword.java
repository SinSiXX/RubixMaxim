package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.SQLUtils;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Banword extends Command {
	private static String keyword = "banword";
	private static String usage = "banword <word>";
	private static String helpShort = "Add a word to the filter.";
	private static String helpLong = "Whatever word is given as the parameter will be added to the blacklist and be treated as a vulgar word, with all of the consequences of such.\nThis requires Rubix to be allowed to manage messages to work.";
	
	public Banword()
	{
		super(helpShort,helpLong,keyword,usage);
		this.setElevation(true);
	}
	
	@Override
	public void onCalled( MessageReceivedEvent msg, String[] params, Server guild)
	{
		boolean canRemove = false;
		List<Role> botroles = msg.getGuild().getRolesForUser(Bot.jda.getSelfInfo());
		for(Role x : botroles)
			if(x.hasPermission(Permission.MESSAGE_MANAGE))
				canRemove = true;
		
		Server target = SQLUtils.loadServer(msg.getGuild().getId());
		
		if(!canRemove)
		{
			sendMessage(msg, "I'm not allowed to moderate this guild. To allow me to moderate, give me permission to delete messages.\n"+
							"If you would like me to use a default list of blacklisted words, be sure to also use `"+ target.getPrefix() +"config DoModerate 1`.");
			return;
		}
		if(params.length==1)
		{
			sendMessage(msg, "Please supply a word to blacklist or remove from the blacklist.");
			return;
		}
		
		List<String> currentWords = target.getBannedWords();
		
		if(currentWords.contains(params[1]))
		{
			SQLUtils.removeBannedWord(target.getId(),params[1]);
			sendMessage(msg, "No longer blacklisted.");
		}
		else
		{
			SQLUtils.addBannedWord(target.getId(), params[1]);
			sendMessage(msg, "Added to this guild's blacklist.");
		}
	}
}
