package com.xetanai.rubix.Commands;

import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Game;
import com.xetanai.rubix.Person;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Games extends Command {
	private static String keyword = "games";
	private static String usage = "games <User>";
	private static String helpShort = "Get the games someone's played.";
	private static String helpLong = "Gets the games the supplied user has played in the order they first played them.";
	
	public Games()
	{
		super(helpShort,helpLong,keyword,usage);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String[] params = msg.getMessage().getRawContent().split(" ");
		String post = "";
		Person target = null;
		
		if(params.length==1)
		{
			target = bot.loadUser(msg.getAuthor().getId());
			post += "__**Your game play time:**__\n";
		}
		else
		{
			List<User> mentioned = msg.getMessage().getMentionedUsers();
			if(mentioned.size() > 1)
			{
				sendMessage(bot, msg, "Please supply a single user.");
				return;
			}
			if(mentioned.size()==1) /* This is the guy. */
				target = bot.loadUser(mentioned.get(0).getId());
			else
			{
				String username = "";
				for(int i = 1; i < params.length; i++)
					username += params[i] + " ";
				username = username.substring(0,username.length()-1);
				List<User> possibleUsers = bot.getJDA().getUsersByName(username);
				
				if(possibleUsers.size()!=1)
				{
					if(possibleUsers.size()==0)
					{
						sendMessage(bot, msg, "Nobody was found with that name! Try @mentioning them.");
						return;
					}
					String posterr = "";
					posterr += "More than one user was found. They are listed below. Try to be more specific, or use an @Mention.```";
					for(User possibility : possibleUsers)
					{
						posterr += "\n* "+ possibility.getUsername();
					}
					posterr += "```";
					sendMessage(bot,msg,posterr);
					return;
				}
				
				if(possibleUsers.size()==1) /* This is the guy. */
					target = bot.loadUser(possibleUsers.get(0).getId());
			}
		post += "__**"+ bot.getJDA().getUserById(target.getId()).getUsername() +"'s game play time:**__\n";
		}
		
		for(Game g : target.getGameList())
			post += g.getName() +" - "+ g.getTimePlayed() +"\n";
		
		sendMessage(bot, msg, post);
	}
}
