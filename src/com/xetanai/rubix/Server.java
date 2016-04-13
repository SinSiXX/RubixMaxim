package com.xetanai.rubix;

import java.util.List;

public class Server {
	private String id;
	private boolean lewd;
	private boolean doMod;
	private int warnCap;
	private String prefix;
	private String welcomeMessage;
	private String goodbyeMessage;
	private boolean doGreet;
	private boolean CNFMessage;
	private boolean allowColor;
	
	public Server(String discordid)
	{
		id = discordid;
		lewd = false;
		doMod = false;
		warnCap = 3;
		prefix = "!";
		doGreet = true;
		welcomeMessage = "";
		goodbyeMessage = "";
		CNFMessage = false;
		allowColor = false;
	}
	
	public Server addOperator(String userid)
	{
		SQLUtils.addOperator(userid, id);
		return this;
	}
	
	public Server removeOperator(String userid)
	{
		SQLUtils.removeOperator(userid, id);
		return this;
	}
	
	public String getWelcome()
	{
		return welcomeMessage;
	}
	
	public Server setWelcome(String newVal)
	{
		welcomeMessage = newVal;
		return this;
	}
	
	public String getFarewell()
	{
		return goodbyeMessage;
	}
	
	public Server setFarewell(String newVal)
	{
		goodbyeMessage = newVal;
		return this;
	}
	
	public boolean doGreet()
	{
		return doGreet;
	}
	
	public Server setGreet(boolean newVal)
	{
		doGreet = newVal;
		return this;
	}
	
	public Server setLewd(boolean newVal)
	{
		lewd = newVal;
		return this;
	}
	
	public Server setMod(boolean newVal)
	{
		doMod = newVal;
		return this;
	}

	public Server setWarnCap(int newVal)
	{
		warnCap = newVal;
		return this;
	}
	
	public String getId()
	{
		return id;
	}
	
	public List<String> getOperators()
	{
		return SQLUtils.getOperators(id);
	}
	
	public boolean isLewd()
	{
		return lewd;
	}
	
	public boolean isModded()
	{
		return doMod;
	}
	
	public int getWarnCap()
	{
		return warnCap;
	}
	
	public Server addBannedWord(String word)
	{
		SQLUtils.addBannedWord(id, word);
		return this;
	}
	
	public List<String> getBannedWords()
	{
		return SQLUtils.getBannedWords(id);
	}
	
	public String getPrefix()
	{
		return prefix;
	}
	
	public Server setPrefix(String newVal)
	{
		prefix = newVal;
		return this;
	}
	
	public boolean doCNFMessage()
	{
		return CNFMessage;
	}
	
	public Server setDoCNF(boolean newval)
	{
		CNFMessage = newval;
		return this;
	}
	
	public boolean allowsColor()
	{
		return allowColor;
	}
	
	public Server setAllowColor(boolean newval)
	{
		allowColor = newval;
		return this;
	}
}
