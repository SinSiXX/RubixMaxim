package com.xetanai.rubix.enitites;

public class Chan {
	private String id;
	private boolean ignored;
	private boolean lewd;
	
	public Chan(String _id)
	{
		id=_id;
		ignored = true;
		lewd = true;
	}
	
	public String getId() {return id;}
	public boolean isIgnored() {return ignored;}
	public boolean isLewd() {return lewd;}
	
	public Chan setIgnored(boolean newval) {ignored = newval;return this;}
	public Chan setLewd(boolean newval) {lewd = newval;return this;}
}
