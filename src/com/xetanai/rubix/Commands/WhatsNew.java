package com.xetanai.rubix.Commands;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Server;

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
	
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild)
	{
		String post = "";
		
		post += "***Changes in Rubix V"+ bot.getVersion() +"***:```diff\n";
		post += "---The released version of Rubix will NEVER reply to this command.\n";
		post += "+ Help has been improved greatly.\n";
		post += "- Fixed the help page count.\n";
		post += "+ Added DM invites. (Available in Rubix Stable).\n";
		post += "+ Added per-server configuration.\n";
		post += "- Fixed unicode causing crashes.\n";
		post += "- Fixed the ID command when @mentioning.\n";
		
		
		post = post.trim()+"```"; /* Get rid of trailing newline and close the code block */
		
		sendMessage(bot, msg, post);
	}
}
