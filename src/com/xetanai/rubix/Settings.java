package com.xetanai.rubix;

public class Settings {
	private String name;
	private boolean deleteCommands;
	private boolean pmReplies;
	
	public Settings() {
		// TODO Auto-generated constructor stub
	}
	
	public Settings setName(String newName)
	{
		name = newName;
		return this;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean deleteCommands()
	{
		return deleteCommands;
	}
	
	public Settings setDeleteCommands(boolean newVal)
	{
		deleteCommands = newVal;
		return this;
	}
	
	public boolean PMReplies()
	{
		return pmReplies;
	}
	
	public Settings setPMReplies(boolean newVal)
	{
		pmReplies = newVal;
		return this;
	}
}
