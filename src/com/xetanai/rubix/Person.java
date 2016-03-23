package com.xetanai.rubix;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.entities.User;

public class Person {
	private String id;
	private boolean isOp;
	private boolean afk;
	private List<Game> games;
	
	public Person(User usr) {
		id = usr.getId();
		isOp = false;
		afk = false;
		games = new ArrayList<Game>();
	}
	
	public String getId()
	{
		return id;
	}
	
	public Boolean isOp()
	{
		return isOp;
	}
	
	public Person setOp(boolean newVal)
	{
		isOp = newVal;
		return this;
	}
	
	public Person setAfk()
	{
		if(afk)
			afk=false;
		else
			afk=true;
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
}
