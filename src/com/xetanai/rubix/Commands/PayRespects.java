package com.xetanai.rubix.Commands;

import java.util.Arrays;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class PayRespects extends Command {
	private static String keyword = "payrespects";
	private static String usage = "payrespects";
	private static String helpShort = "Pay your respects to a fallen brother.";
	private static String helpLong = "Sends several messages to call upon a chorus of respect paying from Rubix's mechanical brothers.";
	
	public PayRespects()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild)
	{
		Message last = null;
		sendMessage(bot, msg, "F");
		
		TextChannel chn = msg.getTextChannel();
		
		List<String> cmds = Arrays.asList("/say F","[]eval \"F\"");
		
		for(String cmd : cmds)
		{
			last = chn.sendMessage(cmd);
			last.deleteMessage();
		}
	}
}
