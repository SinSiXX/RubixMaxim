package com.xetanai.rubix;

import java.util.List;
import java.util.TimerTask;

import net.dv8tion.jda.entities.User;

public class UpdateGames extends TimerTask {
	private Bot bot;
	
	public UpdateGames(Bot r)
	{
		bot = r;
	}

	public void run()
	{
		for(User user : bot.getJDA().getUsers())
		{
			Person usr = bot.loadUser(user.getId());
			if(usr!=null && user.getCurrentGame()!=null)
			{
				List<Game> gameList = usr.getGameList();
				
				boolean hasplayed = false;
				for(Game game : gameList)
				{
					if(game.getName().equals(user.getCurrentGame()))
					{
						game.increment();
						hasplayed = true;
					}
				}
				
				if(!hasplayed && user.getCurrentGame()!=null)
				{
					usr.addGame(user.getCurrentGame());
				}
				
				bot.saveUser(usr);
			}	
		}
	}
}
