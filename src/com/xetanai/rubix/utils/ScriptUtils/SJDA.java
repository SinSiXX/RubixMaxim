package com.xetanai.rubix.utils.ScriptUtils;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.entities.Guild;

public class SJDA
{
	private static Guild origin;
	
	public SJDA(Guild g)
	{
		origin = g;
	}
	public SUser getSelfAsUser(){return new SUser(Bot.jda.getSelfInfo().getId(),origin);}
	public SGuild getGuild(){return new SGuild(origin);}
}
