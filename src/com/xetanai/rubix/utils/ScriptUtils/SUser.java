package com.xetanai.rubix.utils.ScriptUtils;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public class SUser
{
	private static User usr;
	private static Guild g;
	
	public SUser(String id, Guild origin)
	{
		usr = Bot.jda.getUserById(id);
		g = origin;
	}
	
	public SJDA getSJDA(){return new SJDA(g);}
	public String getUsername(){return usr.getUsername();}
	public String getAsMention(){return usr.getAsMention();}
	public String getAvatarURL(){return usr.getAvatarUrl();}
	public String getId(){return usr.getId();}
	public String getCurrentGame(){return usr.getCurrentGame();}
	public String getOnlineStatus(){return usr.getOnlineStatus().toString();}
	public boolean isBot(){return usr.isBot();}
	
	public void sendPM(String s){usr.getPrivateChannel().sendMessage(s);}
}
