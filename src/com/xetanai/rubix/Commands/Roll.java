package com.xetanai.rubix.Commands;

import java.util.Random;

import com.xetanai.rubix.Bot;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Roll extends Command {
	private static String keyword = "roll";
	private static String usage = "roll [NdN]";
	private static String helpLong = "Example: roll 1d20.\nUp to 5 dice are allowed in a single roll.";
	private static String helpShort = "Rolls dice when given a NdN format.";
	
	public Roll(){
		super(helpShort,helpLong,keyword,usage);
	}
	
	public void onCalled(Bot bot, MessageReceivedEvent msg)
	{
		String[] params = msg.getMessage().getContent().split(" ");
		
		if(params.length==1)
		{
			sendMessage(bot, msg, "Try again in NdN format. eg; 1d20.");
			return;
		}
		int dice = Integer.parseInt(params[1].split("d")[0]);
		int sides = Integer.parseInt(params[1].split("d")[1]);
		
		int total = 0;
		int[] rolls = null;
		if(dice <= 10)
		{
			rolls = new int[dice];
			Random rand = new Random();
			for(int i=0;i<dice;i++)
			{
				rolls[i] = rand.nextInt(sides) + 1;
				total+=rolls[i];
			}
		}
		else
		{
			sendMessage(bot, msg, "No more than 10 dice per roll, "+ msg.getAuthor().getAsMention());
			return;
		}
		String post = msg.getAuthor().getAsMention() +" rolled a d"+ sides +" and got **";
		for(int i=0;i<dice;i++)
			post+=rolls[i] +", ";
		post+="**totalling to **"+ total +"**.";
		
		sendMessage(bot, msg, post);
	}
}
