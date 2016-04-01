package com.xetanai.rubix;

import java.util.ArrayList;
import java.util.List;

import com.xetanai.rubix.Commands.Command;

public class Person {
	private String id;
	private boolean afk;
	private List<Game> games;
	
	public Person(String i) {
		id = i;
		afk = false;
		games = new ArrayList<Game>();
	}
	
	public String getId()
	{
		return id;
	}
	
	public Person setAfk()
	{
		if(afk)
			afk=false;
		else
			afk=true;
		return this;
	}
	
	public Person setAfk(boolean newval)
	{
		afk = newval;
		return this;
	}
	
	public boolean isAfk()
	{
		return afk;
	}
	
	public Person addGame(String newgame)
	{
		games.add(new Game(newgame));
		return this;
	}
	
	public List<Game> getGameList()
	{
		return games;
	}
	
	public boolean can(Bot bot, Command cmd, Server guild)
	{
		if(bot.getJDA().getGuildById(guild.getId()).getOwnerId().equals(id) || id.equals("155490847494897664"))
			return true; // Do not question the owner nor Xetanai.
		
		if(cmd.isElevated() && !guild.getOperators().contains(id))
			return false;
		
		if(cmd.isNsfw() && !guild.isLewd())
			return false;
		
		return true;
	}
}
