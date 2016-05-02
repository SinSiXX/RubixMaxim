package com.xetanai.rubix.enitites;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.entities.TextChannel;

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
	private String defaultChannel;
	
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
		defaultChannel = "DEFAULT";
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
	
	public Server setDefaultChannel(String newval)
	{
		defaultChannel = newval;
		return this;
	}
	
	public TextChannel getDefaultChannel()
	{
		if(defaultChannel.equals("DEFAULT"))
			return Bot.jda.getGuildById(id).getPublicChannel();
		
		List<TextChannel> chans = Bot.jda.getGuildById(id).getTextChannels();
		
		for(TextChannel ch : chans)
			if(ch.getName().equalsIgnoreCase(defaultChannel))
				return ch;
		
		return Bot.jda.getGuildById(id).getPublicChannel();
	}
}
