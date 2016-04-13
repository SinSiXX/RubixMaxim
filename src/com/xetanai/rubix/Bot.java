package com.xetanai.rubix;

import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Message;

public class Bot {
	public static JDA jda;
	public static List<Command> commandList = new ArrayList<Command>();
	public static List<Alias> aliasList = new ArrayList<Alias>();
	public static final String version = "2.4.1";
	public static Message lastMessage;
	
	public static void registerCommand(Command cmd)
	{
		commandList.add(cmd);
	}
	
	public static void registerAlias(String cmd, String alias)
	{
		Command old = null;
		for (Command x : commandList)
		{
			if (x.getKeyword().equals(cmd))
				old=x;
		}
		
		aliasList.add(new Alias(old,alias));
	}
	
	public static boolean userIsOp(String userid, String serverid)
	{
		if(userid.equals(jda.getGuildById(serverid).getOwnerId()))
			return true;
		
		Server srv = SQLUtils.loadServer(serverid);
		
		List<String> ops = srv.getOperators();
		
		return ops.contains(userid);
	}
}