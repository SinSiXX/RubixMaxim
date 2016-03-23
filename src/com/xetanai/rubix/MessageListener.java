package com.xetanai.rubix;

import net.dv8tion.jda.hooks.ListenerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.xetanai.rubix.Commands.Command;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class MessageListener extends ListenerAdapter{
	private Bot bot;
	
	public MessageListener(Bot robot) {
		bot = robot;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
		if(event.getAuthor().getId().equals(bot.getJDA().getSelfInfo().getId()))
			return; /* I don't exist. */
		
		Person sender = bot.loadUser(event.getAuthor().getId());
		if(sender == null)
		{
			if(event.getMessage().getContent().contains(bot.getSettings().getPrefix()))
				event.getChannel().sendMessage("Generating a user file for you, try the command again in a second.");
			return;
		}
		if(sender.isAfk() && !event.getMessage().getContent().startsWith(bot.getSettings().getPrefix()))
		{
			bot.saveUser(sender.setAfk());
			event.getChannel().sendMessage("Welcome back, " + event.getAuthor().getAsMention() +"!");
		}
		
		String prefix = bot.getSettings().getPrefix();
		
		try {
			if(isVulgar(event))
			{
				event.getMessage().deleteMessage();
				event.getAuthor().getPrivateChannel().sendMessage("Uh oh. It looks like you were using inappropriate language in a channel/server where it's not allowed, so your message has been removed.\nYour message was:\n"+ event.getMessage().getRawContent());
				return;
			}
		} catch (Exception e) {
			event.getChannel().sendMessage("An error occured.\n```"+ e +"```");
			e.printStackTrace();
		}
		
		for(User x : event.getMessage().getMentionedUsers())
		{
			Person usr = bot.loadUser(x.getId());
			if(usr.isAfk())
			{
				event.getChannel().sendMessage(x.getAsMention() +" is currently afk. Your message will be sent to them by PM.");
				x.getPrivateChannel().sendMessage(event.getAuthor().getAsMention() +" tried to say something to you while you were marked as AFK. Their message was:\n```"+ event.getMessage().getContent() +"```");
			}
		}
		
        if (event.getMessage().getContent().startsWith(prefix))
        {
        	System.out.println("[MessageListener] Command received from "+ event.getAuthor().getUsername() +": "+ event.getMessage().getContent());
        	if (bot.getSettings().deleteCommands() && !event.isPrivate())
        		event.getMessage().deleteMessage();
        }
        else
        	return;
        
        for (Command cmd : bot.getCommandList())
        {
        	if (event.getMessage().getContent().startsWith(prefix+cmd.getKeyword()))
        	{
        		try {
        			if(cmd.getElevated())
        			{
        				if(bot.userIsOp(event.getAuthor().getId()))
        				{
        					cmd.onCalled(bot,event);
        					return;
        				}
        				event.getChannel().sendMessage("An error occured.\n```Insufficient permissions.```");
        			}
        			else if(cmd.isNsfw())
        			{
        				if(isChannelNsfw(event.getTextChannel()))
        					cmd.onCalled(bot,event);
        			}
        			else
        			{
        				cmd.onCalled(bot,event);
        			}
				} catch (Exception e) {
					event.getChannel().sendMessage("An error occured.\n```"+ e +"```");
					e.printStackTrace();
				}
        		return;
        	}
        }
        for (Alias alias : bot.getAliasList())
        {
        	if (event.getMessage().getContent().startsWith(prefix+alias.getKeyword()))
        	{
        		System.out.println("[MessageListener] Command is an alias for "+ alias.getCommand().getKeyword() +". Executing that.");
        		try {
        			if(alias.getCommand().getElevated())
        			{
        				if(bot.userIsOp(event.getAuthor().getId()))
        				{
        					alias.getCommand().onCalled(bot,event);
        					return;
        				}
        				event.getChannel().sendMessage("An error occured.\n```Insufficient permissions.```");
        			}
        			else
        			{
        				alias.getCommand().onCalled(bot,event);
        			}
				} catch (Exception e) {
					event.getChannel().sendMessage("An error occured.\n```"+ e +"```");
					e.printStackTrace();
				}
        		return;
        	}
        }
        
        System.out.println("[MessageListener] Not a registered command. Ignoring.");
    }
	
	public boolean isChannelNsfw(TextChannel channel) throws Exception
	{
		List<String> chans = Files.readAllLines(Paths.get("data/nsfw.at"));
		for(String entry : chans)
			if(entry.split("@")[1].equals(channel.getGuild().getName()) || entry.split("@")[1].equals("*"))
				if(entry.split("@")[0].equals(channel.getName()) || entry.split("@")[0].equals("*"))
				{
					return true;
				}
		return false;
	}
	
	public boolean isVulgar(MessageReceivedEvent message) throws Exception
	{
		if(message.getAuthor().equals(bot.getJDA().getSelfInfo()) || message.isPrivate())
			return false;
		
		List<Role> botroles = message.getGuild().getRolesForUser(bot.getJDA().getSelfInfo());		
		List<String> bannedwords = Files.readAllLines(Paths.get("data/bannedwords.txt"));
		
		
		boolean canRemove = false;
		for(Role x : botroles)
			if(x.hasPermission(Permission.MESSAGE_MANAGE))
				canRemove = true;
		if(!canRemove)
			return false;
		
		String msg = message.getMessage().getRawContent().toLowerCase();
		
		for(String word : bannedwords)
			if(msg.equals(word) || msg.matches(".*\\b"+ word +"\\b.*"))
			{
				System.out.println("[Chat Filter] Profanity detected from "+ message.getAuthor().getUsername() +" in "+ message.getTextChannel().getName() +"@"+ message.getGuild().getName() +".");
				return (!isChannelNsfw(message.getTextChannel()));
			}
		return false;
	}
}
