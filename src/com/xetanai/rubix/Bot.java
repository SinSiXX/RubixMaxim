package com.xetanai.rubix;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.JDABuilder;

public class Bot {
	private JDABuilder jdab;
	private List<Command> commandList = new ArrayList<Command>();
	
	
	public Bot(String username, String password) {
		jdab = new JDABuilder().setEmail(username).setPassword(password);

	}
	
	public JDABuilder getJDAB()
	{
		return jdab;
	}
	
	public List<Command> getCommandList()
	{
		return commandList;
	}
	
	public void registerCommand(Command cmd)
	{
		commandList.add(cmd);
	}
}
