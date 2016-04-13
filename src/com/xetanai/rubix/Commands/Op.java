package com.xetanai.rubix.Commands;

import java.sql.SQLException;
import java.util.List;

import com.xetanai.rubix.Bot;
import com.xetanai.rubix.Person;
import com.xetanai.rubix.SQLUtils;
import com.xetanai.rubix.Server;

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
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild) throws SQLException
	{
		if(msg.getMessage().mentionsEveryone())
		{
			sendMessage(msg,msg.getAuthor().getUsername() +" plz... That's a horrible plan.");
			return;
		}
		else if(params.length==1)
		{
			sendMessage(msg,"Please supply an @Mention or username to Op.");
			return;
		}
		if(msg.getMessage().getMentionedUsers().size() > 1)
		{
			sendMessage(msg, "One at a time please! Operator status isn't candy, "+ msg.getAuthor().getAsMention() +"!");
			return;
		}
		
		List<String> users = getIdsInParams(msg,params);

		Person newOp = SQLUtils.loadUser(users.get(0));
		Server targetsrv = SQLUtils.loadServer(msg.getGuild().getId());
		
		if(!Bot.userIsOp(newOp.getId(), msg.getGuild().getId()))
		{
			targetsrv.addOperator(newOp.getId());
			sendMessage(msg,Bot.jda.getUserById(users.get(0)).getUsername() +" is now an Operator. (Promoted by "+ msg.getAuthor().getUsername() +")");
		}
		else
		{
			targetsrv.removeOperator(newOp.getId());
			sendMessage(msg,Bot.jda.getUserById(users.get(0)).getUsername() +" is no longer an operator. (Demoted by "+ msg.getAuthor().getUsername() +")");
		}
	}
}
