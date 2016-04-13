package com.xetanai.rubix.Commands;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.ScriptUtils;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Eval extends Command {
	private static String keyword = "eval";
	private static String usage = "eval <code>";
	private static String helpShort = "Evaluates code within code markers.";
	private static String helpLong = "In depth API will be made available once this is in a useful state.";
	
	public Eval()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{
		ScriptEngineManager sem = new ScriptEngineManager();
	    ScriptEngine script = sem.getEngineByName("JavaScript");
	    
	    String message = msg.getMessage().getContent();
	    String code = "";
	    if(message.contains("```javascript\n"))
	    	code = message.substring(message.indexOf("```javascript\n")+13, message.length()-3);
	    else if(message.contains("```"))
	    	code = message.substring(message.indexOf("```")+3, message.length()-3);
	    else
	    	code = message.substring(message.indexOf(" ")+4);
	    
	    script.put("jda", Bot.jda);
	    script.put("sutils", new ScriptUtils(msg));
	    script.put("event", msg);
	    
	    try {
			script.eval(code);
		} catch (ScriptException e) {
			sendMessage(msg, "Error evaluating your script.\n```"+ e.getMessage() +"```");
			e.printStackTrace();
		}
	}
}
