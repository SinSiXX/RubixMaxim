package com.xetanai.rubix.Commands;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.enitites.Server;
import com.xetanai.rubix.utils.ScriptUtils.*;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Eval extends Command {
	public Eval()
	{
		super("eval");
		setUsage("eval <code>");
		setHelp("",false);
		setHelp("",true);
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
	    script.put("event", msg);
	    script.put("sjda", new SJDA(msg.getGuild()));
	    
	    try {
			script.eval(code);
		} catch (ScriptException e) {
			sendMessage(msg, "Error evaluating your script.\n```"+ e.getMessage() +"```");
		}
	}
}
