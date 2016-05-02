package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public class Banword extends Command {
	public Banword()
	{
		super("banword");
		setUsage("banword <word>");
		setHelp("Add a word to the filter.",false);
		setHelp("Whatever word is given as the parameter will be added to the blacklist and be treated as a vulgar word, with all of the consequences of such.\n"
				+ "This requires Rubix to be allowed to manage messages to work.",true);
		setElevation(true);
		needsPermissionTo(Permission.MESSAGE_MANAGE);
	}
	
	@Override
	public void onCalled( MessageReceivedEvent msg, String[] params, Server guild)
	{
		if(!PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), Permission.MESSAGE_MANAGE, msg.getGuild()))
		{
			sendMessage(msg, "I'm not allowed to moderate this guild. To allow me to moderate, give me permission to delete messages.");
			return;
		}
		
		Server target = SQLUtils.loadServer(msg.getGuild().getId());

		if(params.length==1)
		{
			sendMessage(msg, "Please supply a word to blacklist or remove from the blacklist.");
			return;
		}
		
		List<String> currentWords = target.getBannedWords();
		
		String phrase = "";
		for(int i = 1; i < params.length; i++)
		{
			phrase += params[i];
			if(i!=params.length-1)
				phrase += " ";
		}
		
		if(currentWords.contains(params[1]))
		{
			SQLUtils.removeBannedWord(target.getId(),phrase.toLowerCase());
			sendMessage(msg, "No longer blacklisted.");
		}
		else
		{
			SQLUtils.addBannedWord(target.getId(),phrase.toLowerCase());
			sendMessage(msg, "Added to this guild's blacklist.");
		}
	}
}
