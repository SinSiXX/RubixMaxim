package com.xetanai.rubix;

import java.util.ArrayList;
import java.util.List;

public class Server {
	private String id;
	private List<String> operators;
	private boolean lewd;
	private boolean vulgar;
	private int warnCap;
	private List<String> mutedIds;
	private List<String> bannedWords;
	private String prefix;
	private String welcomeMessage;
	private String goodbyeMessage;
	private boolean doGreet;
	
	public Server(String discordid)
	{
		id = discordid;
		operators = new ArrayList<String>();
		lewd = false;
		vulgar = false;
		warnCap = 3;
		mutedIds = new ArrayList<String>();
		bannedWords = new ArrayList<String>();
		prefix = "!";
		doGreet = true;
		welcomeMessage = "";
		goodbyeMessage = "";
	}
	
	public Server addOperator(String userid)
	{
		operators.add(userid);
		return this;
	}
	
	public Server removeOperator(String userid)
	{
		operators.remove(userid);
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
	
	public Server setVulgar(boolean newVal)
	{
		vulgar = newVal;
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
		return operators;
	}
	
	public boolean isLewd()
	{
		return lewd;
	}
	
	public boolean isVulgar()
	{
		return vulgar;
	}
	
	public int getWarnCap()
	{
		return warnCap;
	}
	
	public Server addMutedUser(String id)
	{
		mutedIds.add(id);
		return this;
	}
	
	public Server addBannedWord(String word)
	{
		bannedWords.add(word);
		return this;
	}
	
	public List<String> getMutedUsers()
	{
		return mutedIds;
	}
	
	public List<String> getBannedWords()
	{
		return bannedWords;
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
}
