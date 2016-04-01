package com.xetanai.rubix.Commands;

import java.sql.SQLException;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Person;
import com.xetanai.rubix.Server;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Op extends Command {
	private static String keyword = "op";
	private static String usage = "op <User>";
	private static String helpShort = "Promotes a user to operator.";
	private static String helpLong = "Promotes a user to operator. Operators are allowed to use all commands, including some that can terminate the bot, so be careful who you promote!";
	
	public Op()
	{
		super(helpShort,helpLong,keyword,usage);
		this.setElevation(true);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg, String[] params, Server guild) throws SQLException
	{
		if(msg.getMessage().mentionsEveryone())
		{
			sendMessage(bot,msg,msg.getAuthor().getUsername() +" plz... That's a horrible plan.");
			return;
		}
		else if(params.length==1)
		{
			sendMessage(bot,msg,"Please supply an @Mention or username to Op.");
			return;
		}
		if(msg.getMessage().getMentionedUsers().size() > 1)
		{
			sendMessage(bot, msg, "One at a time please! Operator status isn't candy, "+ msg.getAuthor().getAsMention() +"!");
			return;
		}
		
		List<User> mentioned = msg.getMessage().getMentionedUsers();
		if(mentioned.size()==1) /* This is the guy. */
		{
			Person newOp = bot.loadUser(mentioned.get(0).getId());
			Server targetsrv = bot.loadServer(msg.getGuild().getId());
			
			if(!bot.isOp(newOp.getId(), msg.getGuild().getId()))
			{
				targetsrv.addOperator(newOp.getId());
				String newVal = "";
				
				for(String entry : targetsrv.getOperators())
				{
					newVal += entry +",";
				}
				
				bot.changeServer(targetsrv.getId(), "Operators", newVal);
				sendMessage(bot,msg,mentioned.get(0).getAsMention() +" is now an Operator. (Promoted by "+ msg.getAuthor().getUsername() +")");
			}
			else
			{
				targetsrv.removeOperator(newOp.getId());
				String newVal = "";
				
				for(String entry : targetsrv.getOperators())
				{
					newVal += entry +",";
				}
				
				bot.changeServer(targetsrv.getId(), "Operators", newVal);
				sendMessage(bot,msg,mentioned.get(0).getAsMention() +" is no longer an Operator. (Demoted by "+ msg.getAuthor().getUsername() +")");
			}
			return;
		}

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
			String post = "";
			post += "More than one user was found. They are listed below. Try to be more specific, or use an @Mention.```";
			for(User possibility : possibleUsers)
			{
				post += "\n* "+ possibility.getUsername();
			}
			post += "```";
			sendMessage(bot,msg,post);
			return;
		}
		
		if(possibleUsers.size()==1) /* This is the guy. */
		{
			Person newOp = bot.loadUser(possibleUsers.get(0).getId());
			Server targetsrv = bot.loadServer(msg.getGuild().getId());
			
			if(!bot.isOp(newOp.getId(), msg.getGuild().getId()))
			{
				targetsrv.addOperator(newOp.getId());
				String newVal = "";
				
				for(String entry : targetsrv.getOperators())
				{
					newVal += entry +",";
				}
				
				bot.changeServer(targetsrv.getId(), "Operators", newVal);
				sendMessage(bot,msg,possibleUsers.get(0).getAsMention() +" is now an Operator. (Promoted by "+ msg.getAuthor().getUsername() +")");
			}
			else
			{
				targetsrv.removeOperator(newOp.getId());
				String newVal = "";
				
				for(String entry : targetsrv.getOperators())
				{
					newVal += entry +",";
				}
				
				bot.changeServer(targetsrv.getId(), "Operators", newVal);
				sendMessage(bot,msg,possibleUsers.get(0).getAsMention() +" is no longer an Operator. (Demoted by "+ msg.getAuthor().getUsername() +")");
			}
		}
	}
}
