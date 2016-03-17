package com.xetanai.rubix;

import com.xetanai.rubix.Commands.Command;

public class Alias {
	private Command target;
	private String keyword;
	
	public Alias(Command cmd, String newkey)
	{
		target = cmd;
		keyword = newkey;
	}
	
	public Command getCommand()
	{
		return target;
	}
	
	public String getKeyword()
	{
		return keyword;
	}
}
