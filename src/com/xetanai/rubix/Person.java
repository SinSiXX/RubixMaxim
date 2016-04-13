package com.xetanai.rubix;

import java.util.List;

import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;

public class Person {
	private String id;
	private boolean afk;
	
	public Person(String i) {
		id = i;
		afk = false;
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
	
	public boolean canUse(Command cmd, Server guild)
	{
		if(guild==null)
			return true; // Show all commands in PM.
		
		if(Bot.jda.getGuildById(guild.getId()).getOwnerId().equals(id)
				|| id.equals("155490847494897664")
				|| id.equals("153966430558224384"))
			return true; // Do not question the owner nor the devs.
		
		if(cmd.isElevated() && !guild.getOperators().contains(id))
			return false;
		
		if(cmd.isNsfw() && !guild.isLewd())
			return false;
		
		return true;
	}
	
	public boolean can(Permission perm, Guild guild)
	{
		boolean can = false;
		
		List<Role> roles = guild.getRolesForUser(Bot.jda.getUserById(id));
		for(Role x : roles)
			if(x.hasPermission(perm))
				can = true;
		
		return can;
	}
}
