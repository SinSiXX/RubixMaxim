package com.xetanai.rubix.utils.ScriptUtils;

import net.dv8tion.jda.entities.TextChannel;

public class SChannel
{
	private static TextChannel chan;
	
	public SChannel(TextChannel ch)
	{
		chan = ch;
	}
	
	public SJDA getSJDA(){return new SJDA(chan.getGuild());}
	public String getName(){return chan.getName();}
	public String getAsMention(){return chan.getAsMention();}
	public String getId(){return chan.getId();}
	public String getTopic(){return chan.getTopic();}
	
	public void sendMessage(String s){chan.sendMessage(s);}
}
