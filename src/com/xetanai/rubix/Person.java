package com.xetanai.rubix;

import net.dv8tion.jda.entities.User;

public class Person {
	private String id;
	private boolean isOp;
	
	public Person(User usr) {
		id = usr.getId();
	}
	
	public User getUser(Bot rubix)
	{
		return rubix.getJDA().getUserById(id);
	}
	
	public Boolean isOp()
	{
		return isOp;
	}
}
