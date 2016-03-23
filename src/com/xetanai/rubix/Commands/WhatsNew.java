package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class WhatsNew extends Command {
	private static String keyword = "whatsnew";
	private static String usage = "whatsnew";
	private static String helpShort = "Get differences in dev Rubix.";
	private static String helpLong = "Gets differences in dev Rubix and the current release version.";
	
	public WhatsNew()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String post = "";
		
		post += "***Changes in Rubix V"+ bot.getVersion() +"***:```diff\n";
		post += "---The released version of Rubix will NEVER reply to this command.\n";
		post += "+ The Osu command looks much better.\n";
		post += "+ The Help command looks much better.\n";
		post += "- Fixed the Op command on users with spaces in their name.\n";
		post += "- Fixed a bug in Aliases where the first would be ommitted if multiple existed.\n";
		post += "+ The Aliases command looks much better.\n";
		post += "+ Added a games command which displays your games.\n";
		post += "+ Added pages in help. Use !rd:help page <#> to get a page.\n";
		
		
		post = post.trim()+"```"; /* Get rid of trailing newline and close the code block */
		
		sendMessage(bot, msg, post);
	}
}
