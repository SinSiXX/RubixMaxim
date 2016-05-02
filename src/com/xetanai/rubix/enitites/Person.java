package com.xetanai.rubix.enitites;


import java.util.Date;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Commands.Command;
import com.xetanai.rubix.utils.SQLUtils;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.utils.PermissionUtil;

public class Person {
	private String id;
	private boolean afk;
	private Date lastHeard;
	
	public Person(String i) {
		id = i;
		afk = false;
		lastHeard = null;
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
		
		SQLUtils.changeUser(id, "Afk", afk);
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
	
	public Person setLastHeard(Date newDate)
	{
		lastHeard = newDate;
		return this;
	}
	
	public Date getLastHeard()
	{
		return lastHeard;
	}
	
	public boolean canUse(Command cmd, Server guild, Chan channel)
	{
		if(guild==null)
			return true; // Show all commands in PM.
		
		for(Permission x : cmd.getPermissions())
			if(!PermissionUtil.checkPermission(Bot.jda.getSelfInfo(), x, Bot.jda.getTextChannelById(channel.getId()))) // Server/channel perms are unavoidable, even for the devs and server owner.
				return false;
		
		if(id.equals("155490847494897664") // Do not question the devs
			|| id.equals("153966430558224384"))
			return true;
		
		if(cmd.getKeyword().equals("eval")) // Eval is only reserved for the devs.
			return false;
		
		if(Bot.jda.getGuildById(guild.getId()).getOwnerId().equals(id))
			return true; // Do not question the server owner
		
		if(cmd.isElevated() && !Bot.userIsOp(id, guild.getId()))
			return false;
		if(cmd.isNsfw() && (!guild.isLewd() || !channel.isLewd()))
			return false;
		
		return true;
	}
}
