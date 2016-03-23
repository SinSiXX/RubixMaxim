package com.xetanai.rubix;

public class Game {
	private String name;
	private int seconds;
	
	public Game(String nm)
	{
		name = nm;
		seconds = 0;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getRawTimePlayed()
	{
		return seconds;

	}
	
	public String getTimePlayed()
	{
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int secs = 0;
		
		minutes = seconds / 60;
		secs = seconds % 60;
		
		hours = minutes / 60;
		minutes = minutes % 60;
		
		days = hours / 24;
		hours = hours % 24;
		
		String returned = "";
		if(days>=1)
			returned += "**"+ days +"** days, ";
		if(hours>=1)
			returned += "**"+ hours +"** hours, ";
		returned += "**"+minutes+"** minutes, and **"+secs+"** seconds.";
		return returned;
	}
	
	public Game increment()
	{
		seconds += 5;
		return this;
	}
}