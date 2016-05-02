package com.xetanai.rubix.utils.ScriptUtils;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;

public class SGuild
{
	private static Guild guild;
	
	public SGuild(Guild g)
	{
		guild = g;
	}
	
	public SJDA getSJDA(){return new SJDA(guild);}
	public List<SChannel> getChannels()
	{
		List<SChannel> lst = new ArrayList<SChannel>();
		for(TextChannel x : guild.getTextChannels())
			lst.add(new SChannel(x));
		
		return lst;
	}
}
