package com.xetanai.rubix.Commands;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.ScriptUtils;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Eval extends Command {
	private static String keyword = "eval";
	private static String helpShort = "say <phrase> - Repeat what you say.";
	private static String helpLong = "say <phrase>\nRepeats exactly what you supply as the parameter.";
	
	public Eval()
	{
		super(helpShort,helpLong,keyword);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		ScriptEngineManager sem = new ScriptEngineManager();
	    ScriptEngine script = sem.getEngineByName("JavaScript");
	    
	    String message = msg.getMessage().getContent();
	    String code = "";
	    if(message.contains("```"))
	    	code = message.substring(message.indexOf("```")+3, message.length()-3);
	    else
	    	code = message.substring(bot.getSettings().getPrefix().length()+4);
	    
	    script.put("rubix", bot);
	    script.put("sutils", new ScriptUtils(msg));
	    
	    try {
			script.eval(code);
		} catch (ScriptException e) {
			sendMessage(bot, msg, "Error evaluating your script.\n```"+ e.getMessage() +"```");
			e.printStackTrace();
		}
	}
}
