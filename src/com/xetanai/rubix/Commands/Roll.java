package com.xetanai.rubix.Commands;

import java.util.Random;

import com.xetanai.rubix.enitites.Server;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Roll extends Command {
	public Roll(){
		super("roll");
		setUsage("roll [NdN]");
		setHelp("Rolls dice.",false);
		setHelp("Rolls dice.\n"
				+ "By default, rolls one 100 sided die.\n"
				+ "If a number is provided, then that will be the number of sides on the die rolled.\n"
				+ "If an NdN is provided (ID, 2d20), then it will roll multiple dice of that type.\n"
				+ "A maximum of 10 dice are allowed in a single roll.",true);
	}
	
	@Override
	public void onCalled(MessageReceivedEvent msg, String[] params, Server guild)
	{		
		int dice = 0;
		int sides=0;
		
		if(params.length == 2)
		{
			String[] parts = params[1].split("d");
			
			try {
				dice = Integer.parseInt(parts[0]);
				if(parts.length==2)
					sides = Integer.parseInt(parts[1]);
				else
				{
					sides = dice;
					dice = 1;
				}
				
				if(!(sides>0 && dice>0))
				{
					sendMessage(msg, "You must supply only numbers greater than 0.");
					return;
				}
			} catch(NumberFormatException e) {
				sendMessage(msg, "Please enter your roll in NdN format. (1d20, 2d6, etc.)");
				return;
			}
		}
		else
		{
			dice = 1;
			sides = 100;
		}
		
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
			sendMessage(msg, "No more than 10 dice per roll, "+ msg.getAuthor().getAsMention());
			return;
		}
		String post = msg.getAuthor().getAsMention() +" rolled a d"+ sides +" and got **";
		for(int i=0;i<dice;i++)
			post+=rolls[i] +", ";
		post+="**totalling to **"+ total +"**.";
		
		sendMessage(msg, post);
	}
}
